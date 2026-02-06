package ru.nsu.romanenko;

import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.Protocol.*;
import ru.nsu.romanenko.Slave.Slave;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

class SlaveTest {

    @Test
    void testSlaveConnectionAndLogic() throws Exception {
        try (ServerSocket fakeMasterServer = new ServerSocket(0)) {
            int port = fakeMasterServer.getLocalPort();
            ExecutorService executor = Executors.newSingleThreadExecutor();

            Future<?> slaveFuture = executor.submit(() -> {
                Slave slave = new Slave("localhost", port, 101);
                slave.startSlave();
            });

            Socket slaveSocket = fakeMasterServer.accept();
            ObjectOutputStream out = new ObjectOutputStream(slaveSocket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(slaveSocket.getInputStream());

            Object handshake = in.readObject();
            assertTrue(handshake instanceof SlaveHandShake);
            assertEquals(101, ((SlaveHandShake) handshake).slaveID());


            int[] testData = {3, 4, 5};
            Task task = new Task(testData, 1);
            out.writeObject(task);

            Object response = in.readObject();
            assertTrue(response instanceof Result);
            Result result = (Result) response;

            assertEquals(1, result.taskID());
            assertTrue(result.foundNotPrime(), "Слейв должен был найти составное число 4");

            slaveSocket.close();
            slaveFuture.cancel(true);
            executor.shutdownNow();
        }
    }
}