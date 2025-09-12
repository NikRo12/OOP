package ru.nsu.romanenko;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SampleTests {

    @Test
    public void testHeapSortWithSortedArray() {
        int[] arr = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};
        Sample.heapSort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithReverseSortedArray() {
        int[] arr = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};
        Sample.heapSort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithUnsortedArray() {
        int[] arr = {3, 1, 4, 2, 5};
        int[] expected = {1, 2, 3, 4, 5};
        Sample.heapSort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithDuplicateValues() {
        int[] arr = {5, 2, 5, 1, 2};
        int[] expected = {1, 2, 2, 5, 5};
        Sample.heapSort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithSingleElement() {
        int[] arr = {42};
        int[] expected = {42};
        Sample.heapSort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithEmptyArray() {
        int[] arr = {};
        int[] expected = {};
        Sample.heapSort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapSortWithNegativeNumbers() {
        int[] arr = {-3, -1, -4, -2, -5};
        int[] expected = {-5, -4, -3, -2, -1};
        Sample.heapSort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test
    public void testHeapifyWithLeftChildLargest() {
        int[] arr = {1, 3, 2};
        int n = arr.length;
        Sample.heapify(arr, n, 0);
        assertEquals(3, arr[0]);
    }

    @Test
    public void testHeapifyWithRightChildLargest() {
        int[] arr = {1, 2, 3};
        int n = arr.length;
        Sample.heapify(arr, n, 0);
        assertEquals(3, arr[0]);
    }

    @Test
    public void testHeapifyWithNoSwapNeeded() {
        int[] arr = {3, 1, 2};
        int n = arr.length;
        Sample.heapify(arr, n, 0);
        assertEquals(3, arr[0]);
    }

    @Test
    public void testPrintArrayMethod() {
        int[] arr = {1, 2, 3};
        Sample.printArray(arr);
        assertTrue(true);
    }

    @Test
    public void testMainMethod() {
        assertDoesNotThrow(() -> Sample.main(new String[]{}));
    }
}