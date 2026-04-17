package ru.nsu.romanenko.checker;

import ru.nsu.romanenko.model.TaskResult;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.regex.*;

public class BuildManager {

    private static final Logger log = Logger.getLogger(BuildManager.class.getName());

    private final int timeoutSeconds;
    private final GitManager gitManager;

    public BuildManager(int timeoutSeconds, GitManager gitManager) {
        this.timeoutSeconds = timeoutSeconds;
        this.gitManager = gitManager;
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

        if (!compile(taskDir, tool, result)) return;

        generateDocsAndCheckStyle(taskDir, tool, result);

        if (!result.isDocGenerated() || !result.isStyleOk()) {
            result.setStatus(TaskResult.Status.NOT_CHECKED);
            result.setErrorMessage("Tests skipped: "
                + (!result.isDocGenerated() ? "javadoc failed" : "style check failed"));
            return;
        }

        runTests(taskDir, tool, result);
    }

    private boolean compile(Path taskDir, BuildTool tool, TaskResult result) {
        log.info("Compiling " + taskDir.getFileName() + " with " + tool);
        String[] cmd = switch (tool) {
            case GRADLE -> buildGradleCmd(taskDir, "compileJava");
            case MAVEN  -> new String[]{"mvn", "-q", "compile", "-f", taskDir.resolve("pom.xml").toString()};
        };

        try {
            GitManager.ProcessResult pr = runWithTimeout(taskDir, cmd);
            if (pr.isSuccess()) {
                result.setCompiled(true);
                return true;
            } else {
                result.setStatus(TaskResult.Status.COMPILE_ERROR);
                result.setErrorMessage(truncate(pr.stderr(), 2000));
                return false;
            }
        } catch (Exception e) {
            result.setStatus(TaskResult.Status.COMPILE_ERROR);
            result.setErrorMessage("Compile exception: " + e.getMessage());
            return false;
        }
    }

    private void generateDocsAndCheckStyle(Path taskDir, BuildTool tool, TaskResult result) {
        log.info("Generating javadoc for " + taskDir.getFileName());
        String[] docCmd = switch (tool) {
            case GRADLE -> buildGradleCmd(taskDir, "javadoc");
            case MAVEN  -> new String[]{"mvn", "-q", "javadoc:javadoc",
                                        "-f", taskDir.resolve("pom.xml").toString()};
        };
        try {
            GitManager.ProcessResult pr = runWithTimeout(taskDir, docCmd);
            result.setDocGenerated(pr.isSuccess());
            if (!pr.isSuccess()) log.warning("Javadoc failed for " + taskDir.getFileName());
        } catch (Exception e) {
            result.setDocGenerated(false);
            log.warning("Javadoc failed: " + e.getMessage());
        }

        log.info("Checking Google Java Style for " + taskDir.getFileName());
        String[] styleCmd = switch (tool) {
            case GRADLE -> buildGradleCmd(taskDir, "checkstyleMain");
            case MAVEN  -> new String[]{"mvn", "-q", "checkstyle:check",
                                        "-f", taskDir.resolve("pom.xml").toString()};
        };
        try {
            GitManager.ProcessResult pr = runWithTimeout(taskDir, styleCmd);
            if (pr.isSuccess()) {
                result.setStyleOk(true);
            } else if (isTaskNotFound(pr)) {
                // Checkstyle not configured in student's project — treat as not checked, don't block
                result.setStyleOk(true);
                log.info("Checkstyle not configured in " + taskDir.getFileName() + ", skipping style check");
            } else {
                result.setStyleOk(false);
                log.warning("Style violations found in " + taskDir.getFileName());
            }
        } catch (Exception e) {
            result.setStyleOk(false);
            log.warning("Style check failed: " + e.getMessage());
        }
    }

    private void runTests(Path taskDir, BuildTool tool, TaskResult result) {
        log.info("Running tests for " + taskDir.getFileName());
        String[] testCmd = switch (tool) {
            case GRADLE -> buildGradleCmd(taskDir, "test");
            case MAVEN  -> new String[]{"mvn", "-q", "test",
                                        "-f", taskDir.resolve("pom.xml").toString()};
        };

        try {
            GitManager.ProcessResult pr = runWithTimeout(taskDir, testCmd);
            parseTestResults(taskDir, tool, pr, result);

            if (result.getTestsFailed() > 0) {
                result.setStatus(TaskResult.Status.TESTS_FAILED);
            } else {
                result.setStatus(TaskResult.Status.SUCCESS);
            }
        } catch (TimeoutException e) {
            result.setStatus(TaskResult.Status.TESTS_FAILED);
            result.setErrorMessage("Tests timed out after " + timeoutSeconds + "s");
        } catch (Exception e) {
            result.setStatus(TaskResult.Status.TESTS_FAILED);
            result.setErrorMessage("Test exception: " + e.getMessage());
        }
    }

