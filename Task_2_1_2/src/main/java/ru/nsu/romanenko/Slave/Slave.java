package ru.nsu.romanenko.Slave;

import ru.nsu.romanenko.Protocol.CancelTask;
import ru.nsu.romanenko.Protocol.Result;
import ru.nsu.romanenko.Protocol.SlaveHandShake;
import ru.nsu.romanenko.Protocol.Task;
import ru.nsu.romanenko.Solution.Solution;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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
        AtomicBoolean readerDied = new AtomicBoolean(false);
        Thread reader = startReader(in, taskQueue, currentCancelled, Thread.currentThread(), readerDied);

        try {
            while (!Thread.currentThread().isInterrupted()) {
                Task task;
                try {
                    task = taskQueue.take();
                } catch (InterruptedException e) {
                    if (readerDied.get()) throw new IOException("Connection lost");
                    throw e;
                }

                AtomicBoolean cancelled = new AtomicBoolean(false);
                currentCancelled.set(cancelled);
                boolean found = Solution.consistently(task.numbers(), cancelled);

                out.writeObject(cancelled.get() ? new Result(false, -1) : new Result(found, task.taskID()));
                out.reset();
            }
        } finally {
            reader.interrupt();
        }
    }

    private Thread startReader(ObjectInputStream in, BlockingQueue<Task> taskQueue,
                               AtomicReference<AtomicBoolean> currentCancelled,
                               Thread mainThread, AtomicBoolean readerDied) {
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
                readerDied.set(true);
                mainThread.interrupt();
            }
        });
        reader.setDaemon(true);
        reader.start();
        return reader;
    }
}
