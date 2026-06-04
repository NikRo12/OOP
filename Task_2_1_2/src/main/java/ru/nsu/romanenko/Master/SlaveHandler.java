package ru.nsu.romanenko.Master;

import ru.nsu.romanenko.Protocol.CancelTask;
import ru.nsu.romanenko.Protocol.Result;
import ru.nsu.romanenko.Protocol.Task;

import java.io.*;
import java.net.Socket;

public class SlaveHandler implements Runnable {
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final int slaveId;
    private final SessionManager sessionManager;

    public SlaveHandler(Socket socket, ObjectInputStream in, ObjectOutputStream out,
                        int slaveId, SessionManager sessionManager) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.slaveId = slaveId;
        this.sessionManager = sessionManager;
    }

    public synchronized void sendCancel() {
        try {
            out.writeObject(new CancelTask());
            out.reset();
        } catch (IOException ignored) {}
    }

    @Override
    public void run() {
        Task currentTask = null;
        try {
            while (!socket.isClosed()) {
                currentTask = sessionManager.takeTask();

                sessionManager.registerActiveHandler(this);
                synchronized (this) {
                    out.writeObject(currentTask);
                    out.reset();
                }

                Result result = (Result) in.readObject();
                sessionManager.unregisterActiveHandler(this);

                if (result.taskID() != -1) {
                    sessionManager.reportResult(result, this, currentTask.sessionId());
                }
                currentTask = null;
            }
        } catch (Exception e) {
            sessionManager.unregisterActiveHandler(this);
            System.err.println("Slave " + slaveId + " disconnected: " + e.getMessage());
            if (currentTask != null) {
                System.out.println("Returning task " + currentTask.taskID() + " to queue.");
                sessionManager.returnTask(currentTask);
            }
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
}
