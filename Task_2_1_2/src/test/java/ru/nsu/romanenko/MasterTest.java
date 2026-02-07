package ru.nsu.romanenko;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.Master.Master;
import ru.nsu.romanenko.Protocol.*;
import ru.nsu.romanenko.Slave.Slave;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class MasterTest {

    private ExecutorService serverExecutor;
    private ExecutorService slaveExecutor;
    private int port;

    @BeforeEach
    void setUp() {
        serverExecutor = Executors.newSingleThreadExecutor();
        slaveExecutor = Executors.newCachedThreadPool();
        port = 8099;
    }

    @AfterEach
    void tearDown() {
        serverExecutor.shutdownNow();
        slaveExecutor.shutdownNow();
    }

    @Test
    void testFullFlow_FoundComposite() throws Exception {
        serverExecutor.submit(() -> new Master(port).startServer());
        Thread.sleep(500);

        slaveExecutor.submit(() -> new Slave("localhost", port, 1).startSlave());
        slaveExecutor.submit(() -> new Slave("localhost", port, 2).startSlave());
        Thread.sleep(500);

        try (Socket clientSocket = new Socket("localhost", port)) {
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            int[] data = new int[100];
            for (int i = 0; i < 100; i++) data[i] = 3;
            data[55] = 4;
            out.writeObject(new ClientHandShake(data));

            Object response = in.readObject();
            assertTrue(response instanceof Result);
            assertTrue(((Result) response).foundNotPrime());
        }
    }

    @Test
    void testFullFlow_AllPrimes() throws Exception {
        serverExecutor.submit(() -> new Master(port).startServer());
        Thread.sleep(500);

        slaveExecutor.submit(() -> new Slave("localhost", port, 1).startSlave());
        Thread.sleep(500);

        try (Socket clientSocket = new Socket("localhost", port)) {
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            int[] data = {3, 5, 7, 11, 13};

            out.writeObject(new ClientHandShake(data));

            Object response = in.readObject();
            assertTrue(response instanceof Result);
            assertFalse(((Result) response).foundNotPrime());
        }
    }
}