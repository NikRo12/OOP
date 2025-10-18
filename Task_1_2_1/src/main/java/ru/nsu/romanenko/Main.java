package ru.nsu.romanenko;

import ru.nsu.romanenko.graphs.Graph;
import ru.nsu.romanenko.graphs.IncidenceMatrix;

public class Main {
    public static void main(String[] args) {
        Graph graph = new IncidenceMatrix();

        String path = "src/main/resources/text.txt";

        if (graph.readFromFile(path)) {
            graph.printGraph();
        }
    }
}