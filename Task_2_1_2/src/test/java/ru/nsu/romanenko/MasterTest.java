package ru.nsu.romanenko;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import ru.nsu.romanenko.Master.Master;
import ru.nsu.romanenko.Protocol.ClientHandShake;
import ru.nsu.romanenko.Protocol.Result;
import ru.nsu.romanenko.Protocol.SlaveHandShake;
import ru.nsu.romanenko.Slave.Slave;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class MasterTest {

    private ExecutorService serverExecutor;
    private ExecutorService slaveExecutor;
    private int port;
    private Master master;

    @BeforeEach
    void setUp() throws Exception {
        serverExecutor = Executors.newSingleThreadExecutor();
        slaveExecutor = Executors.newCachedThreadPool();
        master = new Master(0);
        serverExecutor.submit(master::startServer);
        port = master.getPort();
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        serverExecutor.shutdownNow();
        slaveExecutor.shutdownNow();
        serverExecutor.awaitTermination(2, TimeUnit.SECONDS);
        slaveExecutor.awaitTermination(2, TimeUnit.SECONDS);
    }

    @Test
    @Timeout(10)
    void testFullFlow_foundComposite() throws Exception {
        slaveExecutor.submit(() -> new Slave("localhost", port, 1).startSlave());
        slaveExecutor.submit(() -> new Slave("localhost", port, 2).startSlave());

        try (Socket socket = new Socket("localhost", port)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            int[] data = new int[100];
            for (int i = 0; i < 100; i++) data[i] = 3;
            data[55] = 4;
            out.writeObject(new ClientHandShake(data));

            Result response = (Result) in.readObject();
            assertTrue(response.foundNotPrime());
        }
    }

    @Test
    @Timeout(10)
    void testFullFlow_allPrimes() throws Exception {
        slaveExecutor.submit(() -> new Slave("localhost", port, 1).startSlave());

        try (Socket socket = new Socket("localhost", port)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(new ClientHandShake(new int[]{3, 5, 7, 11, 13}));

            Result response = (Result) in.readObject();
            assertFalse(response.foundNotPrime());
        }
    }

    @Test
    @Timeout(10)
    void testEdgeCase_emptyArray() throws Exception {
        slaveExecutor.submit(() -> new Slave("localhost", port, 1).startSlave());

        try (Socket socket = new Socket("localhost", port)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(new ClientHandShake(new int[]{}));

            Result response = (Result) in.readObject();
            assertFalse(response.foundNotPrime());
        }
    }

    @Test
    @Timeout(10)
    void testEdgeCase_singleNonPrime() throws Exception {
        slaveExecutor.submit(() -> new Slave("localhost", port, 1).startSlave());

        try (Socket socket = new Socket("localhost", port)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(new ClientHandShake(new int[]{4}));

            Result response = (Result) in.readObject();
            assertTrue(response.foundNotPrime());
        }
    }

    @Test
    @Timeout(15)
    void testFaultTolerance_slaveDropsTask() throws Exception {
        CountDownLatch taskTaken = new CountDownLatch(1);
        slaveExecutor.submit(() -> {
            try (Socket socket = new Socket("localhost", port)) {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                out.writeObject(new SlaveHandShake(999));
                in.readObject();
                taskTaken.countDown();
            } catch (Exception ignored) {}
        });

        slaveExecutor.submit(() -> new Slave("localhost", port, 2).startSlave());

        ExecutorService clientExecutor = Executors.newSingleThreadExecutor();
        try {
            Future<Result> clientFuture = clientExecutor.submit(() -> {
                try (Socket socket = new Socket("localhost", port)) {
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.flush();
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    out.writeObject(new ClientHandShake(new int[]{4, 3, 5}));
                    return (Result) in.readObject();
                }
            });

            assertTrue(taskTaken.await(5, TimeUnit.SECONDS), "Фейковый slave должен был взять задачу");
            Result result = clientFuture.get(10, TimeUnit.SECONDS);
            assertTrue(result.foundNotPrime());
        } finally {
            clientExecutor.shutdownNow();
        }
    }
}
