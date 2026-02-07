package ru.nsu.romanenko.Slave;

public class Main {
    public static void main(String[] args) {
        Slave slave = new Slave("localhost", 8080, 1);
        slave.startSlave();
    }
}
