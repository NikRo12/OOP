package ru.nsu.romanenko;

import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.dsl.OopCheckerConfig;
import ru.nsu.romanenko.model.*;
import ru.nsu.romanenko.report.HtmlReporter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HtmlReporterTest {

    @Test
    void reportContainsStudentNames() {
        OopCheckerConfig config = buildConfig();
        Map<String, StudentResult> results = buildResults(config);

        String html = generateHtml(config, results);

        assertTrue(html.contains("John Doe"), "Report should contain student full name");
        assertTrue(html.contains("ivanov"),       "Report should contain github login");
    }

    @Test
    void reportIsValidHtml() {
        OopCheckerConfig config = buildConfig();
        String html = generateHtml(config, buildResults(config));

        assertTrue(html.startsWith("<!DOCTYPE html>"));
        assertTrue(html.contains("</html>"));
        assertTrue(html.contains("<table"));
        assertTrue(html.contains("</table>"));
    }

    @Test
    void reportContainsTaskIds() {
        OopCheckerConfig config = buildConfig();
        String html = generateHtml(config, buildResults(config));

        assertTrue(html.contains("Task_1_1_1"));
    }

    @Test
    void reportContainsGroupName() {
        OopCheckerConfig config = buildConfig();
        String html = generateHtml(config, buildResults(config));

        assertTrue(html.contains("24213"));
    }

    @Test
    void reportContainsScores() {
        OopCheckerConfig config = buildConfig();
        Map<String, StudentResult> results = buildResults(config);
        StudentResult sr = results.get("ivanov");
        TaskResult tr = new TaskResult("Task_1_1_1");
        tr.setStatus(TaskResult.Status.SUCCESS);
        tr.setScore(1.0);
        sr.addTaskResult(tr);

        String html = generateHtml(config, results);
        assertTrue(html.contains("1.0"), "Score should appear in report");
    }

    // -------------------------------------------------------------------------

    private OopCheckerConfig buildConfig() {
        OopCheckerConfig config = new OopCheckerConfig();

        Task t = new Task("Task_1_1_1", "Heapsort", 1,
            LocalDate.of(2026, 10, 6), LocalDate.of(2026, 10, 20));
        config.addTask(t);

        Group g = new Group("24213");
        Student s = new Student("ivanov", "John Doe", "https://github.com/ivanov/OOP");
        g.addStudent(s);
        config.addGroup(g);

        CheckPoint cp = new CheckPoint("CP1", LocalDate.of(2025, 11, 1));
        config.addCheckPoint(cp);

        return config;
    }

    private Map<String, StudentResult> buildResults(OopCheckerConfig config) {
        Student s = config.findStudent("ivanov");
        StudentResult sr = new StudentResult(s);
        sr.setRepoCloned(true);
        return Map.of("ivanov", sr);
    }

    private String generateHtml(OopCheckerConfig config,
                                  Map<String, StudentResult> results) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        new HtmlReporter(config, results).generate(ps);
        return baos.toString();
    }
}
