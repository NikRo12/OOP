package ru.nsu.romanenko.Master;

import ru.nsu.romanenko.Protocol.Result;
import ru.nsu.romanenko.Protocol.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionManager {
    private final BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
    private volatile Session currentSession;
    private static final int PARTS_COUNT = 10;
    private final CopyOnWriteArrayList<SlaveHandler> activeHandlers = new CopyOnWriteArrayList<>();

    private static class Session {
        final CompletableFuture<Boolean> future = new CompletableFuture<>();
        final AtomicInteger remaining;

        Session(int taskCount) {
            this.remaining = new AtomicInteger(taskCount);
        }
    }

    public synchronized CompletableFuture<Boolean> startSession(int[] data) {
        List<Task> tasks = splitToTasks(data, PARTS_COUNT);
        if (tasks.isEmpty()) {
            return CompletableFuture.completedFuture(false);
        }
        Session session = new Session(tasks.size());
        taskQueue.clear();
        taskQueue.addAll(tasks);
        currentSession = session;
        System.out.println("Session started: " + tasks.size() + " tasks.");
        return session.future;
    }

    public Task takeTask() throws InterruptedException {
        return taskQueue.take();
    }

    public void returnTask(Task task) {
        taskQueue.add(task);
    }

    public void registerActiveHandler(SlaveHandler handler) {
        activeHandlers.add(handler);
    }

    public void unregisterActiveHandler(SlaveHandler handler) {
        activeHandlers.remove(handler);
    }

    public void reportResult(Result result, SlaveHandler reporter) {
        Session session = currentSession;
        if (session == null || session.future.isDone()) return;

        if (result.foundNotPrime()) {
            if (session.future.complete(true)) {
                taskQueue.clear();
                cancelOtherSlaves(reporter);
            }
        } else {
            if (session.remaining.decrementAndGet() == 0) {
                session.future.complete(false);
            }
        }
    }

    private void cancelOtherSlaves(SlaveHandler except) {
        for (SlaveHandler handler : activeHandlers) {
            if (handler != except) {
                handler.sendCancel();
            }
        }
    }

    private List<Task> splitToTasks(int[] array, int count) {
        List<Task> tasks = new ArrayList<>();
        int len = array.length;
        if (len == 0) return tasks;

        int chunkSize = Math.max(1, len / count);
        int start = 0, id = 0;
        while (start < len) {
            int end = Math.min(len, start + chunkSize);
            int[] chunk = new int[end - start];
            System.arraycopy(array, start, chunk, 0, chunk.length);
            tasks.add(new Task(chunk, id++));
            start = end;
        }
        return tasks;
    }
}
