package ru.nsu.romanenko;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.romanenko.model.StudentResult;
import ru.nsu.romanenko.model.TaskResult;

import java.io.*;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTest {

    @TempDir
    Path tempDir;

    private String runApp(OopCheckerApp app, Path configFile) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream captured = new PrintStream(baos);
        PrintStream original = System.out;
        System.setOut(captured);
        try {
            app.run(new String[]{configFile.toAbsolutePath().toString()});
        } finally {
            System.setOut(original);
        }
        return baos.toString();
    }

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

        OopCheckerApp app = new OopCheckerApp();
        String html = runApp(app, scriptFile);

        Map<String, StudentResult> results = app.getLastResults();
        assertEquals(2, results.size(), "Both students should have results");
        assertFalse(results.get("alice").isRepoCloned(), "Non-existent repo should fail to clone");
        assertFalse(results.get("bob").isRepoCloned());

        assertTrue(html.startsWith("<!DOCTYPE html>"), "Report must be valid HTML");
        assertTrue(html.contains("Alice Test"), "Report must contain student name");
        assertTrue(html.contains("Bob Test"),   "Report must contain student name");
        assertTrue(html.contains("G1"),         "Report must contain group name");
        assertTrue(html.contains("G2"),         "Report must contain group name");
        assertTrue(html.contains("T1"),         "Report must contain task id");
        assertTrue(html.contains("T2"),         "Report must contain task id");
        assertTrue(html.contains("CP1"),        "Report must contain checkpoint name");
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.MINUTES)
    void nikro12RepoIsClonedAndTask2_1_1IsChecked() throws IOException {
        String script = """
                tasks {
                    task('Task_2_1_1') {
                        title        = 'Simple'
                        maxScore     = 1
                        softDeadline = '2026-02-14'
                        hardDeadline = '2026-02-21'
                    }
                }
                groups {
                    group('24214') {
                        student {
                            github = 'NikRo12'
                            name   = 'Romanenko Nikita'
                            repo   = 'https://github.com/NikRo12/OOP'
                        }
                    }
                }
                assignments {
                    assign { students = ['NikRo12']; tasks = ['Task_2_1_1'] }
                }
                settings {
                    testTimeout = 120
                    gradeThresholds { excellent = 85; good = 70; satisfactory = 55 }
                }
                """;

        Path scriptFile = tempDir.resolve("oop_checker.groovy");
        Files.writeString(scriptFile, script);

        OopCheckerApp app = new OopCheckerApp();
        String html = runApp(app, scriptFile);

        Map<String, StudentResult> results = app.getLastResults();
        assertTrue(results.containsKey("NikRo12"), "NikRo12 must be in results");

        StudentResult sr = results.get("NikRo12");
        assertTrue(sr.isRepoCloned(),
            "NikRo12 repo must be cloned/pulled; error: " + sr.getRepoCloneError());

        TaskResult tr = sr.getTaskResult("Task_2_1_1");
        assertNotNull(tr, "Task_2_1_1 result must exist");
        assertTrue(tr.isCompiled(),
            "Task_2_1_1 must compile; error: " + tr.getErrorMessage());

        assertTrue(html.contains("Romanenko Nikita"), "Report must contain student name");
        assertTrue(html.contains("NikRo12"),          "Report must contain student github");
        assertTrue(html.contains("Task_2_1_1"),       "Report must contain task id");
    }
}
