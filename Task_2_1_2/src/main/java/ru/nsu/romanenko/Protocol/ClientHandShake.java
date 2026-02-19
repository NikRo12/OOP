package ru.nsu.romanenko.Protocol;

import java.io.Serial;
import java.io.Serializable;

public record ClientHandShake(int[] data) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

}
