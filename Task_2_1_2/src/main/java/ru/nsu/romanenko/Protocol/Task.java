package ru.nsu.romanenko.Protocol;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;

public class Task implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /*
    Part of input slice
     */
    @Getter
    private final int[] numbers;

    @Getter
    private final int taskID;

    public Task(int[] numbers, int taskID) {
        this.numbers = numbers;
        this.taskID = taskID;
    }
}
