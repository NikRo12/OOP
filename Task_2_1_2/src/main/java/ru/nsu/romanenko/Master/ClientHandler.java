package ru.nsu.romanenko.Master;

import ru.nsu.romanenko.Protocol.ClientHandShake;
import ru.nsu.romanenko.Protocol.Result;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ClientHandShake handshake;
    private final SessionManager sessionManager;

    public ClientHandler(Socket socket, ObjectOutputStream out,
                         ClientHandShake handshake, SessionManager sessionManager) {
        this.socket = socket;
        this.out = out;
        this.handshake = handshake;
        this.sessionManager = sessionManager;
    }

    @Override
    public void run() {
        try {
            CompletableFuture<Boolean> future = sessionManager.startSession(handshake.data());
            boolean result = future.get();
            out.writeObject(new Result(result, -1));
            System.out.println("Job finished. Result sent: " + result);
        } catch (InterruptedException | ExecutionException | IOException e) {
            System.err.println("Client handler error: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
}
