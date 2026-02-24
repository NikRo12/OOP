package ru.nsu.romanenko.Abstractions;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public abstract class BlockingQueue<T> implements Storage<T>{
    private final int capacity;
    private final Queue<T> items;
    private final ReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;

    private boolean isShutdown = false;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
        this.items = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.notEmpty = this.lock.newCondition();
        this.notFull = this.lock.newCondition();
    }

    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            while (capacity == items.size() && !isShutdown) {
                notFull.await();
            }

            if (isShutdown) return;

            items.add(item);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T get() throws InterruptedException {
        lock.lock();
        try {
            while (items.isEmpty()) {
                if (isShutdown) return null;
                notEmpty.await();
            }

            T item = items.poll();
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }

    public void signalAll() {
        lock.lock();
        try {
            isShutdown = true;
            notEmpty.signalAll();
            notFull.signalAll();
        } finally {
            lock.unlock();
        }
    }
}