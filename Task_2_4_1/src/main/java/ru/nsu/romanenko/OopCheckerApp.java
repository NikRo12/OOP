package ru.nsu.romanenko;

import ru.nsu.romanenko.checker.CheckRunner;
import ru.nsu.romanenko.checker.GitManager;
import ru.nsu.romanenko.checker.ProcessExecutor;
import ru.nsu.romanenko.dsl.ConfigLoader;
import ru.nsu.romanenko.dsl.OopCheckerConfig;
import ru.nsu.romanenko.model.StudentResult;
import ru.nsu.romanenko.report.HtmlReporter;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Logger;

public class OopCheckerApp {

    private static final Logger log = Logger.getLogger(OopCheckerApp.class.getName());

    public void run(String[] args) {
        File configFile = resolveConfigFile(args);
        OopCheckerConfig config = loadConfig(configFile);
        warnIfGitUnavailable();
        Map<String, StudentResult> results = runChecks(config);
        generateReport(config, results);
    }

    private File resolveConfigFile(String[] args) {
        File f = args.length > 0
            ? new File(args[0])
            : new File(System.getProperty("user.dir"), ConfigLoader.DEFAULT_SCRIPT_NAME);

        if (!f.exists()) {
            System.err.println("ERROR: Configuration script not found: " + f.getAbsolutePath());
            System.err.println("Expected file: " + ConfigLoader.DEFAULT_SCRIPT_NAME);
            System.err.println("Usage: java -jar oop-checker.jar [config.groovy]");
            System.exit(1);
        }
        return f;
    }

    private OopCheckerConfig loadConfig(File configFile) {
        log.info("Loading configuration from: " + configFile.getAbsolutePath());
        try {
            OopCheckerConfig config = new ConfigLoader().load(configFile);
            log.info(String.format("Config loaded: %d tasks, %d groups, %d checkpoints",
                config.getTasks().size(), config.getGroups().size(), config.getCheckPoints().size()));
            return config;
        } catch (IOException e) {
            System.err.println("ERROR: Failed to load configuration: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(2);
            return null;
        }
    }

    private void warnIfGitUnavailable() {
        GitManager git = new GitManager(Paths.get(System.getProperty("user.dir"), "repos"), new ProcessExecutor());
        if (!git.isGitAvailable()) {
            System.err.println("WARNING: git does not appear to be available. Repository operations will fail.");
        }
    }

    private Map<String, StudentResult> runChecks(OopCheckerConfig config) {
        CheckRunner runner = new CheckRunner(config);
        try {
            runner.run();
        } catch (IOException e) {
            System.err.println("ERROR: Check run failed: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(3);
        }
        return runner.getResults();
    }

    private void generateReport(OopCheckerConfig config, Map<String, StudentResult> results) {
        try (PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8)) {
            new HtmlReporter(config, results).generate(out);
        }
        log.info("Done. Redirect stdout to a .html file to save the report.");
    }
}
