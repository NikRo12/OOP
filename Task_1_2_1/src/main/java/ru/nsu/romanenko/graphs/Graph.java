package ru.nsu.romanenko.graphs;

import java.util.List;
import java.util.Set;

/**
 * Graph interface.
 */
public interface Graph {
    /**
     * Adds a vertex to the graph.
     *
     * @param vertex vertex to add
     * @return true if vertex was added, false if it already exists
     */
    boolean addVertex(int vertex);

    /**
     * Removes a vertex from the graph.
     *
     * @param vertex vertex to remove
     * @return true if vertex was removed, false if it doesn't exist
     */
    boolean removeVertex(int vertex);

    /**
     * Adds an edge between two vertices.
     *
     * @param from source vertex
     * @param to target vertex
     * @return true if edge was added, false if it already exists or vertices don't exist
     */
    boolean addEdge(int from, int to);

    /**
     * Removes an edge between two vertices.
     *
     * @param from source vertex
     * @param to target vertex
     * @return true if edge was removed, false if it doesn't exist
     */
    boolean removeEdge(int from, int to);

    /**
     * Gets neighbors of a vertex.
     *
     * @param vertex vertex to get neighbors for
     * @return list of neighbor vertices
     */
    List<Integer> getNeighbors(int vertex);

    /**
     * Gets the number of vertices in the graph.
     *
     * @return vertex count
     */
    int getVertexCount();

    /**
     * Gets the number of edges in the graph.
     *
     * @return edge count
     */
    int getEdgeCount();

    /**
     * Gets all vertices in the graph.
     *
     * @return set of vertices
     */
    Set<Integer> getVertices();

    /**
     * Checks if vertex exists in the graph.
     *
     * @param vertex vertex to check
     * @return true if vertex exists
     */
    boolean hasVertex(int vertex);

    /**
     * Checks if edge exists between two vertices.
     *
     * @param from source vertex
     * @param to target vertex
     * @return true if edge exists
     */
    boolean hasEdge(int from, int to);

    /**
     * Deletes a vertex from internal storage.
     *
     * @param vertex vertex to delete
     * @return true if vertex was deleted
     */
    boolean deleteVertex(int vertex);

    /**
     * Reads graph from file.
     *
     * @param filename file to read from
     * @return true if read successfully
     */
    boolean readFromFile(String filename);

    /**
     * Prints the graph representation.
     */
    void printGraph();

    /**
     * Checks equality with another graph.
     *
     * @param obj object to compare with
     * @return true if graphs are equal
     */
    boolean equals(Object obj);

    /**
     * Performs topological sort of the graph.
     *
     * @return topologically sorted list of vertices
     * @throws IllegalStateException if graph has cycles
     */
    List<Integer> topologicalSort();
}