package ru.nsu.romanenko.tests;

import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.Subjects;

import static org.junit.jupiter.api.Assertions.*;

class SubjectsTest {

    @Test
    void testGetStringName() {
        assertEquals("Task", Subjects.TASK.getStringName());
        assertEquals("Exam", Subjects.EXAM.getStringName());
        assertEquals("Credit", Subjects.CREDIT.getStringName());
        assertEquals("Course", Subjects.COURSE.getStringName());
    }

    @Test
    void testIsExamDiscipline() {
        assertFalse(Subjects.TASK.isExamDiscipline());
        assertFalse(Subjects.CONTROL.isExamDiscipline());
        assertFalse(Subjects.COLLOQUIUM.isExamDiscipline());
        assertTrue(Subjects.EXAM.isExamDiscipline());
        assertTrue(Subjects.DIFFERENTIATED.isExamDiscipline());
        assertFalse(Subjects.CREDIT.isExamDiscipline());
        assertTrue(Subjects.PRACTICE.isExamDiscipline());
        assertTrue(Subjects.COURSE.isExamDiscipline());
    }

    @Test
    void testEnumValues() {
        Subjects[] values = Subjects.values();
        assertEquals(8, values.length);

        assertArrayEquals(new Subjects[]{
                Subjects.TASK,
                Subjects.CONTROL,
                Subjects.COLLOQUIUM,
                Subjects.EXAM,
                Subjects.DIFFERENTIATED,
                Subjects.CREDIT,
                Subjects.PRACTICE,
                Subjects.COURSE
        }, values);
    }

    @Test
    void testValueOf() {
        assertEquals(Subjects.TASK, Subjects.valueOf("TASK"));
        assertEquals(Subjects.EXAM, Subjects.valueOf("EXAM"));
        assertEquals(Subjects.CREDIT, Subjects.valueOf("CREDIT"));
    }
}