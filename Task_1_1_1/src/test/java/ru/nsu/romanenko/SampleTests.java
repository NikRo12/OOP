package ru.nsu.romanenko;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SampleTests {

    @Test
    public void testHeapSortWithSortedArray() {
        int[] arr = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};

        Sample.HeapSort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithReverseSortedArray() {
        int[] arr = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};

        Sample.HeapSort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithUnsortedArray() {
        int[] arr = {3, 1, 4, 2, 5};
        int[] expected = {1, 2, 3, 4, 5};

        Sample.HeapSort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithDuplicateValues() {
        int[] arr = {5, 2, 5, 1, 2};
        int[] expected = {1, 2, 2, 5, 5};

        Sample.HeapSort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithSingleElement() {
        int[] arr = {42};
        int[] expected = {42};

        Sample.HeapSort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithEmptyArray() {
        int[] arr = {};
        int[] expected = {};

        Sample.HeapSort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithNegativeNumbers() {
        int[] arr = {-3, -1, -4, -2, -5};
        int[] expected = {-5, -4, -3, -2, -1};

        Sample.HeapSort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithMixedNumbers() {
        int[] arr = {-2, 5, -1, 0, 3};
        int[] expected = {-2, -1, 0, 3, 5};

        Sample.HeapSort(arr);

        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapifyMethod() {
        int[] arr = {3, 1, 4};
        int n = arr.length;

        Sample.heapify(arr, n, 0);

        assertEquals(4, arr[0]);
    }

    @Test
    public void testPrintArrayMethod() {
        int[] arr = {1, 2, 3};

        Sample.print_array(arr);

        assertTrue(true);
    }
}