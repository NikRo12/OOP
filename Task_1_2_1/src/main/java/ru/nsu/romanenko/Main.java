package ru.nsu.romanenko;

import ru.nsu.romanenko.graphs.AdjacencyList;
import ru.nsu.romanenko.graphs.AdjacencyMatrix;
import ru.nsu.romanenko.graphs.Graph;
import ru.nsu.romanenko.graphs.IncidenceMatrix;

public class Main {
    public static void main(String[] args) {
        Graph graph = new AdjacencyMatrix();
        graph.addVertex(1);
        graph.addEdge(1, 1);
        graph.removeVertex(1);

        System.out.println(graph);
    }
}