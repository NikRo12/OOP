package ru.nsu.romanenko.Simulation.Storage;

import ru.nsu.romanenko.Abstractions.BlockingQueue;
import ru.nsu.romanenko.Simulation.Models.Order;

public class Warehouse extends BlockingQueue<Order> {
    public Warehouse(int capacity) {
        super(capacity);
    }
}
