package ru.nsu.romanenko;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.stream.IntStream;

public class CreditBook {
    @Getter
    private final String studentName;

    @Getter
    private final boolean budget;

    @Getter @Setter
    private int currentSemester;

    @Getter
    private final Map<Integer, HashSet<Subject>> eachSemester;

    public CreditBook(String studentName, boolean budget) {
        this.studentName = studentName;
        this.budget = budget;
        this.currentSemester = 1;
        this.eachSemester = new HashMap<>();
        initializeSemesters();
    }

    private void initializeSemesters() {
        HashSet<Subject> first = new HashSet<>();
        first.add(new Subject(Subjects.TASK, 2));
        first.add(new Subject(Subjects.CONTROL, 3));
        first.add(new Subject(Subjects.COLLOQUIUM, 1));
        first.add(new Subject(Subjects.EXAM, 3));
        first.add(new Subject(Subjects.DIFFERENTIATED, 3));
        first.add(new Subject(Subjects.CREDIT, 3));

        HashSet<Subject> second = new HashSet<>();
        second.add(new Subject(Subjects.TASK, 2));
        second.add(new Subject(Subjects.CONTROL, 3));
        second.add(new Subject(Subjects.COLLOQUIUM, 1));
        second.add(new Subject(Subjects.EXAM, 3));
        second.add(new Subject(Subjects.DIFFERENTIATED, 3));
        second.add(new Subject(Subjects.CREDIT, 2));

        HashSet<Subject> third = new HashSet<>();
        third.add(new Subject(Subjects.TASK, 3));
        third.add(new Subject(Subjects.CONTROL, 2));
        third.add(new Subject(Subjects.EXAM, 2));
        third.add(new Subject(Subjects.DIFFERENTIATED, 6));

        HashSet<Subject> fourth = new HashSet<>();
        fourth.add(new Subject(Subjects.TASK, 2));
        fourth.add(new Subject(Subjects.CONTROL, 1));
        fourth.add(new Subject(Subjects.EXAM, 5));
        fourth.add(new Subject(Subjects.DIFFERENTIATED, 5));

        HashSet<Subject> fifth = new HashSet<>();
        fifth.add(new Subject(Subjects.TASK, 2));
        fifth.add(new Subject(Subjects.CONTROL, 2));
        fifth.add(new Subject(Subjects.EXAM, 3));
        fifth.add(new Subject(Subjects.DIFFERENTIATED, 4));

        HashSet<Subject> sixth = new HashSet<>();
        sixth.add(new Subject(Subjects.TASK, 2));
        sixth.add(new Subject(Subjects.CONTROL, 2));
        sixth.add(new Subject(Subjects.EXAM, 2));
        sixth.add(new Subject(Subjects.DIFFERENTIATED, 6));

        HashSet<Subject> seventh = new HashSet<>();
        seventh.add(new Subject(Subjects.TASK, 2));
        seventh.add(new Subject(Subjects.EXAM, 1));
        seventh.add(new Subject(Subjects.DIFFERENTIATED, 4));
        seventh.add(new Subject(Subjects.CREDIT, 1));
        seventh.add(new Subject(Subjects.PRACTICE, 1));

        HashSet<Subject> eighth = new HashSet<>();
        eighth.add(new Subject(Subjects.DIFFERENTIATED, 4));
        eighth.add(new Subject(Subjects.CREDIT, 1));
        eighth.add(new Subject(Subjects.PRACTICE, 2));
        eighth.add(new Subject(Subjects.COURSE, 1));

        eachSemester.put(1, first);
        eachSemester.put(2, second);
        eachSemester.put(3, third);
        eachSemester.put(4, fourth);
        eachSemester.put(5, fifth);
        eachSemester.put(6, sixth);
        eachSemester.put(7, seventh);
        eachSemester.put(8, eighth);
    }

    public float getAverageScore() {
        IntSummaryStatistics stats = eachSemester.values().stream()
                .flatMap(Collection::stream)
                .filter(this::isSubjectExamDiscipline)
                .flatMap(subject -> subject.getMarks().stream())
                .mapToInt(Integer::intValue)
                .summaryStatistics();

        return stats.getCount() > 0 ? (float) stats.getAverage() : 0;
    }

    public boolean canTransferToBudget() {
        if (budget) {
            return true;
        }

        if (currentSemester < 2) {
            return false;
        }

        return IntStream.rangeClosed(currentSemester - 1, currentSemester)
                .mapToObj(eachSemester::get)
                .flatMap(Collection::stream)
                .noneMatch(subject ->
                        (isSubjectExamDiscipline(subject) || subject.getName() == Subjects.CREDIT)
                                && hasUnsatisfactoryMarks(subject)
                );
    }

    public boolean canGetRedDiploma() {
        HashSet<Subject> subjects = eachSemester.get(currentSemester);

        boolean hasBadMarks = subjects.stream().anyMatch(subject -> {
            if (subject.getName() == Subjects.COURSE) {
                return !subject.getMarks().isEmpty() && !subject.getMarks().contains(5);
            }
            if ((isSubjectExamDiscipline(subject) || subject.getName() == Subjects.CREDIT)
                    && hasUnsatisfactoryMarks(subject)) {
                return true;
            }
            return subject.getName() == Subjects.DIFFERENTIATED && subject.getMarks().contains(3);
        });

        if (hasBadMarks) return false;

        long totalExamMarks = subjects.stream()
                .filter(this::isSubjectExamDiscipline)
                .mapToLong(subject -> subject.getMarks().size())
                .sum();

        long excellentMarks = subjects.stream()
                .filter(this::isSubjectExamDiscipline)
                .flatMap(subject -> subject.getMarks().stream())
                .filter(mark -> mark == 5)
                .count();

        return totalExamMarks > 0 && ((double) excellentMarks / totalExamMarks >= 0.75);
    }

    public boolean canGetIncreasedScholarship() {
        if (!budget || currentSemester == 1) {
            return false;
        }

        return eachSemester.get(currentSemester - 1).stream()
                .allMatch(subject -> {
                    if (subject.getName() == Subjects.CREDIT && hasUnsatisfactoryMarks(subject)) {
                        return false;
                    }
                    return !isSubjectExamDiscipline(subject) || fullExcellentSubject(subject);
                });
    }

    public void addMark(int semester, Subjects subjectName, int mark) {
        eachSemester.get(semester).stream()
                .filter(subject -> subject.getName() == subjectName)
                .findFirst()
                .ifPresent(subject -> subject.addMark(mark));
    }

    public void evaluateSemester() {
        if (currentSemester < 8) {
            currentSemester++;
        }
    }


    private boolean isSubjectExamDiscipline(Subject subject) {
        return subject.getName().isExamDiscipline();
    }

    private boolean hasUnsatisfactoryMarks(Subject subject) {
        if (subject.getName() == Subjects.CREDIT) {
            return subject.getMarks().contains(0);
        }
        if (subject.getMarks().contains(2)) return true;

        if (subject.getName() == Subjects.DIFFERENTIATED) {
            return false;
        }
        return subject.getMarks().contains(3);
    }

    private boolean fullExcellentSubject(Subject subject) {
        return subject.getMarks().stream().allMatch(mark -> mark == 5);
    }
}