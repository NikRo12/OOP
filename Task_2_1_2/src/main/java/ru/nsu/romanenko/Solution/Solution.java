package ru.nsu.romanenko.Solution;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class Solution {
    public static boolean consistently(int[] arr) {
        for (int num : arr) {
            if (!isPrime(num)) {
                return true;
            }
        }
        return false;
    }

    public static boolean concurrentThread(int[] arr, int countThreads) throws InterruptedException {
        AtomicBoolean foundNotPrime = new AtomicBoolean(false);
        Thread[] threads = new Thread[countThreads];

        int chunkSize = arr.length / countThreads;
        if (chunkSize == 0) chunkSize = 1;

        for (int i = 0; i < countThreads; i++) {
            final int threadId = i;
            final int start = i * chunkSize;
            final int end = (i == countThreads - 1) ? arr.length : Math.min(arr.length, start + chunkSize);

            if (start >= arr.length) break;

            threads[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    if (foundNotPrime.get() || Thread.currentThread().isInterrupted()) {
                        return;
                    }
                    if (!isPrime(arr[j])) {
                        foundNotPrime.set(true);
                        for (int k = 0; k < threads.length; k++) {
                            if (k != threadId && threads[k] != null && threads[k].isAlive()) {
                                threads[k].interrupt();
                            }
                        }
                        return;
                    }
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            if (t != null) {
                t.join();
            }
        }

        return foundNotPrime.get();
    }

    public static boolean concurrencyStream(int[] arr) {
        return Arrays.stream(arr)
                .parallel()
                .anyMatch(n -> !isPrime(n));
    }

    private static boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;

        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
}