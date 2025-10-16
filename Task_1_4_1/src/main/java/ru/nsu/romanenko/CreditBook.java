package ru.nsu.romanenko;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CreditBook {
    private final String studentName;
    private final boolean budget;
    private int currentSemester;
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
        float totalSum = 0;
        int totalMarksCount = 0;

        for (HashSet<Subject> semesterSubjects : eachSemester.values()) {
            for (Subject subject : semesterSubjects) {
                if (isSubjectExamDiscipline(subject)) {
                    for (int mark : subject.getMarks()) {
                        totalSum += mark;
                        totalMarksCount++;
                    }
                }
            }
        }

        return totalMarksCount > 0 ? totalSum / totalMarksCount : 0;
    }

    public boolean canTransferToBudget() {
        if (budget) {
            return true;
        }

        if (currentSemester < 2) {
            return false;
        }

        for (int i = currentSemester; i >= currentSemester - 1; i--) {
            for (Subject subject : eachSemester.get(i)) {
                if ((isSubjectExamDiscipline(subject) ||
                        subject.getName() == Subjects.CREDIT) && hasUnsatisfactoryMarks(subject)) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean canGetRedDiploma() {
        int marksCount = 0;
        int marksExcellent = 0;

        for (Subject subject : eachSemester.get(currentSemester)) {
            if((isSubjectExamDiscipline(subject) ||
                    subject.getName() == Subjects.CREDIT) && hasUnsatisfactoryMarks(subject) ||
                    (subject.getName() == Subjects.DIFFERENTIATED && subject.getMarks().contains(3)))
            {
                return false;
            }

            if (subject.getName() == Subjects.COURSE) {
                if (!subject.getMarks().contains(5) && !subject.getMarks().isEmpty()) {
                    return false;
                }
            } else if (isSubjectExamDiscipline(subject)) {
                for(int mark : subject.getMarks())
                {
                    marksCount++;
                    if(mark == 5)
                    {
                        marksExcellent++;
                    }
                }
            }
        }

        return marksCount > 0 &&
                (double) marksExcellent / marksCount >= 0.75;
    }

    public boolean canGetIncreasedScholarship() {
        if (!budget || currentSemester == 1) {
            return false;
        }

        int previousSemester = currentSemester - 1;

        for (Subject subject : eachSemester.get(previousSemester)) {
            if(subject.getName() == Subjects.CREDIT && hasUnsatisfactoryMarks(subject))
            {
                return false;
            }

            else if(isSubjectExamDiscipline(subject) && !fullExcellentSubject(subject))
            {
                return false;
            }
        }

        return true;
    }

    public void addMark(int semester, Subjects subjectName, int mark) {
        for (Subject subject : eachSemester.get(semester)) {
            if (subject.getName() == subjectName) {
                subject.addMark(mark);
                return;
            }
        }
    }

    public void evaluateSemester() {
        if (currentSemester < 8) {
            currentSemester++;
        }
    }

    public String getStudentName() {
        return studentName;
    }

    public boolean isBudget() {
        return budget;
    }

    public int getCurrentSemester() {
        return currentSemester;
    }

    public Map<Integer, HashSet<Subject>> getEachSemester() {
        return eachSemester;
    }

    private boolean isSubjectExamDiscipline(Subject subject) {
        return subject.getName().isExamDiscipline();
    }

    private boolean hasUnsatisfactoryMarks(Subject subject) {
        if (subject.getName() == Subjects.CREDIT) {
            return subject.getMarks().contains(0);
        } else if (subject.getName() == Subjects.EXAM) {
            return subject.getMarks().contains(3) || subject.getMarks().contains(2);
        } else if (subject.getName() == Subjects.DIFFERENTIATED) {
            return subject.getMarks().contains(2);
        }

        return subject.getMarks().contains(3) || subject.getMarks().contains(2);
    }

    private boolean fullExcellentSubject(Subject subject)
    {
        return subject.getMarks().stream().allMatch(mark -> mark == 5);
    }
}