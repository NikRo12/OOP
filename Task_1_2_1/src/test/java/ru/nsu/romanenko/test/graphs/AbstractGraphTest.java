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
    void testTopologicalSortSingleComponent() {
        graph.addVertex(1);

        List<Integer> sorted = graph.topologicalSort();
        assertEquals(1, sorted.size());
        assertEquals(1, sorted.get(0));
    }

    @Test
    void testTopologicalSortWithSelfLoop() {
        graph.addVertex(1);
        graph.addEdge(1, 1);

        assertThrows(IllegalStateException.class, () -> {
            graph.topologicalSort();
        });
    }

    @Test
    void testReadFromFileOnNonEmptyGraph() {
        graph.addVertex(1);

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

        assertFalse(graph1.equals(graph2));
        assertFalse(graph2.equals(graph1));
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

        assertFalse(graph1.equals(graph2));
    }

    @Test
    void testGetVerticesImmutability() {
        graph.addVertex(1);
        graph.addVertex(2);

        Set<Integer> vertices = graph.getVertices();
        assertEquals(2, vertices.size());

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
        for (int i = 1; i <= 10; i++) {
            graph.addVertex(i);
        }

        int edgeCount = 0;
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (i != j && graph.addEdge(i, j)) {
                    edgeCount++;
                }
            }
        }

        assertEquals(edgeCount, graph.getEdgeCount());

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
    void testTopologicalSortMultipleValidOrders() {
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

        assertTrue(sorted.indexOf(1) < sorted.indexOf(2));
        assertTrue(sorted.indexOf(1) < sorted.indexOf(3));
        assertTrue(sorted.indexOf(2) < sorted.indexOf(4));
        assertTrue(sorted.indexOf(3) < sorted.indexOf(4));
    }

    @Test
    void testTopologicalSortDisconnectedGraph() {
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addEdge(1, 2);
        graph.addEdge(3, 4);

        List<Integer> sorted = graph.topologicalSort();
        assertEquals(4, sorted.size());

        assertTrue(sorted.indexOf(1) < sorted.indexOf(2));
        assertTrue(sorted.indexOf(3) < sorted.indexOf(4));
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

        List<Integer> sorted = graph.topologicalSort();
        assertEquals(3, sorted.size());
        assertTrue(sorted.indexOf(1) < sorted.indexOf(2));
    }

    @Test
    void testGraphEqualsWithDifferentVertexOrder() {
        AbstractGraph graph1 = new AdjacencyMatrix();
        AbstractGraph graph2 = new AdjacencyMatrix();

        graph1.addVertex(1);
        graph1.addVertex(2);
        graph1.addEdge(1, 2);

        graph2.addVertex(2);
        graph2.addVertex(1);
        graph2.addEdge(1, 2);

        assertTrue(graph1.equals(graph2));
    }

    @Test
    void testGraphEqualsReflexivity() {
        assertTrue(graph.equals(graph));
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

        assertTrue(graph1.equals(graph2));
        assertTrue(graph2.equals(graph3));
        assertTrue(graph1.equals(graph3));
    }

    @Test
    void testGraphEqualsWithNull() {
        assertFalse(graph.equals(null));
    }

    @Test
    void testGraphEqualsWithDifferentClass() {
        assertFalse(graph.equals("Not a graph"));
    }
}