package ru.nsu.romanenko.graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdjacencyMatrix extends AbstractGraph {
    private final Map<Integer, Map<Integer, Boolean>> matrix;

    public AdjacencyMatrix() {
        this.matrix = new HashMap<>();
    }

    @Override
    public boolean addVertex(int vertex) {
        if (hasVertex(vertex)) {
            return false;
        }
        vertices.add(vertex);

        Map<Integer, Boolean> newRow = new HashMap<>();
        for (int existingVertex : vertices) {
            newRow.put(existingVertex, false);
        }
        matrix.put(vertex, newRow);

        for (Map<Integer, Boolean> row : matrix.values()) {
            if (row != newRow) {
                row.put(vertex, false);
            }
        }

        return true;
    }

    @Override
    public boolean removeVertex(int vertex) {
        if (!hasVertex(vertex)) {
            return false;
        }

        int edgesRemoved = 0;
        Map<Integer, Boolean> row = matrix.get(vertex);
        for (boolean hasEdge : row.values()) {
            if (hasEdge) {
                edgesRemoved++;
            }
        }

        matrix.remove(vertex);

        for (Map<Integer, Boolean> otherRow : matrix.values()) {
            if (otherRow.containsKey(vertex) && otherRow.get(vertex)) {
                edgesRemoved++;
            }
        }

        matrix.remove(vertex);

        for (Map<Integer, Boolean> otherRow : matrix.values()) {
            otherRow.remove(vertex);
        }

        edgeCount -= edgesRemoved;
        return deleteVertex(vertex);
    }

    @Override
    public boolean addEdge(int from, int to) {
        if (!hasVertex(from) || !hasVertex(to)) {
            return false;
        }

        if (!matrix.get(from).get(to)) {
            matrix.get(from).put(to, true);
            edgeCount++;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeEdge(int from, int to) {
        if (!hasEdge(from, to)) {
            return false;
        }

        matrix.get(from).put(to, false);
        edgeCount--;
        return true;
    }

    @Override
    public List<Integer> getNeighbors(int vertex) {
        List<Integer> neighbours = new ArrayList<>();
        if (!hasVertex(vertex)) {
            return neighbours;
        }

        Map<Integer, Boolean> row = matrix.get(vertex);
        for (Map.Entry<Integer, Boolean> entry : row.entrySet()) {
            if (entry.getValue()) {
                neighbours.add(entry.getKey());
            }
        }
        return neighbours;
    }

    @Override
    public boolean hasEdge(int from, int to) {
        if (!hasVertex(from) || !hasVertex(to)) {
            return false;
        }
        return matrix.get(from).get(to);
    }

    @Override
    public String toString() {
        if (vertices.isEmpty()) {
            return "";
        }

        List<Integer> sortedVertices = new ArrayList<>(vertices);
        Collections.sort(sortedVertices);

        StringBuilder graphString = new StringBuilder();

        graphString.append("    ");
        for (int vertex : sortedVertices) {
            graphString.append(vertex).append("  ");
        }
        graphString.append("\n");

        for (int from : sortedVertices) {
            graphString.append(from).append(" | ");
            for (int to : sortedVertices) {
                graphString.append(hasEdge(from, to) ? "1" : "0").append("  ");
            }
            graphString.append("\n");
        }

        return graphString.toString();
    }
}