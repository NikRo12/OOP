package ru.nsu.romanenko.Slave;

import java.io.*;
import java.net.*;
import ru.nsu.romanenko.Protocol.Task;

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
            masterSocket.getOutputStream().write(slaveID);
            masterSocket.getOutputStream().flush();

            int responseByte = masterSocket.getInputStream().read();

            if (responseByte == -1) {
                System.err.println("Мастер закрыл соединение");
            }

            int response = (byte) responseByte;

            if (response >= 0) {
                System.out.println("Получен положительный ответ: " + response);

                ObjectOutputStream outputStream = new ObjectOutputStream(masterSocket.getOutputStream());
                outputStream.flush();
                ObjectInputStream inputStream = new ObjectInputStream(masterSocket.getInputStream());

                while (true){
                    try {
                        Task task = (Task) inputStream.readObject();

                    } catch (ClassNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                }

            } else {
                System.err.println("Получен отрицательный ответ: " + response);
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
