package ru.nsu.romanenko.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String name;
    private final List<Student> students = new ArrayList<>();

    public Group() {}

    public Group(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Student> getStudents() { return students; }

    public void addStudent(Student student) {
        student.setGroupName(this.name);
        students.add(student);
    }

    public Student findByGithub(String github) {
        return students.stream()
            .filter(s -> s.getGithub().equals(github))
            .findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return "Group{name='" + name + "', students=" + students.size() + "}";
    }
}
