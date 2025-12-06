package ru.nsu.romanenko.test.graphs;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.graphs.IncidenceMatrix;

/**
 * Test class for IncidenceMatrix.
 */
class IncidenceMatrixTest {

    @Test
    void testAddVertex() {
        IncidenceMatrix graph = new IncidenceMatrix();
        assertTrue(graph.addVertex(1));
        assertTrue(graph.hasVertex(1));
        assertFalse(graph.addVertex(1));
    }

    @Test
    void testRemoveVertex() {
        IncidenceMatrix graph = new IncidenceMatrix();
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
        IncidenceMatrix graph = new IncidenceMatrix();
        graph.addVertex(1);
        graph.addVertex(2);

        assertTrue(graph.addEdge(1, 2));
        assertTrue(graph.hasEdge(1, 2));
        assertFalse(graph.addEdge(1, 2));
        assertFalse(graph.addEdge(1, 3));
    }

    @Test
    void testRemoveEdge() {
        IncidenceMatrix graph = new IncidenceMatrix();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);

        assertTrue(graph.removeEdge(1, 2));
        assertFalse(graph.hasEdge(1, 2));
        assertFalse(graph.removeEdge(1, 2));
    }

    @Test
    void testMultipleEdges() {
        IncidenceMatrix graph = new IncidenceMatrix();
        graph.addVertex(1);
        graph.addVertex(2);

        assertTrue(graph.addEdge(1, 2));
        assertTrue(graph.addEdge(2, 1));

        assertTrue(graph.hasEdge(1, 2));
        assertTrue(graph.hasEdge(2, 1));
        assertEquals(2, graph.getEdgeCount());
    }

    @Test
    void testPrintGraph() {
        IncidenceMatrix graph = new IncidenceMatrix();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);

        assertDoesNotThrow(() -> System.out.println(graph));
    }
}