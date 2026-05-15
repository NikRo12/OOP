package ru.nsu.romanenko.dsl;

import ru.nsu.romanenko.model.*;

import java.util.*;

public class OopCheckerConfig {
    private Map<String, Task> tasks = new LinkedHashMap<>();
    private Map<String, Group> groups = new LinkedHashMap<>();
    private List<AssignmentEntry> assignments = new ArrayList<>();
    private List<CheckPoint> checkPoints = new ArrayList<>();
    private GradeConfig gradeConfig = new GradeConfig();

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public Task getTask(String id) {
        return tasks.get(id);
    }

    public Map<String, Task> getTasks() {
        return Collections.unmodifiableMap(tasks);
    }

    public void addGroup(Group group) {
        groups.put(group.getName(), group);
    }

    public Map<String, Group> getGroups() {
        return Collections.unmodifiableMap(groups);
    }

    public Student findStudent(String github) {
        for (Group g : groups.values()) {
            Student s = g.findByGithub(github);
            if (s != null) return s;
        }
        return null;
    }

    public List<Student> getAllStudents() {
        List<Student> result = new ArrayList<>();
        for (Group g : groups.values()) {
            result.addAll(g.getStudents());
        }
        return result;
    }

    public void addAssignment(AssignmentEntry entry) {
        assignments.add(entry);
    }

    public List<AssignmentEntry> getAssignments() {
        return Collections.unmodifiableList(assignments);
    }

    public Set<String> getTasksForStudent(String github) {
        Set<String> result = new LinkedHashSet<>();
        for (AssignmentEntry entry : assignments) {
            if (entry.getStudentGithubs().contains(github)) {
                result.addAll(entry.getTaskIds());
            }
        }
        return result;
    }

    public void addCheckPoint(CheckPoint cp) {
        checkPoints.add(cp);
    }

    public List<CheckPoint> getCheckPoints() {
        return Collections.unmodifiableList(checkPoints);
    }

    public GradeConfig getGradeConfig() {
        return gradeConfig;
    }

    public void setGradeConfig(GradeConfig gradeConfig) {
        this.gradeConfig = gradeConfig;
    }
}
