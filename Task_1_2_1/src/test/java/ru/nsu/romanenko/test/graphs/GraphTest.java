package ru.nsu.romanenko.test.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Set;
import ru.nsu.romanenko.graphs.AdjacencyMatrix;
import ru.nsu.romanenko.graphs.Graph;

/**
 * Test class for Graph interface.
 */
class GraphTest {

    @Test
    void testAddAndRemoveVertex() {
        Graph graph = new AdjacencyMatrix();
        assertTrue(graph.addVertex(1));
        assertTrue(graph.hasVertex(1));
        assertEquals(1, graph.getVertexCount());

        assertTrue(graph.removeVertex(1));
        assertFalse(graph.hasVertex(1));
        assertEquals(0, graph.getVertexCount());
    }

    @Test
    void testAddAndRemoveEdge() {
        Graph graph = new AdjacencyMatrix();
        graph.addVertex(1);
        graph.addVertex(2);

        assertTrue(graph.addEdge(1, 2));
        assertTrue(graph.hasEdge(1, 2));
        assertEquals(1, graph.getEdgeCount());

        assertTrue(graph.removeEdge(1, 2));
        assertFalse(graph.hasEdge(1, 2));
        assertEquals(0, graph.getEdgeCount());
    }

    @Test
    void testGetNeighbors() {
        Graph graph = new AdjacencyMatrix();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);

        List<Integer> neighbors = graph.getNeighbors(1);
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains(2));
        assertTrue(neighbors.contains(3));
    }

    @Test
    void testGetVertices() {
        Graph graph = new AdjacencyMatrix();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);

        Set<Integer> vertices = graph.getVertices();
        assertEquals(3, vertices.size());
        assertTrue(vertices.contains(1));
        assertTrue(vertices.contains(2));
        assertTrue(vertices.contains(3));
    }
}