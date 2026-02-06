package ru.nsu.romanenko;

import java.util.concurrent.atomic.AtomicBoolean;

import static ru.nsu.romanenko.Solution.isPrime;

public class ConcurrentThread implements Solution{
    private final int countThreads;

    public ConcurrentThread(int countThreads) {
        this.countThreads = countThreads;
    }

    public boolean answer(int[] arr) {
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
                try {
                    t.join();
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

        return foundNotPrime.get();
    }
}
