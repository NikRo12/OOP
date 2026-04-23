package ru.nsu.romanenko.checker;

import ru.nsu.romanenko.model.TaskResult;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class BuildManager {

    private static final Logger log = Logger.getLogger(BuildManager.class.getName());

    private final int timeoutSeconds;
    private final ProcessRunner processRunner;
    private final TestResultParser testResultParser;
    private final ExecutorService timeoutExecutor;

    public BuildManager(int timeoutSeconds, ProcessRunner processRunner) {
        this.timeoutSeconds = timeoutSeconds;
        this.processRunner = processRunner;
        this.testResultParser = new TestResultParser();
        this.timeoutExecutor = Executors.newCachedThreadPool();
    }

    public void shutdown() {
        timeoutExecutor.shutdownNow();
    }

    public void runPipeline(Path taskDir, TaskResult result) {
        if (taskDir == null || !Files.isDirectory(taskDir)) {
            result.setStatus(TaskResult.Status.COMPILE_ERROR);
            result.setErrorMessage("Task directory not found");
            return;
        }

        BuildTool tool = detectBuildTool(taskDir);
        if (tool == null) {
            result.setStatus(TaskResult.Status.COMPILE_ERROR);
            result.setErrorMessage("No build tool found (expected build.gradle or pom.xml)");
            return;
        }

        BuildCommandFactory commands = BuildCommandFactory.forTool(tool);

        if (!compile(taskDir, commands, result)) return;
        generateDocs(taskDir, commands, result);
        checkStyle(taskDir, commands, result);

        if (!result.isDocGenerated() || !result.isStyleOk()) {
            result.setStatus(TaskResult.Status.NOT_CHECKED);
            result.setErrorMessage("Tests skipped: "
                + (!result.isDocGenerated() ? "javadoc failed" : "style check failed"));
            return;
        }

        runTests(taskDir, tool, commands, result);
    }

    private boolean compile(Path taskDir, BuildCommandFactory commands, TaskResult result) {
        log.info("Compiling " + taskDir.getFileName());
        try {
            ProcessResult pr = runWithTimeout(taskDir, commands.compileCmd(taskDir));
            if (pr.isSuccess()) { result.setCompiled(true); return true; }
            result.setStatus(TaskResult.Status.COMPILE_ERROR);
            result.setErrorMessage(truncate(pr.stderr(), 2000));
            return false;
        } catch (Exception e) {
            result.setStatus(TaskResult.Status.COMPILE_ERROR);
            result.setErrorMessage("Compile exception: " + e.getMessage());
            return false;
        }
    }

    private void generateDocs(Path taskDir, BuildCommandFactory commands, TaskResult result) {
        log.info("Generating javadoc for " + taskDir.getFileName());
        try {
            ProcessResult pr = runWithTimeout(taskDir, commands.javadocCmd(taskDir));
            result.setDocGenerated(pr.isSuccess());
            if (!pr.isSuccess()) log.warning("Javadoc failed for " + taskDir.getFileName());
        } catch (Exception e) {
            result.setDocGenerated(false);
        }
    }

    private void checkStyle(Path taskDir, BuildCommandFactory commands, TaskResult result) {
        log.info("Checking style for " + taskDir.getFileName());
        try {
            ProcessResult pr = runWithTimeout(taskDir, commands.checkstyleCmd(taskDir));
            if (pr.isSuccess() || isTaskNotFound(pr)) {
                result.setStyleOk(true);
            } else {
                result.setStyleOk(false);
                log.warning("Style violations in " + taskDir.getFileName());
            }
        } catch (Exception e) {
            result.setStyleOk(false);
        }
    }

    private void runTests(Path taskDir, BuildTool tool,
                          BuildCommandFactory commands, TaskResult result) {
        log.info("Running tests for " + taskDir.getFileName());
        try {
            ProcessResult pr = runWithTimeout(taskDir, commands.testCmd(taskDir));
            testResultParser.parse(taskDir, tool, pr, result);
            result.setStatus(result.getTestsFailed() > 0
                ? TaskResult.Status.TESTS_FAILED : TaskResult.Status.SUCCESS);
        } catch (TimeoutException e) {
            result.setStatus(TaskResult.Status.TESTS_FAILED);
            result.setErrorMessage("Tests timed out after " + timeoutSeconds + "s");
        } catch (Exception e) {
            result.setStatus(TaskResult.Status.TESTS_FAILED);
            result.setErrorMessage("Test exception: " + e.getMessage());
        }
    }

    private BuildTool detectBuildTool(Path taskDir) {
        if (Files.exists(taskDir.resolve("build.gradle")) ||
            Files.exists(taskDir.resolve("build.gradle.kts"))) return BuildTool.GRADLE;
        if (Files.exists(taskDir.resolve("pom.xml")))           return BuildTool.MAVEN;
        return null;
    }

    private ProcessResult runWithTimeout(Path workDir, String... cmd)
            throws IOException, TimeoutException, InterruptedException {
        Future<ProcessResult> future = timeoutExecutor.submit(
                () -> processRunner.run(workDir.toFile(), cmd));
        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (java.util.concurrent.TimeoutException e) {
            future.cancel(true);
            throw new TimeoutException("Command timed out: " + String.join(" ", cmd));
        } catch (ExecutionException e) {
            throw new IOException("Command failed", e.getCause());
        }
    }

    private boolean isTaskNotFound(ProcessResult pr) {
        String combined = pr.stdout() + pr.stderr();
        return combined.contains("not found") || combined.contains("Task '") && combined.contains("not found");
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max) + "..." : s;
    }
}
