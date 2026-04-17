package ru.nsu.romanenko.dsl

import ru.nsu.romanenko.model.*

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OopCheckerDslDelegate {

    private final OopCheckerConfig config
    private final File baseDir

    OopCheckerDslDelegate(OopCheckerConfig config, File baseDir) {
        this.config = config
        this.baseDir = baseDir
    }

    void include(String relativePath) {
        File file = new File(baseDir, relativePath)
        if (!file.exists()) {
            throw new FileNotFoundException("Included config file not found: ${file.absolutePath}")
        }
        def loader = new ConfigLoader()
        def sub = loader.load(file)
        sub.tasks.values().each { config.addTask(it) }
        sub.groups.values().each { config.addGroup(it) }
        sub.assignments.each { config.addAssignment(it) }
        sub.checkPoints.each { config.addCheckPoint(it) }
    }

    void tasks(Closure cl) {
        cl.delegate = new TasksSpec(config)
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    void groups(Closure cl) {
        cl.delegate = new GroupsSpec(config)
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    void checkPoints(Closure cl) {
        cl.delegate = new CheckPointsSpec(config)
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    void assignments(Closure cl) {
        cl.delegate = new AssignmentsSpec(config)
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    void settings(Closure cl) {
        cl.delegate = new SettingsSpec(config.gradeConfig)
        cl.resolveStrategy = Closure.DELEGATE_FIRST
        cl.call()
    }

    static class TasksSpec {
        private final OopCheckerConfig config
        TasksSpec(OopCheckerConfig config) { this.config = config }

        void task(String id, Closure cl) {
            def spec = new TaskSpec(id)
            cl.delegate = spec
            cl.resolveStrategy = Closure.DELEGATE_FIRST
            cl.call()
            config.addTask(spec.build())
        }
    }

    static class TaskSpec {
        String id
        String title
        int maxScore = 100
        String softDeadline
        String hardDeadline

        TaskSpec(String id) { this.id = id }

        Task build() {
            def fmt = DateTimeFormatter.ISO_LOCAL_DATE
            new Task(
                id,
                title ?: id,
                maxScore,
                softDeadline ? LocalDate.parse(softDeadline, fmt) : null,
                hardDeadline ? LocalDate.parse(hardDeadline, fmt) : null
            )
        }
    }

    static class GroupsSpec {
        private final OopCheckerConfig config
        GroupsSpec(OopCheckerConfig config) { this.config = config }

        void group(String name, Closure cl) {
            def group = new Group(name)
            def spec = new GroupSpec(group)
            cl.delegate = spec
            cl.resolveStrategy = Closure.DELEGATE_FIRST
            cl.call()
            config.addGroup(group)
        }
    }

    static class GroupSpec {
        private final Group group
        GroupSpec(Group group) { this.group = group }

        void student(Closure cl) {
            def spec = new StudentSpec()
            cl.delegate = spec
            cl.resolveStrategy = Closure.DELEGATE_FIRST
            cl.call()
            group.addStudent(spec.build())
        }
    }

    static class StudentSpec {
        String github
        String name
        String repo

        Student build() {
            new Student(github, name, repo)
        }
    }

    static class CheckPointsSpec {
        private final OopCheckerConfig config
        CheckPointsSpec(OopCheckerConfig config) { this.config = config }

        void checkPoint(String name, Closure cl) {
            def spec = new CheckPointSpec(name)
            cl.delegate = spec
            cl.resolveStrategy = Closure.DELEGATE_FIRST
            cl.call()
            config.addCheckPoint(spec.build())
        }
    }

    static class CheckPointSpec {
        String name
        String date

        CheckPointSpec(String name) { this.name = name }

        CheckPoint build() {
            new CheckPoint(name,
                date ? LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE) : null)
        }
    }

    static class AssignmentsSpec {
        private final OopCheckerConfig config
        AssignmentsSpec(OopCheckerConfig config) { this.config = config }

        void assign(Closure cl) {
            def spec = new AssignSpec()
            cl.delegate = spec
            cl.resolveStrategy = Closure.DELEGATE_FIRST
            cl.call()
            config.addAssignment(spec.build())
        }
    }

    static class AssignSpec {
        List<String> students = []
        List<String> tasks = []

        AssignmentEntry build() {
            def entry = new AssignmentEntry()
            entry.studentGithubs = students
            entry.taskIds = tasks
            entry
        }
    }

    static class SettingsSpec {
        private final GradeConfig gradeConfig

        SettingsSpec(GradeConfig gradeConfig) { this.gradeConfig = gradeConfig }

        void setTestTimeout(int seconds) {
            gradeConfig.testTimeoutSeconds = seconds
        }

        void gradeThresholds(Closure cl) {
            def spec = new GradeThresholdSpec(gradeConfig)
            cl.delegate = spec
            cl.resolveStrategy = Closure.DELEGATE_FIRST
            cl.call()
        }

        void bonusPoints(Closure cl) {
            def spec = new BonusSpec(gradeConfig)
            cl.delegate = spec
            cl.resolveStrategy = Closure.DELEGATE_FIRST
            cl.call()
        }
    }

    static class GradeThresholdSpec {
        private final GradeConfig gradeConfig
        GradeThresholdSpec(GradeConfig c) { this.gradeConfig = c }
        void setExcellent(int v) { gradeConfig.excellentThreshold = v }
        void setGood(int v)      { gradeConfig.goodThreshold = v }
        void setSatisfactory(int v) { gradeConfig.satisfactoryThreshold = v }
    }

    static class BonusSpec {
        private final GradeConfig gradeConfig
        BonusSpec(GradeConfig c) { this.gradeConfig = c }

        void student(String github, Closure cl) {
            def spec = new BonusStudentSpec(gradeConfig, github)
            cl.delegate = spec
            cl.resolveStrategy = Closure.DELEGATE_FIRST
            cl.call()
        }
    }

    static class BonusStudentSpec {
        private final GradeConfig gradeConfig
        private final String github
        BonusStudentSpec(GradeConfig c, String github) {
            this.gradeConfig = c
            this.github = github
        }

        void task(String taskId, Closure cl) {
            def spec = new BonusTaskSpec()
            cl.delegate = spec
            cl.resolveStrategy = Closure.DELEGATE_FIRST
            cl.call()
            gradeConfig.addBonus(github, taskId, spec.bonus)
        }
    }

    static class BonusTaskSpec {
        int bonus = 0
    }
}
