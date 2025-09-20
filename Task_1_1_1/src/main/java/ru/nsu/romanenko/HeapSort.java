package ru.nsu.romanenko;

/**
 * Класс реализующий пирамидальную сортировку
 */
public class HeapSort {
    private HeapSort(){}

    /**
     * Сортирует массив с помощью пирамидальной сортировки.
     *
     * @param arr массив для сортировки
     */
    public static void heapSort(int[] arr) {
        int n = arr.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        for (int i = n - 1; i >= 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapify(arr, i, 0);
        }
    }

    /**
     * Сортирует массив с помощью пирамидальной сортировки, не изменяя исходный
     *
     * @param arr массив для сортировки
     */
    public static int[] saveHeapSort(int[] arr) {
        int n = arr.length;

        int[] result_arr = new int[n];

        System.arraycopy(arr, 0, result_arr, 0, n);

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(result_arr, n, i);
        }

        for (int i = n - 1; i >= 0; i--) {
            int temp = result_arr[0];
            result_arr[0] = result_arr[i];
            result_arr[i] = temp;

            heapify(result_arr, i, 0);
        }

        return result_arr;
    }

    /**
     * Преобразует поддерево в кучу.
     *
     * @param arr массив
     * @param n   размер кучи
     * @param i   корневой узел
     */
    private static void heapify(int[] arr, int n, int i) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && arr[l] > arr[largest]) {
            largest = l;
        }

        if (r < n && arr[r] > arr[largest]) {
            largest = r;
        }

        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            heapify(arr, n, largest);
        }
    }

    /**
     * Выводит массив на экран.
     *
     * @param arr массив для вывода
     */
    public static void printArray(int[] arr) {
        for (int j : arr) {
            System.out.println(j);
        }
    }
}
