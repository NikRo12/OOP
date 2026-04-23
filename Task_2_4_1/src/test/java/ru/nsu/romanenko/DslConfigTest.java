package ru.nsu.romanenko;

import org.junit.jupiter.api.*;
import ru.nsu.romanenko.dsl.ConfigLoader;
import ru.nsu.romanenko.dsl.OopCheckerConfig;
import ru.nsu.romanenko.model.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DslConfigTest {

    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("oop-checker-test-");
    }

    @AfterEach
    void tearDown() throws IOException {
        try (var walk = Files.walk(tempDir)) {
            walk.sorted(java.util.Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }
    }

    @Test
    void loadsTasksFromScript() throws IOException {
        String script = """
            tasks {
                task('Task_1_1_1') {
                    title        = 'Heapsort'
                    maxScore     = 1
                    softDeadline = '2026-10-06'
                    hardDeadline = '2026-10-20'
                }
            }
            """;
        OopCheckerConfig config = loadScript(script);

        Task t = config.getTask("Task_1_1_1");
        assertNotNull(t, "Task should be loaded");
        assertEquals("Heapsort", t.getTitle());
        assertEquals(1, t.getMaxScore());
        assertEquals(LocalDate.of(2026, 10, 6), t.getSoftDeadline());
        assertEquals(LocalDate.of(2026, 10, 20), t.getHardDeadline());
    }

    @Test
    void loadsGroupsAndStudents() throws IOException {
        String script = """
            groups {
                group('24213') {
                    student {
                        github = 'ivanov'
                        name   = 'John Doe'
                        repo   = 'https://github.com/ivanov/OOP'
                    }
                }
            }
            """;
        OopCheckerConfig config = loadScript(script);

        assertEquals(1, config.getGroups().size());
        Group g = config.getGroups().get("24213");
        assertNotNull(g);
        assertEquals(1, g.getStudents().size());

        Student s = g.getStudents().get(0);
        assertEquals("ivanov", s.getGithub());
        assertEquals("John Doe", s.getFullName());
        assertEquals("https://github.com/ivanov/OOP", s.getRepoUrl());
        assertEquals("24213", s.getGroupName());
    }

    @Test
    void loadsCheckPoints() throws IOException {
        String script = """
            checkPoints {
                checkPoint('CP1') { date = '2025-11-01' }
                checkPoint('CP2') { date = '2026-01-15' }
            }
            """;
        OopCheckerConfig config = loadScript(script);

        assertEquals(2, config.getCheckPoints().size());
        CheckPoint cp = config.getCheckPoints().get(0);
        assertEquals("CP1", cp.getName());
        assertEquals(LocalDate.of(2025, 11, 1), cp.getDate());
    }

    @Test
    void loadsAssignments() throws IOException {
        String script = """
            assignments {
                assign {
                    students = ['ivanov', 'petrov']
                    tasks    = ['Task_1_1_1', 'Task_1_2_1']
                }
            }
            """;
        OopCheckerConfig config = loadScript(script);

        assertEquals(1, config.getAssignments().size());
        AssignmentEntry entry = config.getAssignments().get(0);
        assertTrue(entry.getStudentGithubs().contains("ivanov"));
        assertTrue(entry.getStudentGithubs().contains("petrov"));
        assertTrue(entry.getTaskIds().contains("Task_1_1_1"));
    }

    @Test
    void loadsSettings() throws IOException {
        String script = """
            settings {
                testTimeout = 45
                gradeThresholds {
                    excellent    = 88
                    good         = 72
                    satisfactory = 55
                }
                bonusPoints {
                    student('ivanov') {
                        task('Task_1_1_1') { bonus = 1 }
                    }
                }
            }
            """;
        OopCheckerConfig config = loadScript(script);

        GradeConfig gc = config.getGradeConfig();
        assertEquals(45, gc.getTestTimeoutSeconds());
        assertEquals(88, gc.getExcellentThreshold());
        assertEquals(72, gc.getGoodThreshold());
        assertEquals(55, gc.getSatisfactoryThreshold());
        assertEquals(1, gc.getBonus("ivanov", "Task_1_1_1"));
    }

    @Test
    void includeImportsAnotherFile() throws IOException {
        String tasksScript = """
            tasks {
                task('Task_1_1_1') {
                    title    = 'Heapsort'
                    maxScore = 1
                }
            }
            """;
        Path tasksFile = tempDir.resolve("tasks.groovy");
        Files.writeString(tasksFile, tasksScript);

        String mainScript = """
            include 'tasks.groovy'
            groups {
                group('24213') {
                    student {
                        github = 'ivanov'
                        name   = 'John Doe'
                        repo   = 'https://github.com/ivanov/OOP'
                    }
                }
            }
            """;
        OopCheckerConfig config = loadScript(mainScript);

        assertNotNull(config.getTask("Task_1_1_1"),
            "Task from included file should be present");
        assertFalse(config.getGroups().isEmpty(),
            "Groups from main file should be present");
    }

    @Test
    void getTasksForStudentReturnsCorrectTasks() throws IOException {
        String script = """
            groups {
                group('24213') {
                    student { github='ivanov'; name='John'; repo='https://github.com/x/y' }
                    student { github='petrov'; name='Peter'; repo='https://github.com/x/z' }
                }
            }
            assignments {
                assign { students=['ivanov','petrov']; tasks=['Task_1_1_1'] }
                assign { students=['ivanov'];          tasks=['Task_2_1_1'] }
            }
            """;
        OopCheckerConfig config = loadScript(script);

        var ivanovTasks = config.getTasksForStudent("ivanov");
        assertTrue(ivanovTasks.contains("Task_1_1_1"));
        assertTrue(ivanovTasks.contains("Task_2_1_1"));

        var petrovTasks = config.getTasksForStudent("petrov");
        assertTrue(petrovTasks.contains("Task_1_1_1"));
        assertFalse(petrovTasks.contains("Task_2_1_1"));
    }

    private OopCheckerConfig loadScript(String script) throws IOException {
        Path scriptFile = tempDir.resolve("oop_checker.groovy");
        Files.writeString(scriptFile, script);
        return new ConfigLoader().load(scriptFile.toFile());
    }
}
