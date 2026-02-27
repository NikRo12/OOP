package ru.nsu.romanenko.Simulation;

import ru.nsu.romanenko.Abstractions.Logger;
import ru.nsu.romanenko.Abstractions.Worker;
import ru.nsu.romanenko.Simulation.Storage.OrderQueue;
import ru.nsu.romanenko.Simulation.Storage.Warehouse;
import ru.nsu.romanenko.Simulation.Models.Order;
import ru.nsu.romanenko.Simulation.Models.Pizza;
import ru.nsu.romanenko.Simulation.Workers.Baker;
import ru.nsu.romanenko.Simulation.Workers.Courier;

import java.util.ArrayList;
import java.util.List;

public class Pizzeria {
    private final OrderQueue orderQueue;
    private final Warehouse warehouse;
    private final List<Worker> bakers;
    private final List<Worker> couriers;

    private final List<Thread> bakersThreads;
    private final List<Thread> couriersThreads;

    private final int backersCount;
    private final int backersSpeed;
    private final int couriersCount;
    private final int couriersCapacity;

    private final Logger logger;

    public Pizzeria(int N, int bakersSpeed, int M, int couriersCapacity,
                    int T, Logger logger) {
        this.orderQueue = new OrderQueue(20);
        this.warehouse = new Warehouse(T);
        this.bakers = new ArrayList<>();
        this.couriers = new ArrayList<>();
        this.bakersThreads = new ArrayList<>();
        this.couriersThreads = new ArrayList<>();

        this.backersCount = N;
        this.backersSpeed = bakersSpeed;
        this.couriersCount = M;
        this.couriersCapacity = couriersCapacity;

        this.logger = logger;
    }

    public void open() {
        hire();

        bakers.forEach((worker -> bakersThreads.add(new Thread((Runnable) worker))));
        couriers.forEach((worker -> couriersThreads.add(new Thread((Runnable) worker))));

        bakersThreads.forEach((Thread::start));
        couriersThreads.forEach((Thread::start));

        for (int i = 1; i <= 15; i++) {
            try {
                orderQueue.put(new Order(i, new Pizza(true, 16, 799),
                        "Novosibirsk"));
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                logger.systemLog(ex.getMessage());
            }
        }
    }

    public void close() {
        try {
            orderQueue.close();

            for (Thread bakerThread : bakersThreads) {
                bakerThread.join();
            }

            warehouse.close();

            for (Thread courierThread : couriersThreads) {
                courierThread.join();
            }
        } catch (InterruptedException ex) {
            logger.systemLog(ex.getMessage());
        } finally {
            logger.systemLog("Pizzeria graceful closed");
        }
    }

    private void hire() {
        for (int i = 0; i < backersCount; i++) {
            Baker baker = new Baker(backersSpeed, orderQueue, warehouse, logger);
            bakers.add(baker);
        }

        for (int i = 0; i < couriersCount; i++) {
            Courier courier = new Courier(couriersCapacity, warehouse, logger);
            couriers.add(courier);
        }
    }
}
