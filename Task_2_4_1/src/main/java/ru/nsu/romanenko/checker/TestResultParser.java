package ru.nsu.romanenko.checker;

import ru.nsu.romanenko.model.TaskResult;

import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Logger;
import java.util.regex.*;

public class TestResultParser {

    private static final Logger log = Logger.getLogger(TestResultParser.class.getName());

    public void parse(Path taskDir, BuildTool tool, ProcessResult pr, TaskResult result) {
        Path reportsDir = reportsDir(taskDir, tool);

        int[] counts = fromXmlReports(reportsDir);
        if (counts[0] + counts[1] + counts[2] == 0) {
            counts = fromOutputSummary(pr.stdout() + pr.stderr());
        }

        result.setTestsPassed(counts[0]);
        result.setTestsFailed(counts[1]);
        result.setTestsSkipped(counts[2]);
    }

    private Path reportsDir(Path taskDir, BuildTool tool) {
        return switch (tool) {
            case GRADLE -> taskDir.resolve("build/test-results/test");
            case MAVEN  -> taskDir.resolve("target/surefire-reports");
        };
    }

    private int[] fromXmlReports(Path reportsDir) {
        int passed = 0, failed = 0, skipped = 0;
        if (!Files.isDirectory(reportsDir)) return new int[]{0, 0, 0};
        try (var stream = Files.list(reportsDir)) {
            for (Path xml : stream.filter(p -> p.toString().endsWith(".xml")).toList()) {
                int[] c = parseXml(xml);
                passed  += c[0];
                failed  += c[1];
                skipped += c[2];
            }
        } catch (IOException e) {
            log.warning("Could not read test reports: " + e.getMessage());
        }
        return new int[]{passed, failed, skipped};
    }

    private int[] parseXml(Path xmlFile) {
        try {
            String content = Files.readString(xmlFile);
            int tests    = attr(content, "tests");
            int failures = attr(content, "failures");
            int errors   = attr(content, "errors");
            int skipped  = attr(content, "skipped");
            return new int[]{Math.max(0, tests - failures - errors - skipped), failures + errors, skipped};
        } catch (Exception e) {
            return new int[]{0, 0, 0};
        }
    }

    private int attr(String xml, String name) {
        Matcher m = Pattern.compile(name + "=\"(\\d+)\"").matcher(xml);
        return m.find() ? Integer.parseInt(m.group(1)) : 0;
    }

    private int[] fromOutputSummary(String output) {
        Matcher m = Pattern.compile(
            "(\\d+) tests? completed(?:, (\\d+) failed)?(?:, (\\d+) skipped)?").matcher(output);
        if (!m.find()) return new int[]{0, 0, 0};
        int total   = Integer.parseInt(m.group(1));
        int failed  = m.group(2) != null ? Integer.parseInt(m.group(2)) : 0;
        int skipped = m.group(3) != null ? Integer.parseInt(m.group(3)) : 0;
        return new int[]{total - failed - skipped, failed, skipped};
    }
}
