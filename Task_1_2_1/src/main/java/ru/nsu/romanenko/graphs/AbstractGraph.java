package ru.nsu.romanenko.graphs;

import ru.nsu.romanenko.system.Input;

import java.util.*;

public abstract class AbstractGraph implements Graph {
    protected Set<Integer> vertices = new HashSet<>();
    protected int edgeCount = 0;

    @Override
    public int getVertexCount() {
        return vertices.size();
    }

    @Override
    public int getEdgeCount() {
        return edgeCount;
    }

    @Override
    public Set<Integer> getVertices() {
        return new HashSet<>(vertices);
    }

    @Override
    public boolean hasVertex(int vertex) {
        return vertices.contains(vertex);
    }

    @Override
    public boolean deleteVertex(int vertex)
    {
        return vertices.remove(vertex);
    }

    @Override
    public List<Integer> topologicalSort() {
        List<Integer> result = new ArrayList<>();
        Map<Integer, Integer> inDegree = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();

        for (int vertex : vertices) {
            inDegree.put(vertex, 0);
        }

        for (int vertex : vertices) {
            for (int neighbor : getNeighbors(vertex)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        for (int vertex : vertices) {
            if (inDegree.get(vertex) == 0) {
                queue.offer(vertex);
            }
        }

        while (!queue.isEmpty()) {
            int current = queue.poll();
            result.add(current);

            for (int neighbor : getNeighbors(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Проверка на циклы
        if (result.size() != vertices.size()) {
            throw new IllegalStateException("Graph has a cycle, topological sort not possible");
        }

        return result;
    }

    @Override
    public boolean readFromFile(String filename) {
        if (!vertices.isEmpty()) {
            System.out.println("You already created this graph");
            return false;
        }

        try {
            ArrayList<String> inputLines = Input.read(filename);
            int size = inputLines.size();

            for (int i = 1; i <= size; i++) {
                if (!addVertex(i)) {
                    System.out.println("Add Vertex Error at vertex " + i);
                    return false;
                }
            }

            for (int i = 0; i < size; i++) {
                String line = inputLines.get(i);
                for (int j = 0; j < size; j++) {
                    if (line.charAt(j) == '1') {
                        if (!addEdge(i + 1, j + 1)) {
                            System.out.println("Add Edge Error from " + (i + 1) + " to " + (j + 1));
                            return false;
                        }
                    }
                }
            }

            return true;
        } catch (RuntimeException e) {
            System.out.println("Error reading graph from file: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Graph other)) return false;

        if (!this.getVertices().equals(other.getVertices())) return false;

        for (int from : this.getVertices()) {
            for (int to : this.getVertices()) {
                if (this.hasEdge(from, to) != other.hasEdge(from, to)) {
                    return false;
                }
            }
        }
        return true;
    }
}