package ru.nsu.romanenko.model;

import java.time.LocalDate;
import java.util.*;

public class StudentResult {
    private final Student student;
    private final Map<String, TaskResult> taskResults = new LinkedHashMap<>();
    private boolean repoCloned = false;
    private String repoCloneError;

    public StudentResult(Student student) {
        this.student = student;
    }

    public Student getStudent() { return student; }

    public boolean isRepoCloned() { return repoCloned; }
    public void setRepoCloned(boolean repoCloned) { this.repoCloned = repoCloned; }

    public String getRepoCloneError() { return repoCloneError; }
    public void setRepoCloneError(String repoCloneError) { this.repoCloneError = repoCloneError; }

    public void addTaskResult(TaskResult result) {
        taskResults.put(result.getTaskId(), result);
    }

    public TaskResult getTaskResult(String taskId) {
        return taskResults.get(taskId);
    }

    public Map<String, TaskResult> getTaskResults() {
        return Collections.unmodifiableMap(taskResults);
    }

    public double getTotalScore() {
        return taskResults.values().stream()
            .mapToDouble(TaskResult::getTotalScore)
            .sum();
    }

    public double getMaxPossibleScore(Map<String, Task> taskMap) {
        return taskResults.keySet().stream()
            .mapToInt(id -> taskMap.getOrDefault(id, new Task()).getMaxScore())
            .sum();
    }


    public double getScoreByDate(LocalDate date, Map<String, Task> taskMap) {
        return taskResults.values().stream()
            .filter(r -> r.getLastCommitDate() != null && !r.getLastCommitDate().isAfter(date))
            .mapToDouble(TaskResult::getTotalScore)
            .sum();
    }
}
