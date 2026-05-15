package ru.nsu.romanenko.model;

public class Student {
    private String github;
    private String fullName;
    private String repoUrl;
    private String groupName;

    public Student() {}

    public Student(String github, String fullName, String repoUrl) {
        this.github = github;
        this.fullName = fullName;
        this.repoUrl = repoUrl;
    }

    public String getGithub() { return github; }
    public void setGithub(String github) { this.github = github; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRepoUrl() { return repoUrl; }
    public void setRepoUrl(String repoUrl) { this.repoUrl = repoUrl; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    @Override
    public String toString() {
        return "Student{github='" + github + "', name='" + fullName + "'}";
    }
}
