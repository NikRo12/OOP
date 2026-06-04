package ru.nsu.romanenko.Slave;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int id = new Random().nextInt(9000) + 1000;
        Slave slave = new Slave("localhost", 8080, id);
        slave.startSlave();
    }
}
