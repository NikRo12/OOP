package ru.nsu.romanenko.graphs;

import java.util.List;
import java.util.Set;

public interface Graph {
    boolean addVertex(int vertex);
    boolean removeVertex(int vertex);
    boolean addEdge(int from, int to);
    boolean removeEdge(int from, int to);
    List<Integer> getNeighbors(int vertex);

    int getVertexCount();
    int getEdgeCount();
    Set<Integer> getVertices();
    boolean hasVertex(int vertex);
    boolean hasEdge(int from, int to);
    boolean deleteVertex(int vertex);

    boolean readFromFile(String filename);
    void printGraph();
    boolean equals(Object obj);

    List<Integer> topologicalSort();
}