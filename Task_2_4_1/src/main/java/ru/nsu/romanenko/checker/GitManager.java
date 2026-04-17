package ru.nsu.romanenko.checker;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class GitManager {

    private static final Logger log = Logger.getLogger(GitManager.class.getName());
    private final Path workDir;

    public GitManager(Path workDir) {
        this.workDir = workDir;
    }

    public Path getWorkDir() { return workDir; }

    public boolean isGitAvailable() {
        try {
            File dir = new File(System.getProperty("user.dir"));
            ProcessResult result = runProcess(dir, "git", "--version");
            return result.exitCode == 0;
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

        ProcessResult result = runProcess(workDir.toFile(),
            "git", "clone", "--filter=blob:none", "--no-checkout", repoUrl, github);
        if (result.exitCode != 0) {
            throw new IOException("git clone failed for " + github + ":\n" + result.stderr);
        }

        runProcess(repoPath.toFile(), "git", "sparse-checkout", "init", "--cone");

        String[] sparseCmd = buildSparseSetCmd(taskIds);
        runProcess(repoPath.toFile(), sparseCmd);

        String branch = detectDefaultBranch(repoPath);
        ProcessResult checkoutResult = runProcess(repoPath.toFile(), "git", "checkout", branch);
        if (checkoutResult.exitCode != 0) {
            throw new IOException("git checkout failed for " + github + ":\n" + checkoutResult.stderr);
        }

        log.info("Sparse clone done for " + github + " (tasks: " + taskIds + ")");
        return repoPath;
    }

    private Path pull(Path repoPath, String github,
                      java.util.Set<String> taskIds) throws IOException {
        runProcess(repoPath.toFile(), buildSparseSetCmd(taskIds));

        ProcessResult fetchResult = runProcess(repoPath.toFile(), "git", "fetch", "--all");
        if (fetchResult.exitCode != 0) {
            log.warning("git fetch warning for " + github + ": " + fetchResult.stderr);
        }

        String branch = detectDefaultBranch(repoPath);
        runProcess(repoPath.toFile(), "git", "checkout", branch);

        ProcessResult pullResult = runProcess(repoPath.toFile(), "git", "pull", "--ff-only");
        if (pullResult.exitCode != 0) {
            log.warning("git pull warning for " + github + ": " + pullResult.stderr);
        }

        return repoPath;
    }

    private String[] buildSparseSetCmd(java.util.Set<String> taskIds) {
        java.util.List<String> cmd = new java.util.ArrayList<>();
        cmd.add("git");
        cmd.add("sparse-checkout");
        cmd.add("set");
        cmd.addAll(taskIds);
        return cmd.toArray(new String[0]);
    }

    public String detectDefaultBranch(Path repoPath) {
        try {
            ProcessResult result = runProcess(repoPath.toFile(),
                "git", "branch", "-r");
            if (result.exitCode == 0) {
                if (result.stdout.contains("origin/main")) return "main";
                if (result.stdout.contains("origin/master")) return "master";
            }
        } catch (Exception ignored) {}
        return "main"; // default fallback
    }

    public LocalDate getLastCommitDate(Path repoPath, String taskId) {
        try {
            // Find task directory
            Path taskDir = findTaskDir(repoPath, taskId);
            String pathArg = taskDir != null ? repoPath.relativize(taskDir).toString() : ".";

            ProcessResult result = runProcess(repoPath.toFile(),
                "git", "log", "-1", "--format=%ci", "--", pathArg);

            if (result.exitCode == 0 && !result.stdout.isBlank()) {
                String dateStr = result.stdout.trim().substring(0, 10);
                return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
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

    public ProcessResult runProcess(File workDir, String... command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(workDir);
        pb.redirectErrorStream(false);

        Process process = pb.start();

        StringBuilder stdout = new StringBuilder();
        StringBuilder stderr = new StringBuilder();

        Thread outThread = new Thread(() -> {
            try (BufferedReader r = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = r.readLine()) != null) stdout.append(line).append('\n');
            } catch (IOException ignored) {}
        });
        Thread errThread = new Thread(() -> {
            try (BufferedReader r = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = r.readLine()) != null) stderr.append(line).append('\n');
            } catch (IOException ignored) {}
        });

        outThread.start();
        errThread.start();

        int exitCode;
        try {
            exitCode = process.waitFor();
            outThread.join(5000);
            errThread.join(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            process.destroyForcibly();
            throw new IOException("Process interrupted", e);
        }

        return new ProcessResult(exitCode, stdout.toString(), stderr.toString());
    }

    public record ProcessResult(int exitCode, String stdout, String stderr) {
        public boolean isSuccess() { return exitCode == 0; }
    }
}
