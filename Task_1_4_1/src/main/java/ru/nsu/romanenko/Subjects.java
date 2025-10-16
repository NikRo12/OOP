package ru.nsu.romanenko;

public enum Subjects {
    TASK("Task", false),
    CONTROL("Control", false),
    COLLOQUIUM("Colloquium", false),
    EXAM("Exam", true),
    DIFFERENTIATED("Differentiated", true),
    CREDIT("Credit", false),
    PRACTICE("Practice", true),
    COURSE("Course", true);

    private final String stringName;
    private final boolean examDiscipline;

    Subjects(String stringName, boolean examDiscipline)
    {
        this.stringName = stringName;
        this.examDiscipline = examDiscipline;
    }

    public String getStringName() {
        return stringName;
    }

    public boolean isExamDiscipline() {
        return examDiscipline;
    }
}
