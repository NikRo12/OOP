package ru.nsu.romanenko.Master;

import ru.nsu.romanenko.Protocol.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Master {
    private final int port;
    private final BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
    private volatile CompletableFuture<Boolean> currentSessionFuture;
    private final AtomicInteger remainingTasks = new AtomicInteger(0);
    private static final int PARTS_COUNT = 10;

    public Master(int port) {
        this.port = port;
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Master started on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        handleConnection(socket);
                    } catch (IOException | ClassNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                }).start();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void handleConnection(Socket socket) throws IOException, ClassNotFoundException {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        Object handshake = in.readObject();

        if (handshake instanceof SlaveHandShake) {
            int id = ((SlaveHandShake) handshake).slaveID();
            System.out.println("Slave connected, ID: " + id);
            new SlaveHandler(socket, in, out, id).run();

        } else if (handshake instanceof ClientHandShake) {
            System.out.println("Client connected");
            handleClient((ClientHandShake) handshake, out);
        } else {
            socket.close();
        }
    }

    private void handleClient(ClientHandShake handshake, ObjectOutputStream out) throws IOException {
        int[] data = handshake.data();

        currentSessionFuture = new CompletableFuture<>();
        taskQueue.clear();

        List<Task> tasks = splitArrayToTasks(data, PARTS_COUNT);
        remainingTasks.set(tasks.size());
        taskQueue.addAll(tasks);

        System.out.println("Job started: " + tasks.size() + " tasks created.");

        try {
            boolean finalRes = currentSessionFuture.get();
            out.writeObject(new Result(finalRes, -1));
            System.out.println("Job finished. Result sent: " + finalRes);

        } catch (InterruptedException | ExecutionException ex) {
            System.out.println(ex.getMessage());
        } finally {
            taskQueue.clear();
        }
    }

    private List<Task> splitArrayToTasks(int[] array, int tasksCount) {
        List<Task> tasks = new ArrayList<>();
        int len = array.length;
        if (len == 0) return tasks;

        int chunkSize = len / tasksCount;
        if (chunkSize == 0) chunkSize = 1;

        int start = 0;
        int id = 0;
        while (start < len) {
            int end = Math.min(len, start + chunkSize);
            int[] chunk = new int[end - start];
            System.arraycopy(array, start, chunk, 0, chunk.length);
            tasks.add(new Task(chunk, id++));
            start = end;
        }
        return tasks;
    }

    private class SlaveHandler {
        private final Socket socket;
        private final ObjectInputStream in;
        private final ObjectOutputStream out;
        private final int slaveId;

        public SlaveHandler(Socket socket, ObjectInputStream in, ObjectOutputStream out, int id) {
            this.socket = socket;
            this.in = in;
            this.out = out;
            this.slaveId = id;
        }

        public void run() {
            Task currentTask = null;
            try {
                while (!socket.isClosed()) {
                    if (currentSessionFuture != null && currentSessionFuture.isDone()) {
                        continue;
                    }

                    currentTask = taskQueue.take();
                    out.writeObject(currentTask);
                    Result result = (Result) in.readObject();

                    if (result.foundNotPrime()) {
                        if (currentSessionFuture != null && !currentSessionFuture.isDone()) {
                            currentSessionFuture.complete(true);
                        }
                        taskQueue.clear();
                    } else {
                        int left = remainingTasks.decrementAndGet();
                        if (left == 0) {
                            if (currentSessionFuture != null && !currentSessionFuture.isDone()) {
                                currentSessionFuture.complete(false);
                            }
                        }
                    }
                    currentTask = null;
                }
            } catch (Exception e) {
                System.err.println("Slave " + slaveId + " disconnected or failed.");
                if (currentTask != null) {
                    System.out.println("Returning task " + currentTask.taskID() + " to queue.");
                    taskQueue.add(currentTask);
                }
            } finally {
                try { socket.close(); } catch (IOException ex) { System.out.println(ex.getMessage()); }
            }
        }
    }
}