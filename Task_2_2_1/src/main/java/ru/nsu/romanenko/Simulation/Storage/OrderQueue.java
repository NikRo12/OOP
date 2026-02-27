package ru.nsu.romanenko.Simulation.Storage;

import ru.nsu.romanenko.Abstractions.BlockingQueue;
import ru.nsu.romanenko.Simulation.Models.Order;

public class OrderQueue extends BlockingQueue<Order> {
    public OrderQueue(int capacity) {
        super(capacity);
    }
}
