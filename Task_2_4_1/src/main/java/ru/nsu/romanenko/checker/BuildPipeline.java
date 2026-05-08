package ru.nsu.romanenko.checker;

import ru.nsu.romanenko.model.TaskResult;

import java.nio.file.Path;
import java.util.logging.Logger;

public class BuildPipeline {

    private static final Logger log = Logger.getLogger(BuildPipeline.class.getName());

    private final ProcessRunner processRunner;
    private final BuildCommandFactory commands;
    private final TestResultParser testResultParser;
    private final BuildTool tool;

    public BuildPipeline(ProcessRunner processRunner, BuildCommandFactory commands,
                         TestResultParser testResultParser, BuildTool tool) {
        this.processRunner = processRunner;
        this.commands = commands;
        this.testResultParser = testResultParser;
        this.tool = tool;
    }

    public void run(Path taskDir, TaskResult result) {
        if (!compile(taskDir, result)) return;
        generateDocs(taskDir, result);
        checkStyle(taskDir, result);

        if (!result.isDocGenerated() || !result.isStyleOk()) {
            result.setStatus(TaskResult.Status.NOT_CHECKED);
            result.setErrorMessage("Tests skipped: "
                + (!result.isDocGenerated() ? "javadoc failed" : "style check failed"));
            return;
        }

        runTests(taskDir, result);
    }

    private boolean compile(Path taskDir, TaskResult result) {
        log.info("Compiling " + taskDir.getFileName());
        try {
            ProcessResult pr = processRunner.run(taskDir.toFile(), commands.compileCmd(taskDir));
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

    private void generateDocs(Path taskDir, TaskResult result) {
        log.info("Generating javadoc for " + taskDir.getFileName());
        try {
            ProcessResult pr = processRunner.run(taskDir.toFile(), commands.javadocCmd(taskDir));
            result.setDocGenerated(pr.isSuccess());
            if (!pr.isSuccess()) log.warning("Javadoc failed for " + taskDir.getFileName());
        } catch (Exception e) {
            result.setDocGenerated(false);
        }
    }

    private void checkStyle(Path taskDir, TaskResult result) {
        log.info("Checking style for " + taskDir.getFileName());
        try {
            ProcessResult pr = processRunner.run(taskDir.toFile(), commands.checkstyleCmd(taskDir));
            result.setStyleOk(pr.isSuccess() || isTaskNotFound(pr));
            if (!result.isStyleOk()) log.warning("Style violations in " + taskDir.getFileName());
        } catch (Exception e) {
            result.setStyleOk(false);
        }
    }

    private void runTests(Path taskDir, TaskResult result) {
        log.info("Running tests for " + taskDir.getFileName());
        try {
            ProcessResult pr = processRunner.run(taskDir.toFile(), commands.testCmd(taskDir));
            testResultParser.parse(taskDir, tool, pr, result);
            result.setStatus(result.getTestsFailed() > 0
                ? TaskResult.Status.TESTS_FAILED : TaskResult.Status.SUCCESS);
        } catch (Exception e) {
            result.setStatus(TaskResult.Status.TESTS_FAILED);
            result.setErrorMessage("Test exception: " + e.getMessage());
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
