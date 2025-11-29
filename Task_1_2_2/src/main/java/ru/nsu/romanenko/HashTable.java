package ru.nsu.romanenko;

import java.util.*;

public class HashTable<K, V> implements Iterable<Entry<K, V>> {

    private ArrayList<LinkedList<Entry<K, V>>> buckets;
    private int size;
    private int modCount;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;

    public HashTable() {
        this(DEFAULT_CAPACITY);
    }

    public HashTable(int initialCapacity) {
        this.size = 0;
        this.modCount = 0;
        initializeBuckets(initialCapacity);
        this.threshold = (int) (initialCapacity * LOAD_FACTOR);
    }

    public V put(K key, V value) {
        if (key == null)
            throw new NullPointerException();

        if (size >= threshold) {
            resize();
        }

        int hash = hash(key);
        int index = getIndex(hash);
        LinkedList<Entry<K, V>> bucket = buckets.get(index);

        for (Entry<K, V> entry : bucket) {
            if (entry.hash == hash && Objects.equals(entry.key, key)) {
                V oldValue = entry.value;
                entry.value = value;
                return oldValue;
            }
        }

        bucket.add(new Entry<>(key, value, hash));
        size++;
        modCount++;

        return null;
    }

    public V remove(K key) {
        if (key == null) return null;

        int hash = hash(key);
        int index = getIndex(hash);
        LinkedList<Entry<K, V>> bucket = buckets.get(index);

        for (int i = 0; i < bucket.size(); i++) {
            Entry<K, V> entry = bucket.get(i);
            if (entry.hash == hash && Objects.equals(entry.key, key)) {
                V oldValue = entry.value;
                bucket.remove(i);
                size--;
                modCount++;
                return oldValue;
            }
        }
        return null;
    }

    public V get(K key) {
        if (key == null) return null;

        int hash = hash(key);
        int index = getIndex(hash);
        LinkedList<Entry<K, V>> bucket = buckets.get(index);

        for (Entry<K, V> entry : bucket) {
            if (entry.hash == hash && Objects.equals(entry.key, key)) {
                return entry.value;
            }
        }

        return null;
    }

    public V update(K key, V value) {
        return put(key, value);
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashTable<?, ?> that)) return false;

        if (this.size != that.size) return false;

        return this.entrySet().equals(that.entrySet());
    }

    @Override
    public int hashCode() {
        return entrySet().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        boolean first = true;
        for (LinkedList<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(entry.key).append("=").append(entry.value);
                first = false;
            }
        }

        sb.append("}");
        return sb.toString();
    }

    public int getSize() {
        return size;
    }

    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entries = new HashSet<>();
        for (Entry<K, V> entry : this) {
            entries.add(entry);
        }
        return entries;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashTableIterator();
    }

    private class HashTableIterator implements Iterator<Entry<K, V>> {
        private final int expectedModCount = modCount;
        private int currentBucket = 0;
        private Iterator<Entry<K, V>> currentIterator;
        private int elementsReturned = 0;

        HashTableIterator() {
            currentIterator = buckets.get(0).iterator();
            findNextBucket();
        }

        private void findNextBucket() {
            while (!currentIterator.hasNext() && currentBucket < buckets.size() - 1) {
                currentBucket++;
                currentIterator = buckets.get(currentBucket).iterator();
            }
        }

        @Override
        public boolean hasNext() {
            checkForModification();
            return elementsReturned < size;
        }

        @Override
        public Entry<K, V> next() {
            checkForModification();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Entry<K, V> result = currentIterator.next();
            elementsReturned++;

            if (!currentIterator.hasNext()) {
                findNextBucket();
            }

            return result;
        }

        private void checkForModification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private void initializeBuckets(int capacity) {
        buckets = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            buckets.add(new LinkedList<>());
        }
    }

    private int hash(K key) {
        if (key == null) return 0;
        return key.hashCode();
    }

    private int getIndex(int hash) {
        return Math.floorMod(hash, buckets.size());
    }

    private void resize() {
        int newCapacity = buckets.size() * 2;
        ArrayList<LinkedList<Entry<K, V>>> oldBuckets = buckets;

        initializeBuckets(newCapacity);
        threshold = (int) (newCapacity * LOAD_FACTOR);
        size = 0;

        for (LinkedList<Entry<K, V>> bucket : oldBuckets) {
            for (Entry<K, V> entry : bucket) {
                int newIndex = getIndex(entry.hash);
                buckets.get(newIndex).add(entry);
                size++;
            }
        }
    }
}