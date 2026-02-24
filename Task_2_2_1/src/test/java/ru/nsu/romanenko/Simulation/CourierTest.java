package ru.nsu.romanenko.Simulation;

import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.Abstractions.Logger;
import ru.nsu.romanenko.Simulation.Storage.Warehouse;
import ru.nsu.romanenko.Simulation.Structures.Order;
import ru.nsu.romanenko.Simulation.Structures.Pizza;
import ru.nsu.romanenko.Simulation.Workers.Courier;

import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.*;

class CourierTest {
    @Test
    void testCourierExitOnNullFromWarehouse() throws InterruptedException {
        AtomicBoolean doneLogged = new AtomicBoolean(false);

        Warehouse emptyWarehouse = new Warehouse(1) {
            @Override
            public Order get() { return null; }
        };

        Logger stubLogger = new Logger() {
            @Override
            public void log(int id, String state) {}
            @Override
            public void systemLog(String message) {
                if (message.equals("Courier done")) doneLogged.set(true);
            }
        };

        Courier courier = new Courier(2, emptyWarehouse, stubLogger);
        courier.run();

        assertTrue(doneLogged.get());
    }

    @Test
    void testCourierInterruptionLogging() throws InterruptedException {
        AtomicBoolean interruptLogged = new AtomicBoolean(false);
        Pizza pizza = new Pizza(true, 20, 200);

        Warehouse blockingWarehouse = new Warehouse(1) {
            @Override
            public Order get() {
                return new Order(777, pizza, "Street");
            }
        };

        Logger stubLogger = new Logger() {
            @Override
            public void log(int id, String state) {
                if (state.equals("Delivering interrupt")) interruptLogged.set(true);
            }
            @Override
            public void systemLog(String message) {}
        };

        Courier courier = new Courier(1, blockingWarehouse, stubLogger);
        Thread t = new Thread(courier);
        t.start();

        Thread.sleep(100);
        t.interrupt();
        t.join(1000);

        assertTrue(interruptLogged.get());
    }
}