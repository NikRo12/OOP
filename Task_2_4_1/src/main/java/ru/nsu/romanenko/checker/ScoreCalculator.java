package ru.nsu.romanenko.checker;

import ru.nsu.romanenko.model.*;

import java.time.LocalDate;

public class ScoreCalculator {
    public void calculate(TaskResult result, Task task, GradeConfig gradeConfig) {
        if (result.getStatus() == TaskResult.Status.NOT_CHECKED ||
            result.getStatus() == TaskResult.Status.COMPILE_ERROR) {
            result.setScore(0);
            return;
        }

        double base = computeBaseScore(result, task);

        double deadlineFactor = computeDeadlineFactor(result.getLastCommitDate(), task);
        double score = base * deadlineFactor;

        result.setScore(score);
    }

    private double computeBaseScore(TaskResult result, Task task) {
        int total = result.getTotalTests();
        if (total == 0) {
            double score = 0;
            if (result.isCompiled()) score += task.getMaxScore() * 0.5;
            if (result.isDocGenerated()) score += task.getMaxScore() * 0.25;
            if (result.isStyleOk()) score += task.getMaxScore() * 0.25;
            return score;
        }

        double passRate = (double) result.getTestsPassed() / total;
        double base = task.getMaxScore() * passRate;

        if (result.isDocGenerated()) base += task.getMaxScore() * 0.05;
        if (result.isStyleOk())      base += task.getMaxScore() * 0.05;

        return Math.min(base, task.getMaxScore());
    }

    private double computeDeadlineFactor(LocalDate commitDate, Task task) {
        if (commitDate == null) return 0.0;

        LocalDate soft = task.getSoftDeadline();
        LocalDate hard = task.getHardDeadline();

        if (soft == null && hard == null) return 1.0;

        boolean beforeSoft = soft == null || !commitDate.isAfter(soft);
        boolean beforeHard = hard == null || !commitDate.isAfter(hard);

        if (beforeSoft)  return 1.0;
        if (beforeHard)  return 0.5;
        return 0.0;
    }

    public String computeGrade(double totalScore, double maxScore, GradeConfig gradeConfig) {
        if (maxScore == 0) return "—";
        double percent = (totalScore / maxScore) * 100.0;
        return gradeConfig.getGrade(percent);
    }
}
