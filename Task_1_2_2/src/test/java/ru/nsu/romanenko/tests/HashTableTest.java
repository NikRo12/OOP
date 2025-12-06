package ru.nsu.romanenko.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.HashTable;
import ru.nsu.romanenko.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class HashTableTest {

    private HashTable<String, Integer> hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new HashTable<>();
    }

    @Test
    void testPutAndGet() {
        assertNull(hashTable.put("key1", 1));
        assertEquals(1, hashTable.get("key1"));
        assertEquals(1, hashTable.getSize());

        assertEquals(1, hashTable.put("key1", 2));
        assertEquals(2, hashTable.get("key1"));
        assertEquals(1, hashTable.getSize());
    }

    @Test
    void testPutNullKey() {
        assertThrows(NullPointerException.class, () -> hashTable.put(null, 1));
    }

    @Test
    void testGetNonExistingAndNullKey() {
        assertNull(hashTable.get("nonexistent"));
        assertNull(hashTable.get(null));
    }

    @Test
    void testRemove() {
        hashTable.put("key1", 1);
        hashTable.put("key2", 2);

        assertEquals(1, hashTable.remove("key1"));
        assertNull(hashTable.get("key1"));
        assertEquals(2, hashTable.get("key2"));
        assertEquals(1, hashTable.getSize());

        assertNull(hashTable.remove("nonexistent"));
    }

    @Test
    void testContainsKey() {
        hashTable.put("key1", 1);
        assertTrue(hashTable.containsKey("key1"));
        assertFalse(hashTable.containsKey("nonexistent"));
    }

    @Test
    void testUpdate() {
        hashTable.put("key1", 1);
        assertEquals(1, hashTable.update("key1", 2));
        assertEquals(2, hashTable.get("key1"));
    }

    @Test
    void testIterator() {
        hashTable.put("key1", 1);
        hashTable.put("key2", 2);

        Set<String> keys = new HashSet<>();
        for (Entry<String, Integer> entry : hashTable) {
            keys.add(entry.getKey());
        }

        assertEquals(2, keys.size());
        assertTrue(keys.contains("key1"));
        assertTrue(keys.contains("key2"));
    }

    @Test
    void testIteratorConcurrentModification() {
        hashTable.put("key1", 1);
        Iterator<Entry<String, Integer>> iterator = hashTable.iterator();
        hashTable.put("key2", 2);
        assertThrows(ConcurrentModificationException.class, iterator::next);
    }

    @Test
    void testEqualsAndHashCode() {
        hashTable.put("key1", 1);
        hashTable.put("key2", 2);

        HashTable<String, Integer> other = new HashTable<>();
        other.put("key1", 1);
        other.put("key2", 2);

        assertTrue(hashTable.equals(other));
        assertEquals(hashTable.hashCode(), other.hashCode());

        other.put("key3", 3);
        assertFalse(hashTable.equals(other));
    }

    @Test
    void testToString() {
        assertEquals("{}", hashTable.toString());

        hashTable.put("key1", 1);
        String result = hashTable.toString();
        assertTrue(result.contains("key1=1"));
    }

    @Test
    void testEntrySet() {
        hashTable.put("key1", 1);
        hashTable.put("key2", 2);

        Set<Entry<String, Integer>> entrySet = hashTable.entrySet();
        assertEquals(2, entrySet.size());
    }

    @Test
    void testEmptyTable() {
        assertEquals(0, hashTable.getSize());
        assertFalse(hashTable.iterator().hasNext());
        assertEquals("{}", hashTable.toString());
    }

    @Test
    void testMultipleOperations() {
        // Комплексный тест
        hashTable.put("a", 1);
        hashTable.put("b", 2);
        hashTable.put("c", 3);
        assertEquals(3, hashTable.getSize());

        hashTable.remove("b");
        assertEquals(2, hashTable.getSize());
        assertNull(hashTable.get("b"));

        hashTable.put("a", 10);
        assertEquals(10, hashTable.get("a"));
        assertEquals(2, hashTable.getSize());
    }
}