package ru.nsu.romanenko.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.Subject;
import ru.nsu.romanenko.Subjects;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class SubjectTest {
    private Subject examSubject;
    private Subject creditSubject;

    @BeforeEach
    void setUp() {
        examSubject = new Subject(Subjects.EXAM, 3);
        creditSubject = new Subject(Subjects.CREDIT, 1);
    }

    @Test
    void testConstructor() {
        assertEquals(Subjects.EXAM, examSubject.getName());
        assertEquals(3, examSubject.getCount());
        assertTrue(examSubject.getMarks().isEmpty());
    }

    @Test
    void testAddMark() {
        examSubject.addMark(5);
        examSubject.addMark(4);

        ArrayList<Integer> marks = examSubject.getMarks();
        assertEquals(2, marks.size());
        assertEquals(5, marks.get(0));
        assertEquals(4, marks.get(1));
    }

    @Test
    void testAddMarkBeyondCount() {
        examSubject.addMark(5);
        examSubject.addMark(4);
        examSubject.addMark(3);
        examSubject.addMark(5);

        assertEquals(4, examSubject.getCurrentCount());
    }

    @Test
    void testGetMarksReturnsCopy() {
        examSubject.addMark(5);
        ArrayList<Integer> marks1 = examSubject.getMarks();
        ArrayList<Integer> marks2 = examSubject.getMarks();

        assertNotSame(marks1, marks2);
        assertEquals(marks1, marks2);
    }

    @Test
    void testGetCurrentCount() {
        assertEquals(0, examSubject.getCurrentCount());
        examSubject.addMark(5);
        assertEquals(1, examSubject.getCurrentCount());
        examSubject.addMark(4);
        assertEquals(2, examSubject.getCurrentCount());
    }

    @Test
    void testDifferentSubjectTypes() {
        assertEquals(Subjects.EXAM, examSubject.getName());
        assertEquals(Subjects.CREDIT, creditSubject.getName());
        assertTrue(creditSubject.getMarks().isEmpty());
    }
}