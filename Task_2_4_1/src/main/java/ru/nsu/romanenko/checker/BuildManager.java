package ru.nsu.romanenko.checker;

import ru.nsu.romanenko.model.TaskResult;

import java.nio.file.*;
import java.util.logging.Logger;

public class BuildManager {

    private static final Logger log = Logger.getLogger(BuildManager.class.getName());

    private final TimedProcessRunner timedRunner;
    private final TestResultParser testResultParser;

    public BuildManager(int timeoutSeconds, ProcessRunner processRunner) {
        this.timedRunner = new TimedProcessRunner(processRunner, timeoutSeconds);
        this.testResultParser = new TestResultParser();
    }

    public void shutdown() {
        timedRunner.shutdown();
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

        new BuildPipeline(timedRunner, BuildCommandFactory.forTool(tool), testResultParser, tool)
            .run(taskDir, result);
    }

    private BuildTool detectBuildTool(Path taskDir) {
        if (Files.exists(taskDir.resolve("build.gradle")) ||
            Files.exists(taskDir.resolve("build.gradle.kts"))) return BuildTool.GRADLE;
        if (Files.exists(taskDir.resolve("pom.xml")))           return BuildTool.MAVEN;
        return null;
    }
}
