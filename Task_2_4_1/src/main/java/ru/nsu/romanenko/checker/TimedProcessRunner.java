package ru.nsu.romanenko.checker;

import java.io.*;
import java.util.concurrent.*;

public class TimedProcessRunner implements ProcessRunner {

    private final ProcessRunner delegate;
    private final int timeoutSeconds;
    private final ExecutorService executor;

    public TimedProcessRunner(ProcessRunner delegate, int timeoutSeconds) {
        this.delegate = delegate;
        this.timeoutSeconds = timeoutSeconds;
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public ProcessResult run(File workDir, String... command) throws IOException {
        Future<ProcessResult> future = executor.submit(() -> delegate.run(workDir, command));
        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new IOException("timed out after " + timeoutSeconds + "s: " + String.join(" ", command));
        } catch (ExecutionException e) {
            throw new IOException("Command failed", e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted", e);
        }
    }

    public void shutdown() {
        executor.shutdownNow();
    }
}
