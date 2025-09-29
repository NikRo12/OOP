package ru.nsu.romanenko.graphs;

import java.util.*;

public class IncidenceMatrix extends AbstractGraph {
    private final Map<Integer, Map<Integer, Integer>> matrix;
    private final Map<Integer, Edge> edges;
    private int nextEdgeId = 0;

    private static class Edge {
        final int from;
        final int to;
        final int id;

        Edge(int from, int to, int id) {
            this.from = from;
            this.to = to;
            this.id = id;
        }
    }

    public IncidenceMatrix() {
        this.matrix = new HashMap<>();
        this.edges = new HashMap<>();
    }


    @Override
    public boolean addVertex(int vertex) {
        if (hasVertex(vertex)) {
            return false;
        }
        vertices.add(vertex);

        Map<Integer, Integer> newRow = new HashMap<>();
        for(int i : edges.keySet())
        {
            newRow.put(i, 0);
        }

        matrix.put(vertex, newRow);

        return true;
    }

    @Override
    public boolean removeVertex(int vertex) {
        if (!hasVertex(vertex)) {
            return false;
        }

        Set<Integer> edgesToRemove = new HashSet<>();
        Map<Integer, Integer> vertexRow = matrix.get(vertex);

        for (Map.Entry<Integer, Integer> entry : vertexRow.entrySet()) {
            if (entry.getValue() != 0) {
                edgesToRemove.add(entry.getKey());
            }
        }

        for (int edgeId : edgesToRemove) {
            removeEdgeById(edgeId);
        }

        matrix.remove(vertex);

        return deleteVertex(vertex);
    }

    @Override
    public boolean addEdge(int from, int to) {
        if (!hasVertex(from) || !hasVertex(to)) {
            return false;
        }

        for (Edge edge : edges.values()) {
            if (edge.from == from && edge.to == to) {
                return false;
            }
        }

        int edgeId = nextEdgeId++;
        Edge edge = new Edge(from, to, edgeId);
        edges.put(edgeId, edge);

        for (int vertex : vertices) {
            Map<Integer, Integer> vertexRow = matrix.get(vertex);
            if (vertex == from) {
                vertexRow.put(edgeId, 1);
            } else if (vertex == to) {
                vertexRow.put(edgeId, -1);
            } else {
                vertexRow.put(edgeId, 0);
            }
        }

        edgeCount++;
        return true;
    }

    @Override
    public boolean removeEdge(int from, int to) {
        if (!hasEdge(from, to)) {
            return false;
        }

        Integer edgeId = null;
        for (Edge edge : edges.values()) {
            if (edge.from == from && edge.to == to) {
                edgeId = edge.id;
                break;
            }
        }

        if (edgeId != null) {
            removeEdgeById(edgeId);
            return true;
        }

        return false;
    }

    private void removeEdgeById(int edgeId) {
        Edge edge = edges.remove(edgeId);
        if (edge != null) {
            for (Map<Integer, Integer> vertexRow : matrix.values()) {
                vertexRow.remove(edgeId);
            }
            edgeCount--;
        }
    }

    @Override
    public List<Integer> getNeighbors(int vertex) {
        List<Integer> neighbours = new ArrayList<>();
        if (!hasVertex(vertex)) {
            return neighbours;
        }

        Map<Integer, Integer> vertexRow = matrix.get(vertex);
        for (Map.Entry<Integer, Integer> entry : vertexRow.entrySet()) {
            int edgeId = entry.getKey();
            int incidenceType = entry.getValue();

            Edge edge = edges.get(edgeId);
            if (edge != null) {
                if (incidenceType == 1) { // Исходящее ребро
                    neighbours.add(edge.to);
                } else if (incidenceType == -1) { // Входящее ребро
                    neighbours.add(edge.from);
                }
            }
        }

        return neighbours;
    }

    @Override
    public boolean hasEdge(int from, int to) {
        if (!hasVertex(from) || !hasVertex(to)) {
            return false;
        }

        for (Edge edge : edges.values()) {
            if (edge.from == from && edge.to == to) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void printGraph() {
        if (vertices.isEmpty()) {
            System.out.println("Graph is empty");
            return;
        }

        List<Integer> sortedVertices = new ArrayList<>(vertices);
        Collections.sort(sortedVertices);

        List<Integer> sortedEdgeIds = new ArrayList<>(edges.keySet());
        Collections.sort(sortedEdgeIds);

        System.out.print("    ");
        for (int edgeId : sortedEdgeIds) {
            Edge edge = edges.get(edgeId);
            System.out.print("e" + edgeId + "   ");
        }
        System.out.println();

        for (int vertex : sortedVertices) {
            System.out.print(vertex + " | ");
            Map<Integer, Integer> vertexRow = matrix.get(vertex);
            for (int edgeId : sortedEdgeIds) {
                int value = vertexRow.getOrDefault(edgeId, 0);
                if (value == 1) {
                    System.out.print(" 1   ");
                } else if (value == -1) {
                    System.out.print("-1   ");
                } else {
                    System.out.print(" 0   ");
                }
            }
            System.out.println();
        }
    }
}