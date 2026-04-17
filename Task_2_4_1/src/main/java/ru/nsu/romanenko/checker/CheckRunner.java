package ru.nsu.romanenko.checker;

import ru.nsu.romanenko.dsl.OopCheckerConfig;
import ru.nsu.romanenko.model.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Logger;

public class CheckRunner {

    private static final Logger log = Logger.getLogger(CheckRunner.class.getName());

    private final OopCheckerConfig config;
    private final GitManager gitManager;
    private final BuildManager buildManager;
    private final ScoreCalculator scoreCalculator;

    private final Map<String, StudentResult> results = new LinkedHashMap<>();

    public CheckRunner(OopCheckerConfig config) {
        Path workDir = Paths.get(System.getProperty("user.dir"), "repos");
        this.config = config;
        this.gitManager = new GitManager(workDir);
        this.buildManager = new BuildManager(
            config.getGradeConfig().getTestTimeoutSeconds(), gitManager);
        this.scoreCalculator = new ScoreCalculator();
    }

    public void run() throws IOException {
        if (!gitManager.isGitAvailable()) {
            log.severe("git is not available or is not configured. Please ensure git is installed.");
        }

        Path reposDir = gitManager.getWorkDir();
        Files.createDirectories(reposDir);

        Map<String, Set<String>> studentTasks = buildStudentTaskMap();

        for (Map.Entry<String, Set<String>> entry : studentTasks.entrySet()) {
            String github = entry.getKey();
            Set<String> taskIds = entry.getValue();

            Student student = config.findStudent(github);
            if (student == null) {
                log.warning("Student not found in config: " + github);
                continue;
            }

            StudentResult studentResult = new StudentResult(student);
            results.put(github, studentResult);

            Path repoPath = null;
            try {
                repoPath = gitManager.cloneOrUpdate(student.getRepoUrl(), github, taskIds);
                studentResult.setRepoCloned(true);
                log.info("Repository ready for " + github);
            } catch (IOException e) {
                studentResult.setRepoCloned(false);
                studentResult.setRepoCloneError(e.getMessage());
                log.warning("Could not clone/update repo for " + github + ": " + e.getMessage());
            }

            for (String taskId : taskIds) {
                TaskResult taskResult = new TaskResult(taskId);

                if (repoPath == null) {
                    taskResult.setStatus(TaskResult.Status.NOT_CHECKED);
                    taskResult.setErrorMessage("Repository unavailable");
                } else {
                    checkTask(repoPath, taskId, taskResult);
                }

                int bonus = config.getGradeConfig().getBonus(github, taskId);
                taskResult.setBonusScore(bonus);

                Task task = config.getTask(taskId);
                if (task != null) {
                    scoreCalculator.calculate(taskResult, task, config.getGradeConfig());
                }

                studentResult.addTaskResult(taskResult);

                log.info(String.format("  [%s] %s: status=%s, score=%.1f",
                    github, taskId, taskResult.getStatus(), taskResult.getTotalScore()));
            }
        }
    }

    private void checkTask(Path repoPath, String taskId, TaskResult taskResult) {
        Path taskDir = gitManager.findTaskDir(repoPath, taskId);

        taskResult.setLastCommitDate(gitManager.getLastCommitDate(repoPath, taskId));
        buildManager.runPipeline(taskDir, taskResult);
    }
    
    private Map<String, Set<String>> buildStudentTaskMap() {
        Map<String, Set<String>> map = new LinkedHashMap<>();
        for (AssignmentEntry entry : config.getAssignments()) {
            for (String github : entry.getStudentGithubs()) {
                map.computeIfAbsent(github, k -> new LinkedHashSet<>())
                   .addAll(entry.getTaskIds());
            }
        }
        return map;
    }

    public Map<String, StudentResult> getResults() {
        return Collections.unmodifiableMap(results);
    }

    public OopCheckerConfig getConfig() {
        return config;
    }
}
