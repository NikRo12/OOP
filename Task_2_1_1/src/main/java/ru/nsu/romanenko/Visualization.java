package ru.nsu.romanenko;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import org.knowm.xchart.*;

public class Visualization {
    public static void visualize() throws InterruptedException {
        int size = 100000;
        int[] arr = createTestArray(size);

        int[] threadCounts = {1, 2, 4, 8, 16};
        double[] executionTimes = new double[threadCounts.length + 2];

        Instant start = Instant.now();
        boolean seqResult = Solution.consistently(arr);
        Instant end = Instant.now();
        executionTimes[0] = Duration.between(start, end).toNanos() / 1_000_000.0;

        for (int i = 0; i < threadCounts.length; i++) {
            int threads = threadCounts[i];
            Solution.concurrentThread(arr, threads);

            start = Instant.now();
            boolean threadResult = Solution.concurrentThread(arr, threads);
            end = Instant.now();

            if (threadResult != seqResult) {
                System.out.println("Ошибка: разные результаты!");
            }

            executionTimes[i + 1] = Duration.between(start, end).toNanos() / 1_000_000.0;
        }

        Solution.concurrencyStream(arr);

        start = Instant.now();
        boolean streamResult = Solution.concurrencyStream(arr);
        end = Instant.now();

        if (streamResult != seqResult) {
            System.out.println("Ошибка: разные результаты!");
        }

        executionTimes[executionTimes.length - 1] = Duration.between(start, end).toNanos() / 1_000_000.0;
        createChart(threadCounts, executionTimes);
    }

    private static int[] createTestArray(int size) {
        int[] arr = new int[size];
        int primeCount = (int) (size * 0.95);

        int num = 2;
        for (int i = 0; i < primeCount; i++) {
            while (!isPrime(num)) {
                num++;
            }
            arr[i] = num;
            num++;
        }

        for (int i = primeCount; i < size; i++) {
            arr[i] = (i - primeCount + 1) * 2 + 1000000;
        }

        return arr;
    }

    private static boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;

        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    private static void createChart(int[] threadCounts, double[] executionTimes) {
        String[] categories = new String[threadCounts.length + 2];
        categories[0] = "Последовательно";

        for (int i = 0; i < threadCounts.length; i++) {
            categories[i + 1] = threadCounts[i] + " потоков";
        }
        categories[categories.length - 1] = "parallelStream";

        CategoryChart chart = new CategoryChartBuilder()
                .width(1000)
                .height(600)
                .title("Сравнение времени выполнения разных реализаций")
                .xAxisTitle("Реализация")
                .yAxisTitle("Время выполнения (мс)")
                .build();

        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setXAxisLabelRotation(45);
        chart.getStyler().setPlotGridLinesVisible(false);

        chart.addSeries("Время выполнения", Arrays.asList(categories),
                Arrays.stream(executionTimes).boxed().toList());

        new SwingWrapper<>(chart).displayChart();
    }
}

