// AbstractGraphTest.java
package ru.nsu.romanenko.test.graphs;

import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.graphs.AbstractGraph;
import ru.nsu.romanenko.graphs.AdjacencyMatrix;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class AbstractGraphTest {

    @Test
    void testTopologicalSort() {
        AbstractGraph graph = new AdjacencyMatrix();

        // Создаем ациклический граф: 1 -> 2 -> 3
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);

        List<Integer> sorted = graph.topologicalSort();
        assertEquals(3, sorted.size());
        assertEquals(1, sorted.get(0));
        assertEquals(2, sorted.get(1));
        assertEquals(3, sorted.get(2));
    }

    @Test
    void testTopologicalSortWithCycle() {
        AbstractGraph graph = new AdjacencyMatrix();

        // Создаем циклический граф: 1 -> 2 -> 3 -> 1
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 1);

        assertThrows(IllegalStateException.class, () -> {
            graph.topologicalSort();
        });
    }

    @Test
    void testGraphEquals() {
        AbstractGraph graph1 = new AdjacencyMatrix();
        AbstractGraph graph2 = new AdjacencyMatrix();

        graph1.addVertex(1);
        graph1.addVertex(2);
        graph1.addEdge(1, 2);

        graph2.addVertex(1);
        graph2.addVertex(2);
        graph2.addEdge(1, 2);

        assertTrue(graph1.equals(graph2));

        graph2.addEdge(2, 1);
        assertFalse(graph1.equals(graph2));
    }
}