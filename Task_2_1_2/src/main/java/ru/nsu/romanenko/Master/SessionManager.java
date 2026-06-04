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
    private final AtomicInteger sessionCounter = new AtomicInteger(0);

    private static class Session {
        final int id;
        final CompletableFuture<Boolean> future = new CompletableFuture<>();
        final AtomicInteger remaining;

        Session(int id, int taskCount) {
            this.id = id;
            this.remaining = new AtomicInteger(taskCount);
        }
    }

    public synchronized CompletableFuture<Boolean> startSession(int[] data) {
        Session old = currentSession;
        if (old != null && !old.future.isDone()) {
            old.future.completeExceptionally(
                    new IllegalStateException("Session superseded by a new client request"));
        }

        for (SlaveHandler handler : activeHandlers) {
            handler.sendCancel();
        }

        int sessionId = sessionCounter.incrementAndGet();
        List<Task> tasks = splitToTasks(data, sessionId, PARTS_COUNT);
        if (tasks.isEmpty()) {
            return CompletableFuture.completedFuture(false);
        }
        Session session = new Session(sessionId, tasks.size());
        taskQueue.clear();
        taskQueue.addAll(tasks);
        currentSession = session;
        System.out.println("Session " + sessionId + " started: " + tasks.size() + " tasks.");
        return session.future;
    }

    public Task takeTask() throws InterruptedException {
        return taskQueue.take();
    }

    public void returnTask(Task task) {
        Session session = currentSession;
        if (session != null && task.sessionId() == session.id) {
            taskQueue.add(task);
        } else {
            System.out.println("Discarding stale task " + task.taskID()
                    + " from session " + task.sessionId());
        }
    }

    public void registerActiveHandler(SlaveHandler handler) {
        activeHandlers.add(handler);
    }

    public void unregisterActiveHandler(SlaveHandler handler) {
        activeHandlers.remove(handler);
    }

    public void reportResult(Result result, SlaveHandler reporter, int taskSessionId) {
        Session session = currentSession;
        if (session == null || session.future.isDone() || session.id != taskSessionId) return;

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

    private List<Task> splitToTasks(int[] array, int sessionId, int count) {
        List<Task> tasks = new ArrayList<>();
        int len = array.length;
        if (len == 0) return tasks;

        int chunkSize = Math.max(1, len / count);
        int start = 0, id = 0;
        while (start < len) {
            int end = Math.min(len, start + chunkSize);
            int[] chunk = new int[end - start];
            System.arraycopy(array, start, chunk, 0, chunk.length);
            tasks.add(new Task(chunk, id++, sessionId));
            start = end;
        }
        return tasks;
    }
}
