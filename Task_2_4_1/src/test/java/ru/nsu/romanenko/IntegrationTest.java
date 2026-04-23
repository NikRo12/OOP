package ru.nsu.romanenko;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.romanenko.checker.CheckRunner;
import ru.nsu.romanenko.dsl.ConfigLoader;
import ru.nsu.romanenko.dsl.OopCheckerConfig;
import ru.nsu.romanenko.model.StudentResult;
import ru.nsu.romanenko.report.HtmlReporter;

import java.io.*;
import java.nio.file.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTest {

    @TempDir
    Path tempDir;

    @Test
    void fullPipelineProducesReportWithAllStudents() throws IOException {
        String script = """
                tasks {
                    task('T1') { title = 'Task One'; maxScore = 10 }
                    task('T2') { title = 'Task Two'; maxScore = 10 }
                }
                groups {
                    group('G1') {
                        student { github = 'alice'; name = 'Alice Test'; repo = 'https://github.com/nonexistent/r1' }
                    }
                    group('G2') {
                        student { github = 'bob';   name = 'Bob Test';   repo = 'https://github.com/nonexistent/r2' }
                    }
                }
                checkPoints {
                    checkPoint('CP1') { date = '2026-06-01' }
                }
                assignments {
                    assign { students = ['alice', 'bob']; tasks = ['T1', 'T2'] }
                }
                settings {
                    gradeThresholds { excellent = 85; good = 70; satisfactory = 55 }
                }
                """;

        Path scriptFile = tempDir.resolve("oop_checker.groovy");
        Files.writeString(scriptFile, script);

        OopCheckerConfig config = new ConfigLoader().load(scriptFile.toFile());

        assertEquals(2, config.getTasks().size());
        assertEquals(2, config.getGroups().size());
        assertEquals(1, config.getCheckPoints().size());

        CheckRunner runner = new CheckRunner(config);
        runner.run();

        Map<String, StudentResult> results = runner.getResults();
        assertEquals(2, results.size(), "Both students should have results");
        assertFalse(results.get("alice").isRepoCloned(), "Non-existent repo should fail to clone");
        assertFalse(results.get("bob").isRepoCloned());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new HtmlReporter(config, results).generate(new PrintStream(baos));
        String html = baos.toString();

        assertTrue(html.startsWith("<!DOCTYPE html>"), "Report must be valid HTML");
        assertTrue(html.contains("Alice Test"), "Report must contain student name");
        assertTrue(html.contains("Bob Test"),   "Report must contain student name");
        assertTrue(html.contains("G1"),         "Report must contain group name");
        assertTrue(html.contains("G2"),         "Report must contain group name");
        assertTrue(html.contains("T1"),         "Report must contain task id");
        assertTrue(html.contains("T2"),         "Report must contain task id");
        assertTrue(html.contains("CP1"),        "Report must contain checkpoint name");
    }
}
