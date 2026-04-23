package ru.nsu.romanenko.Slave;

import ru.nsu.romanenko.Protocol.Result;
import ru.nsu.romanenko.Protocol.SlaveHandShake;
import ru.nsu.romanenko.Protocol.Task;
import ru.nsu.romanenko.Solution.Solution;

import java.io.*;
import java.net.Socket;

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
            } catch (IOException | ClassNotFoundException ex) {
                if (Thread.currentThread().isInterrupted()) break;
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

    private void runSession(Socket socket) throws IOException, ClassNotFoundException {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        out.writeObject(new SlaveHandShake(slaveID));

        while (true) {
            Task task = (Task) in.readObject();
            Result result = new Result(Solution.consistently(task.numbers()), task.taskID());
            out.writeObject(result);
            out.reset();
        }
    }
}
