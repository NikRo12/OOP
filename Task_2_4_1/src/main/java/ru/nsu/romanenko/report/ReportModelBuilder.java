package ru.nsu.romanenko.report;

import ru.nsu.romanenko.checker.ScoreCalculator;
import ru.nsu.romanenko.dsl.OopCheckerConfig;
import ru.nsu.romanenko.model.*;

import java.util.*;

public class ReportModelBuilder {

    private final ScoreCalculator scoreCalculator = new ScoreCalculator();

    public ReportViewModel build(OopCheckerConfig config, Map<String, StudentResult> results) {
        List<Task> taskList = new ArrayList<>(config.getTasks().values());
        List<CheckPoint> cpList = config.getCheckPoints();

        List<TaskInfo> taskInfos = taskList.stream()
            .map(t -> new TaskInfo(
                t.getId(), t.getTitle(), t.getMaxScore(),
                t.getSoftDeadline(), t.getHardDeadline()))
            .toList();

        List<CheckpointInfo> cpInfos = cpList.stream()
            .map(cp -> new CheckpointInfo(cp.getName(), cp.getDate()))
            .toList();

        List<GroupView> groupViews = new ArrayList<>();
        for (Group group : config.getGroups().values()) {
            List<StudentView> studentViews = new ArrayList<>();
            for (Student student : group.getStudents()) {
                StudentResult sr = results.get(student.getGithub());
                studentViews.add(buildStudentView(student, sr, taskList, cpList, config));
            }
            groupViews.add(new GroupView(group.getName(), studentViews));
        }

        return new ReportViewModel(taskInfos, cpInfos, groupViews);
    }

    private StudentView buildStudentView(
            Student student, StudentResult sr,
            List<Task> taskList, List<CheckPoint> cpList,
            OopCheckerConfig config) {

        Map<String, TaskResult> taskResults = sr != null
            ? sr.getTaskResults() : Map.of();

        Set<String> assignedIds = config.getTasksForStudent(student.getGithub());
        double totalMax = taskList.stream()
            .filter(t -> assignedIds.contains(t.getId()))
            .mapToInt(Task::getMaxScore)
            .sum();

        double totalScore = taskResults.values().stream()
            .mapToDouble(TaskResult::getTotalScore)
            .sum();

        List<CheckpointGradeView> cpGrades = new ArrayList<>();
        for (CheckPoint cp : cpList) {
            double cpScore = sr != null
                ? sr.getScoreByDate(cp.getDate(), config.getTasks()) : 0;
            String grade = scoreCalculator.computeGrade(cpScore, totalMax, config.getGradeConfig());
            cpGrades.add(new CheckpointGradeView(cpScore, grade));
        }

        String finalGrade = scoreCalculator.computeGrade(
            totalScore, totalMax, config.getGradeConfig());

        return new StudentView(
            student.getFullName(), student.getGithub(),
            taskResults, totalScore, totalMax, cpGrades, finalGrade);
    }
}
