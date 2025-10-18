package ru.nsu.romanenko.graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdjacencyList extends AbstractGraph {
    private final Map<Integer, Set<Integer>> list;

    public AdjacencyList() {
        this.list = new HashMap<>();
    }

    @Override
    public boolean addVertex(int vertex) {
        if (hasVertex(vertex)) {
            return false;
        }

        vertices.add(vertex);
        list.put(vertex, new HashSet<>());
        return true;
    }

    @Override
    public boolean removeVertex(int vertex) {
        if (!hasVertex(vertex)) {
            return false;
        }

        int deleteEdges = list.get(vertex).size();
        list.remove(vertex);

        for (Set<Integer> row : list.values()) {
            if (row.remove(vertex)) {
                deleteEdges++;
            }
        }

        edgeCount -= deleteEdges;
        return deleteVertex(vertex);
    }

    @Override
    public boolean addEdge(int from, int to) {
        if (!hasVertex(from) || !hasVertex(to)) {
            return false;
        }

        if (list.get(from).add(to)) {
            edgeCount++;
            return true;
        }

        return false;
    }

    @Override
    public boolean removeEdge(int from, int to) {
        if (!hasVertex(from) || !hasVertex(to)) {
            return false;
        }

        if (list.get(from).remove(to)) {
            edgeCount--;
            return true;
        }

        return false;
    }

    @Override
    public List<Integer> getNeighbors(int vertex) {
        if (!hasVertex(vertex)) {
            return new ArrayList<>();
        }

        return new ArrayList<>(list.get(vertex));
    }

    @Override
    public boolean hasEdge(int from, int to) {
        if (!hasVertex(from) || !hasVertex(to)) {
            return false;
        }

        return list.get(from).contains(to);
    }

    @Override
    public void printGraph() {
        if (vertices.isEmpty()) {
            System.out.println("Graph is empty");
            return;
        }

        List<Integer> sortedVertices = new ArrayList<>(vertices);
        Collections.sort(sortedVertices);

        StringBuilder graphString = new StringBuilder();
        graphString.append("Adjacency List:\n");

        for (int vertex : sortedVertices) {
            List<Integer> neighbors = new ArrayList<>(list.get(vertex));
            Collections.sort(neighbors);
            graphString.append(vertex).append(" -> ").append(neighbors).append("\n");
        }

        System.out.print(graphString.toString());
    }
}