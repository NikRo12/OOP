package ru.nsu.romanenko.input_output;

import java.util.Scanner;

public class Input {
    public static String get_parsing_line()
    {
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        in.close();
        return input;
    }
}
