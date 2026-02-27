package ru.nsu.romanenko.Simulation.Workers;

import ru.nsu.romanenko.Abstractions.Logger;
import ru.nsu.romanenko.Abstractions.Storage;
import ru.nsu.romanenko.Abstractions.Worker;
import ru.nsu.romanenko.Simulation.Models.Order;

public class Baker implements Worker, Runnable{
    private final int speed;
    private final Storage<Order> orderQueue;
    private final Storage<Order> warehouse;
    private final Logger logger;

    private Order currOrder = null;

    public Baker(int speed, Storage<Order> orderQueue,
                 Storage<Order> warehouse, Logger logger) {
        this.speed = speed;
        this.orderQueue = orderQueue;
        this.warehouse = warehouse;
        this.logger = logger;
    }

    @Override
    public void work() throws InterruptedException{
        while((currOrder = orderQueue.get()) != null) {
            logger.log(currOrder.id(), "Start cooking");
            Thread.sleep(speed);
            logger.log(currOrder.id(), "Cooked");
            warehouse.put(currOrder);
            logger.log(currOrder.id(), "Placed in warehouse");
        }
    }

    @Override
    public void run() {
        try {
            work();
        } catch (InterruptedException ex) {
            logger.log(currOrder.id(), "Cooking interrupted");
        } finally {
            logger.systemLog("Baker done");
        }
    }
}
