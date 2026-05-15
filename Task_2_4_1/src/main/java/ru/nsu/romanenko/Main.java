package ru.nsu.romanenko;

import java.util.logging.*;

public class Main {

    public static void main(String[] args) {
        configureLogging();
        new OopCheckerApp().run(args);
    }

    private static void configureLogging() {
        Logger root = Logger.getLogger("");
        for (Handler h : root.getHandlers()) root.removeHandler(h);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            @Override
            public String format(LogRecord r) {
                return String.format("[%s] %s: %s%n",
                    r.getLevel(),
                    r.getLoggerName().replaceFirst("ru\\.nsu\\.romanenko\\.", ""),
                    r.getMessage());
            }
        });
        handler.setLevel(Level.INFO);
        root.addHandler(handler);
        root.setLevel(Level.INFO);
    }
}
