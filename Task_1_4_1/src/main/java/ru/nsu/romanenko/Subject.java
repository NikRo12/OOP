package ru.nsu.romanenko;

import lombok.Getter;
import java.util.ArrayList;

@Getter
public class Subject {
    private final Subjects name;
    private final int count;

    @Getter(lombok.AccessLevel.NONE)
    private final ArrayList<Integer> marks;

    public Subject(Subjects name, int count) {
        this.name = name;
        this.count = count;
        this.marks = new ArrayList<>();
    }

    public ArrayList<Integer> getMarks() {
        return new ArrayList<>(marks);
    }

    public void addMark(int mark) {
        marks.add(mark);
    }

    public int getCurrentCount() {
        return marks.size();
    }
}