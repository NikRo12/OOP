package ru.nsu.romanenko.Slave;

import ru.nsu.romanenko.Protocol.CancelTask;
import ru.nsu.romanenko.Protocol.Result;
import ru.nsu.romanenko.Protocol.SlaveHandShake;
import ru.nsu.romanenko.Protocol.Task;
import ru.nsu.romanenko.Solution.Solution;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Slave {
    private final String masterHost;
    private final int masterPort;
    private final int slaveID;
    private static final int RECONNECT_DELAY_MS = 3000;

    public Slave(String masterHost, int masterPort, int slaveID) {
        this.masterHost = masterHost;
        this.masterPort = masterPort;
        this.slaveID = slaveID;
    }

    public void startSlave() {
        while (!Thread.currentThread().isInterrupted()) {
            try (Socket socket = new Socket(masterHost, masterPort)) {
                System.out.println("Slave " + slaveID + " connected to master at " + masterHost + ":" + masterPort);
                runSession(socket);
            } catch (IOException | ClassNotFoundException | InterruptedException ex) {
                if (Thread.currentThread().isInterrupted() || ex instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                    break;
                }
                System.err.println("Slave " + slaveID + " error: " + ex.getMessage()
                        + ". Reconnecting in " + RECONNECT_DELAY_MS + "ms...");
                try {
                    Thread.sleep(RECONNECT_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    private void runSession(Socket socket) throws IOException, ClassNotFoundException, InterruptedException {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        out.writeObject(new SlaveHandShake(slaveID));

        BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
        AtomicReference<AtomicBoolean> currentCancelled = new AtomicReference<>(new AtomicBoolean(false));
        Thread reader = startReader(in, taskQueue, currentCancelled);

        ExecutorService computeExecutor = Executors.newSingleThreadExecutor();
        try {
            while (!Thread.currentThread().isInterrupted()) {
                processTask(taskQueue.take(), currentCancelled, out, computeExecutor);
            }
        } catch (ExecutionException e) {
            throw new IOException("Computation failed", e);
        } finally {
            reader.interrupt();
            computeExecutor.shutdownNow();
        }
    }

    private Thread startReader(ObjectInputStream in, BlockingQueue<Task> taskQueue,
                               AtomicReference<AtomicBoolean> currentCancelled) {
        Thread reader = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Object msg = in.readObject();
                    if (msg instanceof CancelTask) {
                        currentCancelled.get().set(true);
                    } else if (msg instanceof Task task) {
                        taskQueue.put(task);
                    }
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        });
        reader.setDaemon(true);
        reader.start();
        return reader;
    }

    private void processTask(Task task, AtomicReference<AtomicBoolean> currentCancelled,
                             ObjectOutputStream out, ExecutorService computeExecutor)
            throws ExecutionException, InterruptedException, IOException {
        AtomicBoolean cancelled = new AtomicBoolean(false);
        currentCancelled.set(cancelled);

        boolean found = computeExecutor.submit(
                () -> Solution.consistently(task.numbers(), cancelled)).get();

        out.writeObject(cancelled.get() ? new Result(false, -1) : new Result(found, task.taskID()));
        out.reset();
    }
}
