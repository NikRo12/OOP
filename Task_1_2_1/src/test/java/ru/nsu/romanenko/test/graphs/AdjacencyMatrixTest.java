package ru.nsu.romanenko.test.graphs;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import ru.nsu.romanenko.graphs.AdjacencyList;
import ru.nsu.romanenko.graphs.AdjacencyMatrix;

/**
 * Test class for AdjacencyMatrix.
 */
class AdjacencyMatrixTest {

    private AdjacencyMatrix graph;

    @BeforeEach
    void setUp() {
        graph = new AdjacencyMatrix();
    }

    @Test
    void testAddDuplicateVertex() {
        assertTrue(graph.addVertex(1));
        assertFalse(graph.addVertex(1));
        assertEquals(1, graph.getVertexCount());
    }

    @Test
    void testAddMultipleVertices() {
        for (int i = 1; i <= 5; i++) {
            assertTrue(graph.addVertex(i));
        }
        assertEquals(5, graph.getVertexCount());
        for (int i = 1; i <= 5; i++) {
            assertTrue(graph.hasVertex(i));
        }
    }

    @Test
    void testRemoveNonExistentVertex() {
        assertFalse(graph.removeVertex(999));
        assertEquals(0, graph.getVertexCount());
    }

    @Test
    void testRemoveVertexWithMultipleEdges() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(2, 1);
        graph.addEdge(1, 3);
        graph.addEdge(3, 1);

        assertEquals(4, graph.getEdgeCount());
        assertTrue(graph.removeVertex(1));
        assertEquals(2, graph.getVertexCount());
        assertEquals(0, graph.getEdgeCount());
        assertFalse(graph.hasEdge(1, 2));
        assertFalse(graph.hasEdge(2, 1));
        assertFalse(graph.hasEdge(1, 3));
        assertFalse(graph.hasEdge(3, 1));
    }

    @Test
    void testAddEdgeToNonExistentVertices() {
        assertFalse(graph.addEdge(1, 2));

        graph.addVertex(1);
        assertFalse(graph.addEdge(1, 2));

        graph.addVertex(2);
        assertTrue(graph.addEdge(1, 2));
    }

    @Test
    void testAddDuplicateEdge() {
        graph.addVertex(1);
        graph.addVertex(2);

        assertTrue(graph.addEdge(1, 2));
        assertFalse(graph.addEdge(1, 2));
        assertEquals(1, graph.getEdgeCount());
    }

    @Test
    void testRemoveNonExistentEdge() {
        graph.addVertex(1);
        graph.addVertex(2);

        assertFalse(graph.removeEdge(1, 2));
        assertFalse(graph.removeEdge(1, 999));
        assertFalse(graph.removeEdge(999, 1));
    }

    @Test
    void testGetNeighborsOfNonExistentVertex() {
        List<Integer> neighbors = graph.getNeighbors(999);
        assertTrue(neighbors.isEmpty());
    }

    @Test
    void testHasEdgeWithNonExistentVertices() {
        assertFalse(graph.hasEdge(1, 2));

        graph.addVertex(1);
        assertFalse(graph.hasEdge(1, 2));
        assertFalse(graph.hasEdge(2, 1));
    }

    @Test
    void testSelfLoop() {
        graph.addVertex(1);
        assertTrue(graph.addEdge(1, 1));
        assertTrue(graph.hasEdge(1, 1));
        assertEquals(1, graph.getEdgeCount());

        List<Integer> neighbors = graph.getNeighbors(1);
        assertEquals(1, neighbors.size());
        assertTrue(neighbors.contains(1));
    }

    @Test
    void testPrintEmptyGraph() {
        assertDoesNotThrow(() -> graph.printGraph());
    }

    @Test
    void testPrintNonEmptyGraph() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);

        assertDoesNotThrow(() -> graph.printGraph());
    }

    @Test
    void testGraphEquality() {
        AdjacencyMatrix graph1 = new AdjacencyMatrix();
        AdjacencyMatrix graph2 = new AdjacencyMatrix();

        graph1.addVertex(1);
        graph1.addVertex(2);
        graph1.addEdge(1, 2);

        graph2.addVertex(1);
        graph2.addVertex(2);
        graph2.addEdge(1, 2);

        assertEquals(graph1, graph2);
        assertEquals(graph2, graph1);

        graph2.addEdge(2, 1);
        assertNotEquals(graph1, graph2);
    }

    @Test
    void testGraphEqualsWithDifferentTypes() {
        AdjacencyMatrix matrixGraph = new AdjacencyMatrix();
        AdjacencyList listGraph = new AdjacencyList();

        matrixGraph.addVertex(1);
        listGraph.addVertex(1);

        assertEquals(matrixGraph, listGraph);
    }

    @Test
    void testGraphEqualsWithNull() {
        assertNotEquals(null, graph);
    }

    @Test
    void testGraphEqualsWithSameObject() {
        assertEquals(graph, graph);
    }

    @Test
    void testHashCodeConsistency() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);

        int hashCode1 = graph.hashCode();
        int hashCode2 = graph.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testToStringMethods() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addEdge(1, 2);

        assertNotNull(graph.toString());
        assertFalse(graph.toString().isEmpty());
    }
}