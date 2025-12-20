package ru.nsu.romanenko;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
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
}