package ru.nsu.romanenko;

import ru.nsu.romanenko.graphs.Graph;
import ru.nsu.romanenko.graphs.IncidenceMatrix;

public class Main {
    public static void main(String[] args)
    {
        Graph graph = new IncidenceMatrix();

        if(graph.readFromFile("D:\\Progs\\Develope\\OOP\\OOP\\Task_1_2_1\\src\\main\\resources\\text.txt"))
        {
            graph.printGraph();
        }
    }
}
