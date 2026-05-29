package ru.nsu.romanenko;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import ru.nsu.romanenko.Protocol.Result;
import ru.nsu.romanenko.Protocol.SlaveHandShake;
import ru.nsu.romanenko.Protocol.Task;
import ru.nsu.romanenko.Slave.Slave;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class SlaveTest {

    @Test
    @Timeout(10)
    void testSlave_foundNotPrime() throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        try (ServerSocket fakeMaster = new ServerSocket(0)) {
            int port = fakeMaster.getLocalPort();
            executor.submit(() -> new Slave("localhost", port, 101).startSlave());

            try (Socket conn = fakeMaster.accept()) {
                ObjectOutputStream out = new ObjectOutputStream(conn.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(conn.getInputStream());

                Object handshake = in.readObject();
                assertTrue(handshake instanceof SlaveHandShake);
                assertEquals(101, ((SlaveHandShake) handshake).slaveID());

                out.writeObject(new Task(new int[]{3, 4, 5}, 1));

                Result result = (Result) in.readObject();
                assertEquals(1, result.taskID());
                assertTrue(result.foundNotPrime());
            }
        } finally {
            executor.shutdownNow();
            executor.awaitTermination(2, TimeUnit.SECONDS);
        }
    }

    @Test
    @Timeout(10)
    void testSlave_allPrimes() throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        try (ServerSocket fakeMaster = new ServerSocket(0)) {
            int port = fakeMaster.getLocalPort();
            executor.submit(() -> new Slave("localhost", port, 102).startSlave());

            try (Socket conn = fakeMaster.accept()) {
                ObjectOutputStream out = new ObjectOutputStream(conn.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(conn.getInputStream());

                in.readObject();

                out.writeObject(new Task(new int[]{3, 5, 7, 11, 13}, 2));

                Result result = (Result) in.readObject();
                assertEquals(2, result.taskID());
                assertFalse(result.foundNotPrime());
            }
        } finally {
            executor.shutdownNow();
            executor.awaitTermination(2, TimeUnit.SECONDS);
        }
    }

    @Test
    @Timeout(15)
    void testSlave_reconnectsAfterMasterDisconnect() throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        try (ServerSocket fakeMaster = new ServerSocket(0)) {
            int port = fakeMaster.getLocalPort();
            executor.submit(() -> new Slave("localhost", port, 103).startSlave());

            try (Socket first = fakeMaster.accept()) {
                ObjectOutputStream out = new ObjectOutputStream(first.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(first.getInputStream());
                in.readObject();
            }

            try (Socket second = fakeMaster.accept()) {
                ObjectOutputStream out = new ObjectOutputStream(second.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(second.getInputStream());

                Object handshake = in.readObject();
                assertTrue(handshake instanceof SlaveHandShake);
                assertEquals(103, ((SlaveHandShake) handshake).slaveID());

                out.writeObject(new Task(new int[]{7, 11}, 3));
                Result result = (Result) in.readObject();
                assertFalse(result.foundNotPrime());
            }
        } finally {
            executor.shutdownNow();
            executor.awaitTermination(2, TimeUnit.SECONDS);
        }
    }
}
