package ru.nsu.romanenko;

public class Consistently implements Solution{
    public Consistently() {}

    public boolean answer(int[] arr) {
        for (int num : arr) {
            if (!Solution.isPrime(num)) {
                return true;
            }
        }
        return false;
    }
}
