package ru.nsu.romanenko.report;

import java.util.List;

public class ReportViewModel {

    private final List<TaskInfo> tasks;
    private final List<CheckpointInfo> checkPoints;
    private final List<GroupView> groups;

    public ReportViewModel(List<TaskInfo> tasks, List<CheckpointInfo> checkPoints,
                           List<GroupView> groups) {
        this.tasks = tasks;
        this.checkPoints = checkPoints;
        this.groups = groups;
    }

    public List<TaskInfo> getTasks() { return tasks; }
    public List<CheckpointInfo> getCheckPoints() { return checkPoints; }
    public List<GroupView> getGroups() { return groups; }
}
