package ru.nsu.romanenko.Slave;

import java.io.*;
import java.net.*;

import ru.nsu.romanenko.Protocol.Result;
import ru.nsu.romanenko.Protocol.SlaveHandShake;
import ru.nsu.romanenko.Protocol.Task;
import ru.nsu.romanenko.Solution.Solution;

public class Slave {
    private final String masterHost;
    private final int masterPort;
    private final int slaveID;

    public Slave(String masterHost, int masterPort, int slaveID) {
        this.masterHost = masterHost;
        this.masterPort = masterPort;
        this.slaveID = slaveID;
    }

    public void startSlave() {
        try (Socket masterSocket = new Socket(masterHost, masterPort)) {
            System.out.println("Connection with master completed");

            ObjectOutputStream outputStream = new ObjectOutputStream(masterSocket.getOutputStream());
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(masterSocket.getInputStream());
            outputStream.writeObject(new SlaveHandShake(slaveID));

            while (true) {
                Task task = (Task) inputStream.readObject();
                Result result = new Result(Solution.consistently(task.numbers()), task.taskID());
                outputStream.writeObject(result);
            }

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
