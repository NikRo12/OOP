package ru.nsu.romanenko;

import java.io.FileInputStream;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        Generator.generate();

        String testFile = "huge_utf8_test.txt";
        String searchString = "\uD83D\uDE48";

        try (FileInputStream fis = new FileInputStream(testFile)) {
            ArrayList<Long> positions = SubstringFinder.find(fis, searchString);
            if (!positions.isEmpty()) {
                System.out.println(positions);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}