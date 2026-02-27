package ru.nsu.romanenko.Simulation;

import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.Abstractions.Logger;
import ru.nsu.romanenko.Simulation.Storage.OrderQueue;
import ru.nsu.romanenko.Simulation.Storage.Warehouse;
import ru.nsu.romanenko.Simulation.Models.Order;
import ru.nsu.romanenko.Simulation.Models.Pizza;
import ru.nsu.romanenko.Simulation.Workers.Baker;
import ru.nsu.romanenko.System.SimpleLogger;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class BakerTest {

    @Test
    void testBakerProcessesOrderAndExits() throws InterruptedException {
        AtomicInteger logsCount = new AtomicInteger(0);
        AtomicBoolean orderInWarehouse = new AtomicBoolean(false);

        Pizza pizza = new Pizza(true, 30, 500);
        Order order = new Order(10, pizza, "Main St 1");

        OrderQueue stubQueue = new OrderQueue(1) {
            private int count = 1;
            @Override
            public Order get() {
                if (count-- > 0) return order;
                return null;
            }
        };

        Baker baker = getBaker(orderInWarehouse, logsCount, stubQueue);
        baker.work();

        assertTrue(orderInWarehouse.get());
        assertEquals(3, logsCount.get());
    }

    private static Baker getBaker(AtomicBoolean orderInWarehouse, AtomicInteger logsCount, OrderQueue stubQueue) {
        Warehouse stubWarehouse = new Warehouse(1) {
            @Override
            public void put(Order item) {
                if (item.id() == 10) orderInWarehouse.set(true);
            }
        };

        Logger stubLogger = new Logger() {
            @Override
            public void log(int id, String state) {
                logsCount.incrementAndGet();
            }
            @Override
            public void systemLog(String message) {}
        };

        return new Baker(10, stubQueue, stubWarehouse, stubLogger);
    }

    @Test
    void testBakerHandlesInterruption() throws InterruptedException {
        AtomicBoolean interruptLogged = new AtomicBoolean(false);

        OrderQueue blockingQueue = new OrderQueue(1) {
            @Override
            public Order get() {
                return new Order(99, new Pizza(true, 20, 300), "Test Ave");
            }
        };

        Logger stubLogger = new Logger() {
            @Override
            public void log(int id, String state) {
                if (state.equals("Cooking interrupted")) interruptLogged.set(true);
            }
            @Override
            public void systemLog(String message) {}
        };

        Baker baker = new Baker(5000, blockingQueue, new Warehouse(1), stubLogger);
        Thread bakerThread = new Thread(baker);

        bakerThread.start();
        Thread.sleep(100);
        bakerThread.interrupt();
        bakerThread.join(1000);

        assertTrue(interruptLogged.get());
    }

    @Test
    void testBakerFinallyBlock() throws InterruptedException {
        AtomicBoolean systemLogCalled = new AtomicBoolean(false);

        OrderQueue emptyQueue = new OrderQueue(1) {
            @Override
            public Order get() { return null; }
        };

        Logger stubLogger = new Logger() {
            @Override
            public void log(int id, String state) {}
            @Override
            public void systemLog(String message) {
                if (message.equals("Baker done")) systemLogCalled.set(true);
            }
        };

        Baker baker = new Baker(0, emptyQueue, new Warehouse(1), stubLogger);
        baker.run();

        assertTrue(systemLogCalled.get());
    }

    @Test
    void testBakerWithRealClasses() throws InterruptedException {
        OrderQueue queue = new OrderQueue(2);
        Warehouse warehouse = new Warehouse(2);
        SimpleLogger logger = new SimpleLogger();

        Pizza pizza = new Pizza(true, 30, 500);
        Order order = new Order(1, pizza, "Street 1");

        Baker baker = new Baker(10, queue, warehouse, logger);

        queue.put(order);

        queue.close();

        baker.work();

        assertEquals(order, warehouse.get());
    }
}