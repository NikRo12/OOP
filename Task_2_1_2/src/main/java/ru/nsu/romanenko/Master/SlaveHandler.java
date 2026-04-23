package ru.nsu.romanenko.Master;

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

    @Override
    public void run() {
        Task currentTask = null;
        try {
            while (!socket.isClosed()) {
                currentTask = sessionManager.takeTask();
                out.writeObject(currentTask);
                out.reset();
                Result result = (Result) in.readObject();
                sessionManager.reportResult(result);
                currentTask = null;
            }
        } catch (Exception e) {
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
