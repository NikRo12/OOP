package ru.nsu.romanenko.test.graphs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import ru.nsu.romanenko.graphs.AbstractGraph;
import ru.nsu.romanenko.graphs.AdjacencyMatrix;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Set;

class AbstractGraphTest {

    private AbstractGraph graph;

    @BeforeEach
    void setUp() {
        graph = new AdjacencyMatrix();
    }

    @Test
    void testEmptyGraph() {
        assertEquals(0, graph.getVertexCount());
        assertEquals(0, graph.getEdgeCount());
        assertTrue(graph.getVertices().isEmpty());
    }

    @Test
    void testDeleteNonExistentVertex() {
        assertFalse(graph.deleteVertex(999));
    }

    @Test
    void testTopologicalSortMultipleValidOrders() {
        // Граф с несколькими валидными топологическими порядками
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 4);

        List<Integer> sorted = graph.topologicalSort();
        assertEquals(4, sorted.size());

        // Проверяем обязательные условия
        assertTrue(sorted.indexOf(1) < sorted.indexOf(2));
        assertTrue(sorted.indexOf(1) < sorted.indexOf(3));
        assertTrue(sorted.indexOf(2) < sorted.indexOf(4));
        assertTrue(sorted.indexOf(3) < sorted.indexOf(4));
    }

    @Test
    void testTopologicalSortDisconnectedGraph() {
        // Несвязный граф без циклов
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addEdge(1, 2);
        graph.addEdge(3, 4);

        List<Integer> sorted = graph.topologicalSort();
        assertEquals(4, sorted.size());

        // Проверяем порядок для компонент связности
        assertTrue(sorted.indexOf(1) < sorted.indexOf(2));
        assertTrue(sorted.indexOf(3) < sorted.indexOf(4));
    }

    @Test
    void testTopologicalSortSingleComponent() {
        graph.addVertex(1);

        List<Integer> sorted = graph.topologicalSort();
        assertEquals(1, sorted.size());
        assertEquals(1, sorted.getFirst());
    }

    @Test
    void testTopologicalSortWithSelfLoop() {
        graph.addVertex(1);
        graph.addEdge(1, 1); // Петля создает цикл

        assertThrows(IllegalStateException.class, () -> {
            graph.topologicalSort();
        });
    }

    @Test
    void testReadFromFileOnNonEmptyGraph() {
        graph.addVertex(1);

        // Попытка прочитать файл в непустой граф должна вернуть false
        assertFalse(graph.readFromFile("test.txt"));
    }

    @Test
    void testReadFromFileWithInvalidPath() {
        assertFalse(graph.readFromFile("non_existent_file.txt"));
    }

    @Test
    void testGraphEqualsWithDifferentSizes() {
        AbstractGraph graph1 = new AdjacencyMatrix();
        AbstractGraph graph2 = new AdjacencyMatrix();

        graph1.addVertex(1);
        graph1.addVertex(2);

        graph2.addVertex(1);

        assertNotEquals(graph1, graph2);
        assertNotEquals(graph2, graph1);
    }

    @Test
    void testGraphEqualsWithDifferentEdges() {
        AbstractGraph graph1 = new AdjacencyMatrix();
        AbstractGraph graph2 = new AdjacencyMatrix();

        graph1.addVertex(1);
        graph1.addVertex(2);
        graph1.addEdge(1, 2);

        graph2.addVertex(1);
        graph2.addVertex(2);
        // Нет ребра

        assertNotEquals(graph1, graph2);
    }

    @Test
    void testGetVerticesImmutability() {
        graph.addVertex(1);
        graph.addVertex(2);

        Set<Integer> vertices = graph.getVertices();
        assertEquals(2, vertices.size());

        // Изменение возвращенного множества не должно влиять на граф
        vertices.clear();
        assertEquals(2, graph.getVertexCount());
    }

    @Test
    void testEdgeCasesForHasVertex() {
        assertFalse(graph.hasVertex(0));
        assertFalse(graph.hasVertex(-1));
        assertFalse(graph.hasVertex(Integer.MAX_VALUE));
        assertFalse(graph.hasVertex(Integer.MIN_VALUE));

        graph.addVertex(0);
        assertTrue(graph.hasVertex(0));

        graph.addVertex(-1);
        assertTrue(graph.hasVertex(-1));
    }

    @Test
    void testStressTestAddRemoveVertices() {
        // Тест на производительность и корректность при множественных операциях
        for (int i = 0; i < 100; i++) {
            assertTrue(graph.addVertex(i));
        }
        assertEquals(100, graph.getVertexCount());

        for (int i = 0; i < 100; i += 2) {
            assertTrue(graph.removeVertex(i));
        }
        assertEquals(50, graph.getVertexCount());

        for (int i = 1; i < 100; i += 2) {
            assertTrue(graph.hasVertex(i));
        }
    }

    @Test
    void testStressTestAddRemoveEdges() {
        // Добавляем вершины
        for (int i = 1; i <= 10; i++) {
            graph.addVertex(i);
        }

        // Добавляем рёбра
        int edgeCount = 0;
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (i != j && graph.addEdge(i, j)) {
                    edgeCount++;
                }
            }
        }

        assertEquals(edgeCount, graph.getEdgeCount());

        // Удаляем половину рёбер
        int removedCount = 0;
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (i != j && graph.removeEdge(i, j)) {
                    removedCount++;
                }
            }
        }

        assertEquals(edgeCount - removedCount, graph.getEdgeCount());
    }

    @Test
    void testComplexTopologicalSortScenarios() {
        // Тест различных сценариев топологической сортировки

        // Сценарий 1: Длинная цепочка
        AbstractGraph chain = new AdjacencyMatrix();
        for (int i = 1; i <= 5; i++) {
            chain.addVertex(i);
        }
        for (int i = 1; i < 5; i++) {
            chain.addEdge(i, i + 1);
        }

        List<Integer> chainSorted = chain.topologicalSort();
        for (int i = 0; i < chainSorted.size() - 1; i++) {
            assertTrue(chainSorted.indexOf(i + 1) < chainSorted.indexOf(i + 2));
        }

        // Сценарий 2: Звезда
        AbstractGraph star = new AdjacencyMatrix();
        star.addVertex(0);
        for (int i = 1; i <= 4; i++) {
            star.addVertex(i);
            star.addEdge(0, i);
        }

        List<Integer> starSorted = star.topologicalSort();
        assertEquals(0, starSorted.indexOf(0));
    }

    @Test
    void testTopologicalSortComplexDependencies() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addVertex(5);

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);

        List<Integer> sorted = graph.topologicalSort();
        assertEquals(5, sorted.size());

        // Проверяем порядок зависимостей
        assertTrue(sorted.indexOf(1) < sorted.indexOf(2));
        assertTrue(sorted.indexOf(1) < sorted.indexOf(3));
        assertTrue(sorted.indexOf(2) < sorted.indexOf(4));
        assertTrue(sorted.indexOf(3) < sorted.indexOf(4));
        assertTrue(sorted.indexOf(4) < sorted.indexOf(5));
    }

    @Test
    void testTopologicalSortWithIsolatedVertices() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        // Вершина 3 изолирована

        List<Integer> sorted = graph.topologicalSort();
        assertEquals(3, sorted.size());
        assertTrue(sorted.indexOf(1) < sorted.indexOf(2));
        // Вершина 3 может быть в любой позиции относительно 1 и 2
    }

    @Test
    void testGraphEqualsWithDifferentVertexOrder() {
        AbstractGraph graph1 = new AdjacencyMatrix();
        AbstractGraph graph2 = new AdjacencyMatrix();

        // Добавляем вершины в разном порядке
        graph1.addVertex(1);
        graph1.addVertex(2);
        graph1.addEdge(1, 2);

        graph2.addVertex(2);
        graph2.addVertex(1);
        graph2.addEdge(1, 2);

        // Графы должны быть равны независимо от порядка добавления вершин
        assertEquals(graph1, graph2);
    }

    @Test
    void testGraphEqualsReflexivity() {
        assertEquals(graph, graph);
    }

    @Test
    void testGraphEqualsSymmetry() {
        AbstractGraph graph1 = new AdjacencyMatrix();
        AbstractGraph graph2 = new AdjacencyMatrix();

        graph1.addVertex(1);
        graph2.addVertex(1);

        assertEquals(graph1.equals(graph2), graph2.equals(graph1));
    }

    @Test
    void testGraphEqualsTransitivity() {
        AbstractGraph graph1 = new AdjacencyMatrix();
        AbstractGraph graph2 = new AdjacencyMatrix();
        AbstractGraph graph3 = new AdjacencyMatrix();

        graph1.addVertex(1);
        graph2.addVertex(1);
        graph3.addVertex(1);

        assertEquals(graph1, graph2);
        assertEquals(graph2, graph3);
        assertEquals(graph1, graph3);
    }

    @Test
    void testGraphEqualsWithNull() {
        assertNotEquals(null, graph);
    }

    @Test
    void testGraphEqualsWithDifferentClass() {
        assertNotEquals("Not a graph", graph);
    }
}