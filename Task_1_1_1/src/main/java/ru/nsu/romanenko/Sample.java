package ru.nsu.romanenko;

/**
 * Класс для демонстрации сортировки кучей.
 */
public class Sample {

    /**
     * Основной метод программы.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        int[] arr = {5, 4, 3, 2, 1};

        heapSort(arr);

        printArray(arr);
    }

    /**
     * Сортирует массив с помощью пирамидальной сортировки.
     *
     * @param arr массив для сортировки
     */
    static void heapSort(int[] arr) {
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
     * Преобразует поддерево в кучу.
     *
     * @param arr массив
     * @param n   размер кучи
     * @param i   корневой узел
     */
    static void heapify(int[] arr, int n, int i) {
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
    static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }
}