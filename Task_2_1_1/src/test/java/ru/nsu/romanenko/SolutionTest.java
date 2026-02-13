package ru.nsu.romanenko;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class SolutionTest {

    @Test
    @DisplayName("Должен возвращать false, если массив состоит только из простых чисел")
    void testAllPrimes() {
        int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29};

        assertFalse(new Consistently().answer(primes));
        assertFalse(new ConcurrentThread(4).answer(primes));
        assertFalse(new ConcurrencyStream().answer(primes));
    }

    @Test
    @DisplayName("Должен возвращать true, если в массиве есть составное число")
    void testContainsNotPrime() {
        int[] mixed = {2, 3, 5, 8, 11};

        assertTrue(new Consistently().answer(mixed));
        assertTrue(new ConcurrentThread(2).answer(mixed));
        assertTrue(new ConcurrencyStream().answer(mixed));
    }

    @Test
    @DisplayName("Крайний случай: составное число в самом конце")
    void testNotPrimeAtEnd() {
        int[] arr = {2, 3, 5, 7, 9};

        assertTrue(new ConcurrentThread(3).answer(arr));
    }

    @Test
    @DisplayName("Крайний случай: пустой массив")
    void testEmptyArray() {
        int[] empty = {};

        assertFalse(new Consistently().answer(empty));
        assertFalse(new ConcurrentThread(4).answer(empty));
        assertFalse(new ConcurrencyStream().answer(empty));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 4, 8, 100})
    @DisplayName("Проверка разного количества потоков")
    void testThreadCounts(int threads) {
        int[] arr = {2, 3, 5, 7, 10, 11};
        assertTrue(new ConcurrentThread(threads).answer(arr));
    }
}