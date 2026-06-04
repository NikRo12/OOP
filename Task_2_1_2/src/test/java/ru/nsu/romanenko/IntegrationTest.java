package ru.nsu.romanenko;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import ru.nsu.romanenko.Master.Master;
import ru.nsu.romanenko.Protocol.ClientHandShake;
import ru.nsu.romanenko.Protocol.Result;
import ru.nsu.romanenko.Slave.Slave;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTest {

    private static final int SLAVE_COUNT = 3;

    private ExecutorService masterExecutor;
    private ExecutorService slaveExecutor;
    private Master master;
    private int port;

    @BeforeEach
    void setUp() throws Exception {
        masterExecutor = Executors.newSingleThreadExecutor();
        slaveExecutor = Executors.newFixedThreadPool(SLAVE_COUNT);

        master = new Master(0);
        masterExecutor.submit(master::startServer);
        port = master.getPort();

        for (int i = 1; i <= SLAVE_COUNT; i++) {
            final int id = i;
            slaveExecutor.submit(() -> new Slave("localhost", port, id).startSlave());
        }
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        masterExecutor.shutdownNow();
        slaveExecutor.shutdownNow();
        masterExecutor.awaitTermination(2, TimeUnit.SECONDS);
        slaveExecutor.awaitTermination(2, TimeUnit.SECONDS);
    }

    private Result sendAndReceive(int[] data) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket("localhost", port)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(new ClientHandShake(data));
            return (Result) in.readObject();
        }
    }

    @Test
    @Timeout(15)
    void integration_containsNonPrime() throws Exception {
        int[] input = {6, 8, 7, 13, 5, 9, 4};
        Result result = sendAndReceive(input);
        assertTrue(result.foundNotPrime());
    }

    @Test
    @Timeout(15)
    void integration_allPrimes() throws Exception {
        int[] input = {
            20319251, 6997901, 6997927, 6997937, 17858849,
            6997967, 6998009, 6998029, 6998039, 20165149,
            6998051, 6998053
        };
        Result result = sendAndReceive(input);
        assertFalse(result.foundNotPrime());
    }

    @Test
    @Timeout(15)
    void integration_singleNonPrime_earlyStop() throws Exception {
        int[] input = new int[300];
        for (int i = 0; i < 300; i++) input[i] = 999983;
        input[0] = 4;
        Result result = sendAndReceive(input);
        assertTrue(result.foundNotPrime());
    }

    @Test
    @Timeout(15)
    void integration_nonPrimeAtEnd_allSlavesMustWork() throws Exception {
        int[] input = new int[300];
        for (int i = 0; i < 300; i++) input[i] = 999983;
        input[299] = 4;
        Result result = sendAndReceive(input);
        assertTrue(result.foundNotPrime());
    }

    @Test
    @Timeout(15)
    void integration_emptyArray() throws Exception {
        Result result = sendAndReceive(new int[]{});
        assertFalse(result.foundNotPrime());
    }
}