    private void parseTestResults(Path taskDir, BuildTool tool,
                                  GitManager.ProcessResult pr, TaskResult result) {
        Path reportsDir = switch (tool) {
            case GRADLE -> taskDir.resolve("build/test-results/test");
            case MAVEN  -> taskDir.resolve("target/surefire-reports");
        };

        int passed = 0, failed = 0, skipped = 0;

        if (Files.isDirectory(reportsDir)) {
            try (var stream = Files.list(reportsDir)) {
                for (Path xmlFile : stream.filter(p -> p.toString().endsWith(".xml"))
                                         .toList()) {
                    int[] counts = parseJUnitXml(xmlFile);
                    passed  += counts[0];
                    failed  += counts[1];
                    skipped += counts[2];
                }
            } catch (IOException e) {
                log.warning("Could not read test reports: " + e.getMessage());
            }
        }

        if (passed + failed + skipped == 0) {
            int[] counts = parseOutputSummary(pr.stdout() + pr.stderr());
            passed  = counts[0];
            failed  = counts[1];
            skipped = counts[2];
        }

        result.setTestsPassed(passed);
        result.setTestsFailed(failed);
        result.setTestsSkipped(skipped);
    }

    private int[] parseJUnitXml(Path xmlFile) {
        try {
            String content = Files.readString(xmlFile);
            int tests   = parseAttr(content, "tests");
            int failures= parseAttr(content, "failures");
            int errors  = parseAttr(content, "errors");
            int skipped = parseAttr(content, "skipped");
            int passed  = tests - failures - errors - skipped;
            return new int[]{Math.max(0, passed), failures + errors, skipped};
        } catch (Exception e) {
            return new int[]{0, 0, 0};
        }
    }

    private int parseAttr(String xml, String attr) {
        Pattern p = Pattern.compile(attr + "=\"(\\d+)\"");
        Matcher m = p.matcher(xml);
        return m.find() ? Integer.parseInt(m.group(1)) : 0;
    }

    private int[] parseOutputSummary(String output) {
        Pattern p = Pattern.compile(
            "(\\d+) tests? completed(?:, (\\d+) failed)?(?:, (\\d+) skipped)?");
        Matcher m = p.matcher(output);
        if (m.find()) {
            int total   = Integer.parseInt(m.group(1));
            int failed  = m.group(2) != null ? Integer.parseInt(m.group(2)) : 0;
            int skipped = m.group(3) != null ? Integer.parseInt(m.group(3)) : 0;
            return new int[]{total - failed - skipped, failed, skipped};
        }
        return new int[]{0, 0, 0};
    }

    private enum BuildTool { GRADLE, MAVEN }

    private BuildTool detectBuildTool(Path taskDir) {
        if (Files.exists(taskDir.resolve("build.gradle")) ||
            Files.exists(taskDir.resolve("build.gradle.kts"))) return BuildTool.GRADLE;
        if (Files.exists(taskDir.resolve("pom.xml"))) return BuildTool.MAVEN;
        return null;
    }

    private String[] buildGradleCmd(Path taskDir, String task) {
        String gradleCmd;
        Path gradlew = taskDir.resolve(
            System.getProperty("os.name", "").toLowerCase().contains("win")
                ? "gradlew.bat" : "gradlew");
        if (Files.isExecutable(gradlew)) {
            gradleCmd = gradlew.toAbsolutePath().toString();
        } else {
            gradleCmd = "gradle";
        }
        return new String[]{gradleCmd, task, "--no-daemon", "-q",
                            "-p", taskDir.toAbsolutePath().toString()};
    }

    private GitManager.ProcessResult runWithTimeout(Path workDir, String... cmd)
        throws IOException, TimeoutException, InterruptedException {

        ExecutorService exec = Executors.newSingleThreadExecutor();
        Future<GitManager.ProcessResult> future = exec.submit(() ->
            gitManager.runProcess(workDir.toFile(), cmd));
        exec.shutdown();

        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (java.util.concurrent.TimeoutException e) {
            future.cancel(true);
            throw new TimeoutException("Command timed out: " + String.join(" ", cmd));
        } catch (ExecutionException e) {
            throw new IOException("Command failed", e.getCause());
        }
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max) + "..." : s;
    }

    private boolean isTaskNotFound(GitManager.ProcessResult pr) {
        String combined = pr.stdout() + pr.stderr();
        return combined.contains("not found") || combined.contains("Task '") && combined.contains("not found");
    }
}
