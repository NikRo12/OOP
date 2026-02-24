package ru.nsu.romanenko.Simulation.Storage;

import ru.nsu.romanenko.Abstractions.BlockingQueue;
import ru.nsu.romanenko.Simulation.Structures.Order;

public class Warehouse extends BlockingQueue<Order> {
    public Warehouse(int capacity) {
        super(capacity);
    }

    public void close() {
        super.signalAll();
    }
}
