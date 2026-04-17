package ru.nsu.romanenko;

import org.junit.jupiter.api.*;
import ru.nsu.romanenko.checker.ScoreCalculator;
import ru.nsu.romanenko.model.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ScoreCalculatorTest {

    private ScoreCalculator calc;
    private GradeConfig gradeConfig;
    private Task task;

    @BeforeEach
    void setUp() {
        calc = new ScoreCalculator();
        gradeConfig = new GradeConfig();

        task = new Task(
            "Task_1_1_1", "Heapsort", 1,
            LocalDate.of(2026, 10, 6),
            LocalDate.of(2026, 10, 20)
        );
    }

    @Test
    void fullScoreBeforeSoftDeadline() {
        TaskResult r = new TaskResult("Task_1_1_1");
        r.setStatus(TaskResult.Status.SUCCESS);
        r.setCompiled(true);
        r.setTestsPassed(10);
        r.setTestsFailed(0);
        r.setTestsSkipped(0);
        r.setLastCommitDate(LocalDate.of(2026, 10, 1));

        calc.calculate(r, task, gradeConfig);

        assertEquals(1.0, r.getScore(), 0.01,
            "Full score expected when submitted before soft deadline with all tests passing");
    }

    @Test
    void halfScoreAfterSoftDeadline() {
        TaskResult r = new TaskResult("Task_1_1_1");
        r.setStatus(TaskResult.Status.SUCCESS);
        r.setCompiled(true);
        r.setTestsPassed(10);
        r.setTestsFailed(0);
        r.setTestsSkipped(0);
        r.setLastCommitDate(LocalDate.of(2026, 10, 10));

        calc.calculate(r, task, gradeConfig);

        assertEquals(0.5, r.getScore(), 0.01,
            "Half score expected when submitted after soft but before hard deadline");
    }

    @Test
    void zeroScoreAfterHardDeadline() {
        TaskResult r = new TaskResult("Task_1_1_1");
        r.setStatus(TaskResult.Status.SUCCESS);
        r.setCompiled(true);
        r.setTestsPassed(10);
        r.setTestsFailed(0);
        r.setLastCommitDate(LocalDate.of(2026, 11, 1));

        calc.calculate(r, task, gradeConfig);

        assertEquals(0.0, r.getScore(), 0.01,
            "Zero score expected after hard deadline");
    }

    @Test
    void partialScoreWithSomeFailedTests() {
        TaskResult r = new TaskResult("Task_1_1_1");
        r.setStatus(TaskResult.Status.TESTS_FAILED);
        r.setCompiled(true);
        r.setTestsPassed(7);
        r.setTestsFailed(3);
        r.setTestsSkipped(0);
        r.setLastCommitDate(LocalDate.of(2026, 10, 1));

        calc.calculate(r, task, gradeConfig);

        // 7/10 = 70% of 1 = 0.7 base, no deadline penalty
        assertTrue(r.getScore() > 0.6 && r.getScore() <= 0.8,
            "Partial score expected: " + r.getScore());
    }

    @Test
    void compileErrorGivesZero() {
        TaskResult r = new TaskResult("Task_1_1_1");
        r.setStatus(TaskResult.Status.COMPILE_ERROR);
        r.setLastCommitDate(LocalDate.of(2026, 10, 1));

        calc.calculate(r, task, gradeConfig);

        assertEquals(0.0, r.getScore(), 0.01,
            "Compile error should yield zero score");
    }

    @Test
    void bonusPointsAddedOnTop() {
        TaskResult r = new TaskResult("Task_1_1_1");
        r.setStatus(TaskResult.Status.SUCCESS);
        r.setCompiled(true);
        r.setTestsPassed(10);
        r.setLastCommitDate(LocalDate.of(2026, 10, 1));
        r.setBonusScore(1);

        calc.calculate(r, task, gradeConfig);

        assertEquals(2.0, r.getTotalScore(), 0.01,
            "Bonus should be added on top of base score");
    }

    @Test
    void gradeExcellent() {
        assertEquals("5 (Excellent)", calc.computeGrade(0.95, 1, gradeConfig));
    }

    @Test
    void gradeGood() {
        assertEquals("4 (Good)", calc.computeGrade(0.80, 1, gradeConfig));
    }

    @Test
    void gradeSatisfactory() {
        assertEquals("3 (Satisfactory)", calc.computeGrade(0.65, 1, gradeConfig));
    }

    @Test
    void gradeUnsatisfactory() {
        assertEquals("2 (Fail)", calc.computeGrade(0.40, 1, gradeConfig));
    }

    @Test
    void noDeadlinesGiveFullScore() {
        Task noDeadlineTask = new Task("T", "Test", 1, null, null);
        TaskResult r = new TaskResult("T");
        r.setStatus(TaskResult.Status.SUCCESS);
        r.setCompiled(true);
        r.setTestsPassed(5);
        r.setLastCommitDate(LocalDate.of(2026, 1, 1));

        calc.calculate(r, noDeadlineTask, gradeConfig);

        assertTrue(r.getScore() > 0, "Should get score even with no deadlines");
    }
}
