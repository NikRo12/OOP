package ru.nsu.romanenko.checker;

import java.io.*;
import java.util.logging.Logger;

public class ProcessExecutor implements ProcessRunner {

    private static final Logger log = Logger.getLogger(ProcessExecutor.class.getName());

    @Override
    public ProcessResult run(File workDir, String... command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(workDir);
        pb.redirectErrorStream(false);

        Process process = pb.start();
        StringBuilder stdout = new StringBuilder();
        StringBuilder stderr = new StringBuilder();

        Thread outThread = new Thread(() -> {
            try (BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = r.readLine()) != null) stdout.append(line).append('\n');
            } catch (IOException ignored) {}
        });
        Thread errThread = new Thread(() -> {
            try (BufferedReader r = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
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
}
