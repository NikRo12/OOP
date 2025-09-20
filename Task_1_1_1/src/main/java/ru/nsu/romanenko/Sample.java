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

        HeapSort.heapSort(arr);
        HeapSort.printArray(arr);

        int[] result;
        result = HeapSort.saveHeapSort(arr);
        HeapSort.printArray(result);
    }
}