package ru.nsu.romanenko;

import ru.nsu.romanenko.Simulation.Pizzeria;
import ru.nsu.romanenko.System.Config;
import ru.nsu.romanenko.System.JsonReader;
import ru.nsu.romanenko.System.SimpleLogger;

public class Simulate {
    public static void main(String[] args) throws InterruptedException {
        SimpleLogger logger = new SimpleLogger();
        Config config = JsonReader.read();
        Pizzeria pizzeria = new Pizzeria(config.bakersCount(), config.bakersSpeed(),
                config.couriersCount(), config.couriersCapacity(), config.warehouseCapacity(), logger);

        pizzeria.open();
        Thread.sleep(60_000);
        pizzeria.close();
    }
}
