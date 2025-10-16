package ru.nsu.romanenko;

import java.util.ArrayList;

public class Subject {
    private final Subjects name;
    private final int count;
    private final ArrayList<Integer> marks;

    public Subject(Subjects name, int count)
    {
        this.name = name;
        this.count = count;
        this.marks = new ArrayList<>();
    }

    public Subjects getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public ArrayList<Integer> getMarks() {
        return new ArrayList<>(marks);
    }

    public void addMark(int mark)
    {
        marks.add(mark);
    }

    public int getCurrentCount()
    {
        return marks.size();
    }
}