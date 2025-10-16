package ru.nsu.romanenko.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.CreditBook;
import ru.nsu.romanenko.Subject;
import ru.nsu.romanenko.Subjects;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Map;

class CreditBookTest {
    private CreditBook budgetStudent;
    private CreditBook paidStudent;
    private CreditBook eighthSemesterStudent;

    @BeforeEach
    void setUp() {
        budgetStudent = new CreditBook("Иван Иванов", true);
        paidStudent = new CreditBook("Петр Петров", false);
        eighthSemesterStudent = new CreditBook("Сергей Сергеев", true);

        // Переводим студента на 8 семестр
        for (int i = 1; i < 8; i++) {
            eighthSemesterStudent.evaluateSemester();
        }
    }

    @Test
    void testConstructor() {
        assertEquals("Иван Иванов", budgetStudent.getStudentName());
        assertTrue(budgetStudent.isBudget());
        assertEquals(1, budgetStudent.getCurrentSemester());
        assertNotNull(budgetStudent.getEachSemester());
        assertEquals(8, budgetStudent.getEachSemester().size());
    }

    @Test
    void testGetAverageScoreEmpty() {
        assertEquals(0.0f, budgetStudent.getAverageScore());
    }

    @Test
    void testGetAverageScoreWithMarks() {
        // Добавляем оценки за 1 семестр
        budgetStudent.addMark(1, Subjects.EXAM, 5);
        budgetStudent.addMark(1, Subjects.DIFFERENTIATED, 4);
        budgetStudent.addMark(1, Subjects.EXAM, 5);

        assertEquals(4.666f, budgetStudent.getAverageScore(), 0.001f);
    }

    @Test
    void testGetAverageScoreOnlyExamDisciplines() {
        budgetStudent.addMark(1, Subjects.EXAM, 5);
        budgetStudent.addMark(1, Subjects.TASK, 5);
        budgetStudent.addMark(1, Subjects.CONTROL, 4);

        assertEquals(5.0f, budgetStudent.getAverageScore());
    }

    @Test
    void testCanTransferToBudgetForBudgetStudent() {
        assertTrue(budgetStudent.canTransferToBudget());
    }

    @Test
    void testCanTransferToBudgetForPaidStudentFirstSemester() {
        assertFalse(paidStudent.canTransferToBudget());
    }

    @Test
    void testCanTransferToBudgetForPaidStudentWithGoodMarks() {
        paidStudent.evaluateSemester();
        paidStudent.evaluateSemester();

        paidStudent.addMark(2, Subjects.EXAM, 5);
        paidStudent.addMark(2, Subjects.DIFFERENTIATED, 4);
        paidStudent.addMark(1, Subjects.EXAM, 5);
        paidStudent.addMark(1, Subjects.CREDIT, 1);

        assertTrue(paidStudent.canTransferToBudget());
    }

    @Test
    void testCanTransferToBudgetForPaidStudentWithBadMarks() {
        paidStudent.evaluateSemester();

        paidStudent.addMark(1, Subjects.EXAM, 3);

        assertFalse(paidStudent.canTransferToBudget());
    }

    @Test
    void testCanTransferToBudgetWithUnsatisfactoryCredit() {
        paidStudent.evaluateSemester();

        paidStudent.addMark(1, Subjects.CREDIT, 0);

        assertFalse(paidStudent.canTransferToBudget());
    }

    @Test
    void testCanGetRedDiplomaEighthSemesterExcellent() {
        eighthSemesterStudent.addMark(8, Subjects.COURSE, 5);
        eighthSemesterStudent.addMark(8, Subjects.DIFFERENTIATED, 5);
        eighthSemesterStudent.addMark(8, Subjects.PRACTICE, 5);
        eighthSemesterStudent.addMark(8, Subjects.CREDIT, 1);

        assertTrue(eighthSemesterStudent.canGetRedDiploma());
    }

