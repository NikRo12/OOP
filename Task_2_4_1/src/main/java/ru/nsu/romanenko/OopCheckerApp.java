package ru.nsu.romanenko;

import ru.nsu.romanenko.checker.CheckRunner;
import ru.nsu.romanenko.dsl.ConfigLoader;
import ru.nsu.romanenko.dsl.OopCheckerConfig;
import ru.nsu.romanenko.model.StudentResult;
import ru.nsu.romanenko.report.HtmlReporter;
import ru.nsu.romanenko.report.ReportModelBuilder;
import ru.nsu.romanenko.report.ReportViewModel;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Logger;

public class OopCheckerApp {

    private static final Logger log = Logger.getLogger(OopCheckerApp.class.getName());

    private Map<String, StudentResult> lastResults;

    public void run(String[] args) {
        File configFile = resolveConfigFile(args);
        try (PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8)) {
            execute(configFile, out);
        } catch (IOException e) {
            System.err.println("ERROR: Check run failed: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(3);
        }
        log.info("Done. Redirect stdout to a .html file to save the report.");
    }

    public void execute(File configFile, PrintStream out) throws IOException {
        OopCheckerConfig config = new ConfigLoader().load(configFile);
        log.info(String.format("Config loaded: %d tasks, %d groups, %d checkpoints",
            config.getTasks().size(), config.getGroups().size(), config.getCheckPoints().size()));

        CheckRunner runner = new CheckRunner(config);
        runner.run();
        this.lastResults = runner.getResults();

        ReportViewModel model = new ReportModelBuilder().build(config, lastResults);
        new HtmlReporter(model).generate(out);
    }

    public Map<String, StudentResult> getLastResults() {
        return lastResults;
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
}
