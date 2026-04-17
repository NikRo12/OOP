package ru.nsu.romanenko.model;

import java.time.LocalDate;

public class Task {
    private String id;
    private String title;
    private int maxScore;
    private LocalDate softDeadline;
    private LocalDate hardDeadline;

    public Task() {}

    public Task(String id, String title, int maxScore,
                LocalDate softDeadline, LocalDate hardDeadline) {
        this.id = id;
        this.title = title;
        this.maxScore = maxScore;
        this.softDeadline = softDeadline;
        this.hardDeadline = hardDeadline;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getMaxScore() { return maxScore; }
    public void setMaxScore(int maxScore) { this.maxScore = maxScore; }

    public LocalDate getSoftDeadline() { return softDeadline; }
    public void setSoftDeadline(LocalDate softDeadline) { this.softDeadline = softDeadline; }

    public LocalDate getHardDeadline() { return hardDeadline; }
    public void setHardDeadline(LocalDate hardDeadline) { this.hardDeadline = hardDeadline; }

    @Override
    public String toString() {
        return "Task{id='" + id + "', title='" + title + "', maxScore=" + maxScore + "}";
    }
}