    @Test
    void testCanGetRedDiplomaWithSatisfactoryMarks() {
        eighthSemesterStudent.addMark(8, Subjects.COURSE, 5);
        eighthSemesterStudent.addMark(8, Subjects.DIFFERENTIATED, 3);

        assertFalse(eighthSemesterStudent.canGetRedDiploma());
    }

    @Test
    void testCanGetRedDiplomaWithBadCourse() {
        eighthSemesterStudent.addMark(8, Subjects.COURSE, 4);
        eighthSemesterStudent.addMark(8, Subjects.DIFFERENTIATED, 5);

        assertFalse(eighthSemesterStudent.canGetRedDiploma());
    }

    @Test
    void testCanGetRedDiplomaInsufficientExcellentPercentage() {
        eighthSemesterStudent.addMark(8, Subjects.COURSE, 5);
        eighthSemesterStudent.addMark(8, Subjects.DIFFERENTIATED, 5);
        eighthSemesterStudent.addMark(8, Subjects.EXAM, 4);
        eighthSemesterStudent.addMark(8, Subjects.PRACTICE, 4);

        assertFalse(eighthSemesterStudent.canGetRedDiploma());
    }

    @Test
    void testCanGetRedDiplomaNotFinalSemester() {
        budgetStudent.addMark(1, Subjects.EXAM, 5);
        budgetStudent.addMark(1, Subjects.DIFFERENTIATED, 5);

        assertDoesNotThrow(() -> budgetStudent.canGetRedDiploma());
    }

    @Test
    void testCanGetIncreasedScholarshipFirstSemester() {
        assertFalse(budgetStudent.canGetIncreasedScholarship());
    }

    @Test
    void testCanGetIncreasedScholarshipPaidStudent() {
        assertFalse(paidStudent.canGetIncreasedScholarship());
    }

    @Test
    void testCanGetIncreasedScholarshipWithExcellentMarks() {
        budgetStudent.evaluateSemester();

        budgetStudent.addMark(1, Subjects.EXAM, 5);
        budgetStudent.addMark(1, Subjects.DIFFERENTIATED, 5);
        budgetStudent.addMark(1, Subjects.CREDIT, 1);

        assertTrue(budgetStudent.canGetIncreasedScholarship());
    }

    @Test
    void testCanGetIncreasedScholarshipWithGoodMarks() {
        budgetStudent.evaluateSemester();

        budgetStudent.addMark(1, Subjects.EXAM, 5);
        budgetStudent.addMark(1, Subjects.DIFFERENTIATED, 4);

        assertFalse(budgetStudent.canGetIncreasedScholarship());
    }

    @Test
    void testCanGetIncreasedScholarshipWithUnsatisfactoryCredit() {
        budgetStudent.evaluateSemester();

        budgetStudent.addMark(1, Subjects.EXAM, 5);
        budgetStudent.addMark(1, Subjects.CREDIT, 0);

        assertFalse(budgetStudent.canGetIncreasedScholarship());
    }

    @Test
    void testAddMark() {
        budgetStudent.addMark(1, Subjects.EXAM, 5);

        Map<Integer, HashSet<Subject>> semesters = budgetStudent.getEachSemester();
        Subject examSubject = semesters.get(1).stream()
                .filter(s -> s.getName() == Subjects.EXAM)
                .findFirst()
                .orElse(null);

        assertNotNull(examSubject);
        assertEquals(1, examSubject.getMarks().size());
        assertEquals(5, examSubject.getMarks().get(0));
    }

    @Test
    void testEvaluateSemester() {
        assertEquals(1, budgetStudent.getCurrentSemester());
        budgetStudent.evaluateSemester();
        assertEquals(2, budgetStudent.getCurrentSemester());
    }

    @Test
    void testEvaluateSemesterMax() {
        CreditBook maxStudent = new CreditBook("Макс", true);
        for (int i = 1; i < 10; i++) {
            maxStudent.evaluateSemester();
        }
        assertEquals(8, maxStudent.getCurrentSemester());
    }
}