package ru.nsu.romanenko.Abstractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.jupiter.api.Assertions.*;

class BlockingQueueTest {

    private static class TestQueue extends BlockingQueue<Integer> {
        public TestQueue(int capacity) {
            super(capacity);
        }
    }

    private TestQueue queue;

    @BeforeEach
    void setUp() {
        queue = new TestQueue(2);
    }

    @Test
    void testPutAndGet() throws InterruptedException {
        queue.put(1);
        queue.put(2);

        assertEquals(1, queue.get());
        assertEquals(2, queue.get());
    }

    @Test
    void testBlockingPut() throws InterruptedException {
        queue.put(1);
        queue.put(2);

        Thread producer = new Thread(() -> {
            try {
                queue.put(3);
            } catch (InterruptedException ignored) {}
        });

        producer.start();
        Thread.sleep(100);
        assertTrue(producer.isAlive());

        queue.get();
        producer.join(500);
        assertFalse(producer.isAlive());
    }

    @Test
    void testBlockingGet() throws InterruptedException {
        AtomicReference<Integer> result = new AtomicReference<>();
        Thread consumer = new Thread(() -> {
            try {
                result.set(queue.get());
            } catch (InterruptedException ignored) {}
        });

        consumer.start();
        Thread.sleep(100);
        assertTrue(consumer.isAlive());

        queue.put(42);
        consumer.join(500);
        assertEquals(42, result.get());
    }

    @Test
    void testSignalAllReturnsNullForGet() throws InterruptedException {
        Thread consumer = new Thread(() -> {
            try {
                assertNull(queue.get());
            } catch (InterruptedException ignored) {}
        });

        consumer.start();
        queue.signalAll();
        consumer.join(500);
    }

    @Test
    void testSignalAllStopsPut() throws InterruptedException {
        queue.signalAll();
        queue.put(10);
        assertNull(queue.get());
    }
}