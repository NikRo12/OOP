package ru.nsu.romanenko.Master;

import ru.nsu.romanenko.Protocol.ClientHandShake;
import ru.nsu.romanenko.Protocol.SlaveHandShake;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Master {
    private final int port;
    private final SessionManager sessionManager = new SessionManager();
    private final ExecutorService pool = Executors.newCachedThreadPool();

    public Master(int port) {
        this.port = port;
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Master started on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                pool.submit(() -> handleConnection(socket));
            }
        } catch (IOException ex) {
            System.err.println("Server error: " + ex.getMessage());
        }
    }

    private void handleConnection(Socket socket) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Object handshake = in.readObject();

            if (handshake instanceof SlaveHandShake sh) {
                System.out.println("Slave connected, ID: " + sh.slaveID());
                new SlaveHandler(socket, in, out, sh.slaveID(), sessionManager).run();
            } else if (handshake instanceof ClientHandShake ch) {
                System.out.println("Client connected");
                new ClientHandler(socket, out, ch, sessionManager).run();
            } else {
                socket.close();
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Connection error: " + ex.getMessage());
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
}
