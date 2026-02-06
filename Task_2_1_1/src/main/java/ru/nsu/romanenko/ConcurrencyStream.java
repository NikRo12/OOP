package ru.nsu.romanenko;

import java.util.Arrays;

public class ConcurrencyStream implements Solution{
    public ConcurrencyStream() {}

    public boolean answer(int[] arr) {
        return Arrays.stream(arr)
                .parallel()
                .anyMatch(n -> !Solution.isPrime(n));
    }
}
