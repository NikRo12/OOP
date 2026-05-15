package ru.nsu.romanenko.checker;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class GitManager {

    private static final Logger log = Logger.getLogger(GitManager.class.getName());

    private final Path workDir;
    private final ProcessRunner processRunner;

    public GitManager(Path workDir, ProcessRunner processRunner) {
        this.workDir = workDir;
        this.processRunner = processRunner;
    }

    public Path getWorkDir() { return workDir; }

    public boolean isGitAvailable() {
        try {
            ProcessResult result = processRunner.run(
                new File(System.getProperty("user.dir")), "git", "--version");
            return result.isSuccess();
        } catch (Exception e) {
            return false;
        }
    }

    public Path cloneOrUpdate(String repoUrl, String github,
                              java.util.Set<String> taskIds) throws IOException {
        Path repoPath = workDir.resolve(github);
        if (Files.exists(repoPath.resolve(".git"))) {
            log.info("Repository exists for " + github + ", pulling latest changes...");
            return pull(repoPath, github, taskIds);
        } else {
            log.info("Cloning repository for " + github + " from " + repoUrl);
            return clone(repoUrl, github, repoPath, taskIds);
        }
    }

    private Path clone(String repoUrl, String github, Path repoPath,
                       java.util.Set<String> taskIds) throws IOException {
        Files.createDirectories(workDir);

        ProcessResult result = processRunner.run(workDir.toFile(),
            "git", "clone", "--filter=blob:none", "--no-checkout", repoUrl, github);
        if (!result.isSuccess()) {
            throw new IOException("git clone failed for " + github + ":\n" + result.stderr());
        }

        processRunner.run(repoPath.toFile(), "git", "sparse-checkout", "init", "--cone");
        processRunner.run(repoPath.toFile(), buildSparseSetCmd(taskIds));

        String branch = detectDefaultBranch(repoPath);
        ProcessResult checkout = processRunner.run(repoPath.toFile(), "git", "checkout", branch);
        if (!checkout.isSuccess()) {
            throw new IOException("git checkout failed for " + github + ":\n" + checkout.stderr());
        }

        log.info("Sparse clone done for " + github + " (tasks: " + taskIds + ")");
        return repoPath;
    }

    private Path pull(Path repoPath, String github,
                      java.util.Set<String> taskIds) throws IOException {
        processRunner.run(repoPath.toFile(), buildSparseSetCmd(taskIds));

        ProcessResult fetch = processRunner.run(repoPath.toFile(), "git", "fetch", "--all");
        if (!fetch.isSuccess()) {
            log.warning("git fetch warning for " + github + ": " + fetch.stderr());
        }

        processRunner.run(repoPath.toFile(), "git", "checkout", detectDefaultBranch(repoPath));

        ProcessResult pull = processRunner.run(repoPath.toFile(), "git", "pull", "--ff-only");
        if (!pull.isSuccess()) {
            log.warning("git pull warning for " + github + ": " + pull.stderr());
        }

        return repoPath;
    }

    public String detectDefaultBranch(Path repoPath) {
        try {
            ProcessResult result = processRunner.run(repoPath.toFile(), "git", "branch", "-r");
            if (result.isSuccess()) {
                if (result.stdout().contains("origin/main"))   return "main";
                if (result.stdout().contains("origin/master")) return "master";
            }
        } catch (Exception ignored) {}
        return "main";
    }

    public LocalDate getLastCommitDate(Path repoPath, String taskId) {
        try {
            Path taskDir = findTaskDir(repoPath, taskId);
            String pathArg = taskDir != null ? repoPath.relativize(taskDir).toString() : ".";

            ProcessResult result = processRunner.run(repoPath.toFile(),
                "git", "log", "-1", "--format=%ci", "--", pathArg);

            if (result.isSuccess() && !result.stdout().isBlank()) {
                return LocalDate.parse(
                    result.stdout().trim().substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE);
            }
        } catch (Exception e) {
            log.warning("Could not get last commit date for task " + taskId + ": " + e.getMessage());
        }
        return null;
    }

    public Path findTaskDir(Path repoPath, String taskId) {
        Path direct = repoPath.resolve(taskId);
        if (Files.isDirectory(direct)) return direct;

        try (var stream = Files.list(repoPath)) {
            return stream
                .filter(Files::isDirectory)
                .filter(p -> p.getFileName().toString().equalsIgnoreCase(taskId))
                .findFirst().orElse(null);
        } catch (IOException e) {
            return null;
        }
    }

    private String[] buildSparseSetCmd(java.util.Set<String> taskIds) {
        java.util.List<String> cmd = new java.util.ArrayList<>();
        cmd.add("git");
        cmd.add("sparse-checkout");
        cmd.add("set");
        cmd.addAll(taskIds);
        return cmd.toArray(new String[0]);
    }
}
