package ru.nsu.romanenko.model;

public class TaskResult {
    public enum Status { NOT_CHECKED, COMPILE_ERROR, SUCCESS, TESTS_FAILED }

    private final String taskId;
    private Status status = Status.NOT_CHECKED;

    private boolean compiled = false;
    private boolean docGenerated = false;
    private boolean styleOk = false;

    private int testsPassed = 0;
    private int testsFailed = 0;
    private int testsSkipped = 0;

    private double score = 0.0;
    private int bonusScore = 0;
    private String errorMessage;

    private java.time.LocalDate lastCommitDate;

    public TaskResult(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() { return taskId; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public boolean isCompiled() { return compiled; }
    public void setCompiled(boolean compiled) { this.compiled = compiled; }

    public boolean isDocGenerated() { return docGenerated; }
    public void setDocGenerated(boolean docGenerated) { this.docGenerated = docGenerated; }

    public boolean isStyleOk() { return styleOk; }
    public void setStyleOk(boolean styleOk) { this.styleOk = styleOk; }

    public int getTestsPassed() { return testsPassed; }
    public void setTestsPassed(int testsPassed) { this.testsPassed = testsPassed; }

    public int getTestsFailed() { return testsFailed; }
    public void setTestsFailed(int testsFailed) { this.testsFailed = testsFailed; }

    public int getTestsSkipped() { return testsSkipped; }
    public void setTestsSkipped(int testsSkipped) { this.testsSkipped = testsSkipped; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public int getBonusScore() { return bonusScore; }
    public void setBonusScore(int bonusScore) { this.bonusScore = bonusScore; }

    public double getTotalScore() { return score + bonusScore; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public java.time.LocalDate getLastCommitDate() { return lastCommitDate; }
    public void setLastCommitDate(java.time.LocalDate lastCommitDate) {
        this.lastCommitDate = lastCommitDate;
    }

    public int getTotalTests() { return testsPassed + testsFailed + testsSkipped; }

    public String getStatusLabel() {
        return switch (status) {
            case NOT_CHECKED -> "Not checked";
            case COMPILE_ERROR -> "Compile error";
            case SUCCESS -> "Success";
            case TESTS_FAILED -> "Tests failed";
        };
    }

    public String getStatusCssClass() {
        return switch (status) {
            case SUCCESS -> "ok";
            case COMPILE_ERROR -> "err";
            case TESTS_FAILED -> "warn";
            default -> "na";
        };
    }
}
