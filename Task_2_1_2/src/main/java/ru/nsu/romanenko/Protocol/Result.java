package ru.nsu.romanenko.Protocol;

import java.io.Serial;
import java.io.Serializable;

public record Result(boolean foundNotPrime, int taskID) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

}
