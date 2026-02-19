package ru.nsu.romanenko.Client;

import ru.nsu.romanenko.Protocol.*;
import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            int[] data = new int[100];
            for(int i=0; i<100; i++) data[i] = 13;
            data[50] = 4;

            System.out.println("Sending request to Master...");
            out.writeObject(new ClientHandShake(data));

            Result response = (Result) in.readObject();
            System.out.println("Final Result (Has non-prime?): " + response.foundNotPrime());

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}