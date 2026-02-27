package ru.nsu.romanenko.Simulation.Workers;

import ru.nsu.romanenko.Abstractions.Logger;
import ru.nsu.romanenko.Abstractions.Storage;
import ru.nsu.romanenko.Abstractions.Worker;
import ru.nsu.romanenko.Simulation.Models.Order;

import java.util.ArrayList;
import java.util.List;

public class Courier implements Worker, Runnable{
    private final int capacity;
    private final Storage<Order> warehouse;
    private final List<Order> orders;
    private final Logger logger;

    private boolean isClosing = false;

    public Courier(int capacity, Storage<Order> warehouse,
                   Logger logger) {
        this.capacity = capacity;
        this.warehouse = warehouse;
        this.logger = logger;
        this.orders = new ArrayList<>();
    }

    @Override
    public void work() throws InterruptedException {
        while(!isClosing) {
            while(orders.size() < capacity) {
                Order order = warehouse.get();
                if(order == null) {
                    isClosing = true;
                    break;
                }

                orders.add(order);
            }

            orders.forEach((order) -> logger.log(order.id(), "In back"));
            Thread.sleep(10_000);
            orders.forEach((order) -> logger.log(order.id(), "Delivered"));
            orders.clear();
        }
    }

    @Override
    public void run() {
        try {
            work();
        } catch (InterruptedException ex) {
            orders.forEach((order) -> logger.log(order.id(), "Delivering interrupt"));
        } finally {
            logger.systemLog("Courier done");
        }
    }
}
