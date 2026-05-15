package ru.nsu.romanenko.model;

import java.util.HashMap;
import java.util.Map;

public class GradeConfig {
    private int excellentThreshold = 90;
    private int goodThreshold = 75;
    private int satisfactoryThreshold = 60;
    private int testTimeoutSeconds = 30;

    private final Map<String, Map<String, Integer>> bonusPoints = new HashMap<>();

    public int getExcellentThreshold() { return excellentThreshold; }
    public void setExcellentThreshold(int excellentThreshold) {
        this.excellentThreshold = excellentThreshold;
    }

    public int getGoodThreshold() { return goodThreshold; }
    public void setGoodThreshold(int goodThreshold) {
        this.goodThreshold = goodThreshold;
    }

    public int getSatisfactoryThreshold() { return satisfactoryThreshold; }
    public void setSatisfactoryThreshold(int satisfactoryThreshold) {
        this.satisfactoryThreshold = satisfactoryThreshold;
    }

    public int getTestTimeoutSeconds() { return testTimeoutSeconds; }
    public void setTestTimeoutSeconds(int testTimeoutSeconds) {
        this.testTimeoutSeconds = testTimeoutSeconds;
    }

    public Map<String, Map<String, Integer>> getBonusPoints() { return bonusPoints; }

    public void addBonus(String github, String taskId, int bonus) {
        bonusPoints.computeIfAbsent(github, k -> new HashMap<>()).put(taskId, bonus);
    }

    public int getBonus(String github, String taskId) {
        return bonusPoints.getOrDefault(github, new HashMap<>()).getOrDefault(taskId, 0);
    }

    public String getGrade(double scorePercent) {
        if (scorePercent >= excellentThreshold) return "5 (Excellent)";
        if (scorePercent >= goodThreshold)      return "4 (Good)";
        if (scorePercent >= satisfactoryThreshold) return "3 (Satisfactory)";
        return "2 (Fail)";
    }
}
