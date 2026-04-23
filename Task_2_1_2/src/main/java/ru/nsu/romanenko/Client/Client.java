package ru.nsu.romanenko.Client;

import ru.nsu.romanenko.Protocol.ClientHandShake;
import ru.nsu.romanenko.Protocol.Result;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Small range [2..101] so numbers repeat across tasks — demonstrates cache hits
            Random random = new Random();
            int[] data = new int[500];
            for (int i = 0; i < data.length; i++) {
                data[i] = random.nextInt(100) + 2;
            }
            // Guarantee at least one composite number
            data[random.nextInt(data.length)] = 4;

            System.out.println("Sending " + data.length + " random numbers to Master...");
            out.writeObject(new ClientHandShake(data));

            Result response = (Result) in.readObject();
            System.out.println("Final Result (Has non-prime?): " + response.foundNotPrime());

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
