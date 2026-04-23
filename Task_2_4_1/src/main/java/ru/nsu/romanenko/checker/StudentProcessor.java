package ru.nsu.romanenko.checker;

import ru.nsu.romanenko.dsl.OopCheckerConfig;
import ru.nsu.romanenko.model.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.logging.Logger;

public class StudentProcessor {

    private static final Logger log = Logger.getLogger(StudentProcessor.class.getName());

    private final OopCheckerConfig config;
    private final GitManager gitManager;
    private final BuildManager buildManager;
    private final ScoreCalculator scoreCalculator;

    public StudentProcessor(OopCheckerConfig config, GitManager gitManager,
                            BuildManager buildManager, ScoreCalculator scoreCalculator) {
        this.config = config;
        this.gitManager = gitManager;
        this.buildManager = buildManager;
        this.scoreCalculator = scoreCalculator;
    }

    public StudentResult process(String github, Set<String> taskIds) {
        Student student = config.findStudent(github);
        if (student == null) {
            log.warning("Student not found in config: " + github);
            return null;
        }

        StudentResult studentResult = new StudentResult(student);
        Path repoPath = cloneRepo(student, github, taskIds, studentResult);

        for (String taskId : taskIds) {
            TaskResult taskResult = buildTaskResult(repoPath, taskId, github);
            studentResult.addTaskResult(taskResult);
            log.info(String.format("  [%s] %s: status=%s, score=%.1f",
                github, taskId, taskResult.getStatus(), taskResult.getTotalScore()));
        }

        return studentResult;
    }

    private Path cloneRepo(Student student, String github,
                           Set<String> taskIds, StudentResult studentResult) {
        try {
            Path repoPath = gitManager.cloneOrUpdate(student.getRepoUrl(), github, taskIds);
            studentResult.setRepoCloned(true);
            log.info("Repository ready for " + github);
            return repoPath;
        } catch (IOException e) {
            studentResult.setRepoCloned(false);
            studentResult.setRepoCloneError(e.getMessage());
            log.warning("Could not clone/update repo for " + github + ": " + e.getMessage());
            return null;
        }
    }

    private TaskResult buildTaskResult(Path repoPath, String taskId, String github) {
        TaskResult taskResult = new TaskResult(taskId);

        if (repoPath == null) {
            taskResult.setStatus(TaskResult.Status.NOT_CHECKED);
            taskResult.setErrorMessage("Repository unavailable");
        } else {
            taskResult.setLastCommitDate(gitManager.getLastCommitDate(repoPath, taskId));
            buildManager.runPipeline(gitManager.findTaskDir(repoPath, taskId), taskResult);
        }

        taskResult.setBonusScore(config.getGradeConfig().getBonus(github, taskId));

        Task task = config.getTask(taskId);
        if (task != null) {
            scoreCalculator.calculate(taskResult, task, config.getGradeConfig());
        }

        return taskResult;
    }
}
