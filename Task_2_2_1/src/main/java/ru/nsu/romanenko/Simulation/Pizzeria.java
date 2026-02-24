package ru.nsu.romanenko.Simulation;

import ru.nsu.romanenko.Abstractions.Logger;
import ru.nsu.romanenko.Abstractions.Worker;
import ru.nsu.romanenko.Simulation.Storage.OrderQueue;
import ru.nsu.romanenko.Simulation.Storage.Warehouse;
import ru.nsu.romanenko.Simulation.Structures.Order;
import ru.nsu.romanenko.Simulation.Structures.Pizza;
import ru.nsu.romanenko.Simulation.Workers.Baker;
import ru.nsu.romanenko.Simulation.Workers.Courier;

import java.util.ArrayList;

public class Pizzeria {
    private final OrderQueue orderQueue;
    private final Warehouse warehouse;
    private final ArrayList<Worker> bakers;
    private final ArrayList<Worker> couriers;

    private final ArrayList<Thread> bakersThreads;
    private final ArrayList<Thread> couriersThreads;

    private final int backerCount;
    private final int courierCount;

    private final Logger logger;

    public Pizzeria(int N, int M, int T, Logger logger) {
        this.orderQueue = new OrderQueue(20);
        this.warehouse = new Warehouse(T);
        this.bakers = new ArrayList<>();
        this.couriers = new ArrayList<>();
        this.bakersThreads = new ArrayList<>();
        this.couriersThreads = new ArrayList<>();

        this.backerCount = N;
        this.courierCount = M;

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
                orderQueue.put(new Order(i, new Pizza(true, 16, 799), "Novosibirsk"));
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
        for (int i = 0; i < backerCount; i++) {
            Baker baker = new Baker(3000, orderQueue, warehouse, logger);
            bakers.add(baker);
        }

        for (int i = 0; i < courierCount; i++) {
            Courier courier = new Courier(2, warehouse, logger);
            couriers.add(courier);
        }
    }
}
