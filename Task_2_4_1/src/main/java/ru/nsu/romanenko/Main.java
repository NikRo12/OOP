package ru.nsu.romanenko;

import ru.nsu.romanenko.checker.CheckRunner;
import ru.nsu.romanenko.checker.GitManager;
import ru.nsu.romanenko.dsl.ConfigLoader;
import ru.nsu.romanenko.dsl.OopCheckerConfig;
import ru.nsu.romanenko.report.HtmlReporter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.*;

public class Main {

    public static void main(String[] args) {
        configureLogging();

        Logger log = Logger.getLogger(Main.class.getName());
        File configFile;
        if (args.length > 0) {
            configFile = new File(args[0]);
        } else {
            configFile = new File(System.getProperty("user.dir"),
                ConfigLoader.DEFAULT_SCRIPT_NAME);
        }

        if (!configFile.exists()) {
            System.err.println("ERROR: Configuration script not found: "
                + configFile.getAbsolutePath());
            System.err.println("Expected file: " + ConfigLoader.DEFAULT_SCRIPT_NAME);
            System.err.println("Usage: java -jar oop-checker.jar [config.groovy]");
            System.exit(1);
        }

        log.info("Loading configuration from: " + configFile.getAbsolutePath());

        OopCheckerConfig config;
        try {
            ConfigLoader loader = new ConfigLoader();
            config = loader.load(configFile);
        } catch (IOException e) {
            System.err.println("ERROR: Failed to load configuration: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(2);
            return;
        }

        GitManager gitMgr = new GitManager(
            Paths.get(System.getProperty("user.dir"), "repos"));
        if (!gitMgr.isGitAvailable()) {
            System.err.println("WARNING: git does not appear to be available. "
                + "Repository operations will fail.");
        }

        log.info(String.format("Config loaded: %d tasks, %d groups, %d checkpoints",
            config.getTasks().size(),
            config.getGroups().size(),
            config.getCheckPoints().size()));

        CheckRunner runner = new CheckRunner(config);
        try {
            runner.run();
        } catch (IOException e) {
            System.err.println("ERROR: Check run failed: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(3);
        }

        HtmlReporter reporter = new HtmlReporter(config, runner.getResults());
        try (java.io.PrintStream utf8Out =
                new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8)) {
            reporter.generate(utf8Out);
        }

        log.info("Done. Redirect stdout to a .html file to save the report.");
    }

    private static void configureLogging() {
        Logger root = Logger.getLogger("");
        for (Handler h : root.getHandlers()) root.removeHandler(h);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            @Override
            public String format(LogRecord r) {
                return String.format("[%s] %s: %s%n",
                    r.getLevel(), r.getLoggerName()
                        .replaceFirst("ru\\.nsu\\.oopchecker\\.", ""),
                    r.getMessage());
            }
        });
        handler.setLevel(Level.INFO);
        root.addHandler(handler);
        root.setLevel(Level.INFO);
    }
}
