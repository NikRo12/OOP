package ru.nsu.romanenko.report;

import ru.nsu.romanenko.model.TaskResult;

import java.util.List;
import java.util.Map;

public record StudentView(String fullName, String github,
                          Map<String, TaskResult> taskResults,
                          double totalScore, double totalMax,
                          List<CheckpointGradeView> checkpointGrades,
                          String finalGrade) {
}
