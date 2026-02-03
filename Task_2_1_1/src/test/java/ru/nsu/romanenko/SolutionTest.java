package ru.nsu.romanenko;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class SolutionTest {

    @Test
    @DisplayName("Должен возвращать false, если массив состоит только из простых чисел")
    void testAllPrimes() throws InterruptedException {
        int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29};

        assertFalse(Solution.consistently(primes));
        assertFalse(Solution.concurrentThread(primes, 4));
        assertFalse(Solution.concurrencyStream(primes));
    }

    @Test
    @DisplayName("Должен возвращать true, если в массиве есть составное число")
    void testContainsNotPrime() throws InterruptedException {
        int[] mixed = {2, 3, 5, 8, 11};

        assertTrue(Solution.consistently(mixed));
        assertTrue(Solution.concurrentThread(mixed, 2));
        assertTrue(Solution.concurrencyStream(mixed));
    }

    @Test
    @DisplayName("Крайний случай: составное число в самом конце")
    void testNotPrimeAtEnd() throws InterruptedException {
        int[] arr = {2, 3, 5, 7, 9};

        assertTrue(Solution.concurrentThread(arr, 3));
    }

    @Test
    @DisplayName("Крайний случай: пустой массив")
    void testEmptyArray() throws InterruptedException {
        int[] empty = {};

        assertFalse(Solution.consistently(empty));
        assertFalse(Solution.concurrentThread(empty, 4));
        assertFalse(Solution.concurrencyStream(empty));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 4, 8, 100})
    @DisplayName("Проверка разного количества потоков")
    void testThreadCounts(int threads) throws InterruptedException {
        int[] arr = {2, 3, 5, 7, 10, 11};
        assertTrue(Solution.concurrentThread(arr, threads));
    }
}