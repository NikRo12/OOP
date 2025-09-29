// AdjacencyMatrixTest.java
package ru.nsu.romanenko.test.graphs;

import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.graphs.AdjacencyMatrix;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class AdjacencyMatrixTest {

    @Test
    void testAddVertex() {
        AdjacencyMatrix graph = new AdjacencyMatrix();
        assertTrue(graph.addVertex(1));
        assertTrue(graph.hasVertex(1));
        assertFalse(graph.addVertex(1)); // Дубликат
    }

    @Test
    void testRemoveVertex() {
        AdjacencyMatrix graph = new AdjacencyMatrix();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);

        assertTrue(graph.removeVertex(1));
        assertFalse(graph.hasVertex(1));
        assertFalse(graph.hasEdge(1, 2));
        assertEquals(0, graph.getEdgeCount());
    }

    @Test
    void testAddEdge() {
        AdjacencyMatrix graph = new AdjacencyMatrix();
        graph.addVertex(1);
        graph.addVertex(2);

        assertTrue(graph.addEdge(1, 2));
        assertTrue(graph.hasEdge(1, 2));
        assertFalse(graph.addEdge(1, 2)); // Дубликат
        assertFalse(graph.addEdge(1, 3)); // Несуществующая вершина
    }

    @Test
    void testRemoveEdge() {
        AdjacencyMatrix graph = new AdjacencyMatrix();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);

        assertTrue(graph.removeEdge(1, 2));
        assertFalse(graph.hasEdge(1, 2));
        assertFalse(graph.removeEdge(1, 2)); // Уже удалено
    }

    @Test
    void testGetNeighbors() {
        AdjacencyMatrix graph = new AdjacencyMatrix();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);

        List<Integer> neighbors = graph.getNeighbors(1);
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains(2));
        assertTrue(neighbors.contains(3));

        List<Integer> emptyNeighbors = graph.getNeighbors(2);
        assertTrue(emptyNeighbors.isEmpty());
    }

    @Test
    void testReadFromFile() {
        AdjacencyMatrix graph = new AdjacencyMatrix();
        // Предполагается, что файл существует и содержит валидную матрицу
        // boolean result = graph.readFromFile("valid_graph.txt");
        // assertTrue(result);
        // Добавьте конкретные проверки в зависимости от содержимого файла
    }
}