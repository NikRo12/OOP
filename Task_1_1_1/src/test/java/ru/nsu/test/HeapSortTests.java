package ru.nsu.test;

import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.HeapSort;

import static org.junit.jupiter.api.Assertions.*;

public class HeapSortTests {

    @Test
    public void testHeapSortWithSortedArray() {
        int[] arr = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};
        HeapSort.heapSort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithReverseSortedArray() {
        int[] arr = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};
        HeapSort.heapSort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithUnsortedArray() {
        int[] arr = {3, 1, 4, 2, 5};
        int[] expected = {1, 2, 3, 4, 5};
        HeapSort.heapSort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithDuplicateValues() {
        int[] arr = {5, 2, 5, 1, 2};
        int[] expected = {1, 2, 2, 5, 5};
        HeapSort.heapSort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithSingleElement() {
        int[] arr = {42};
        int[] expected = {42};
        HeapSort.heapSort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithEmptyArray() {
        int[] arr = {};
        int[] expected = {};
        HeapSort.heapSort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithNegativeNumbers() {
        int[] arr = {-3, -1, -4, -2, -5};
        int[] expected = {-5, -4, -3, -2, -1};
        HeapSort.heapSort(arr);
        assertArrayEquals(expected, arr);
    }
}