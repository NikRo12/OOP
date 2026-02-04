package ru.nsu.romanenko.Protocol;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

public class Result implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    private final boolean result;

    @Getter
    private final int taskID;

    public Result(boolean result, int taskID) {
        this.result = result;
        this.taskID = taskID;
    }
}
