package ru.nsu.romanenko.checker;

import ru.nsu.romanenko.dsl.OopCheckerConfig;
import ru.nsu.romanenko.model.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class CheckRunner {

    private static final Logger log = Logger.getLogger(CheckRunner.class.getName());

    private final OopCheckerConfig config;
    private final GitManager gitManager;
    private final BuildManager buildManager;
    private final StudentProcessor studentProcessor;
    private final ExecutorService taskExecutor;

    private final Map<String, StudentResult> results = new ConcurrentHashMap<>();

    public CheckRunner(OopCheckerConfig config) {
        Path workDir = Paths.get(System.getProperty("user.dir"), "repos");
        ProcessExecutor executor = new ProcessExecutor();
        this.config = config;
        this.gitManager = new GitManager(workDir, executor);
        this.buildManager = new BuildManager(
            config.getGradeConfig().getTestTimeoutSeconds(), executor);
        this.taskExecutor = Executors.newCachedThreadPool();
        this.studentProcessor = new StudentProcessor(config, gitManager, buildManager, new ScoreCalculator(), taskExecutor);
    }

    public void run() throws IOException {
        if (!gitManager.isGitAvailable()) {
            log.severe("git is not available. Please ensure git is installed.");
        }
        Files.createDirectories(gitManager.getWorkDir());
        try {
            processStudentsInParallel(buildStudentTaskMap());
        } finally {
            buildManager.shutdown();
            taskExecutor.shutdown();
        }
    }

    private void processStudentsInParallel(Map<String, Set<String>> studentTasks) {
        int threads = Math.max(1, Math.min(studentTasks.size(),
            Runtime.getRuntime().availableProcessors()));
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        List<Future<?>> futures = new ArrayList<>();
        for (Map.Entry<String, Set<String>> e : studentTasks.entrySet()) {
            futures.add(pool.submit(() -> {
                StudentResult sr = studentProcessor.process(e.getKey(), e.getValue());
                if (sr != null) results.put(e.getKey(), sr);
            }));
        }
        pool.shutdown();

        for (Future<?> f : futures) {
            try {
                f.get();
            } catch (ExecutionException | InterruptedException e) {
                log.warning("Student processing error: " + e.getMessage());
            }
        }
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
