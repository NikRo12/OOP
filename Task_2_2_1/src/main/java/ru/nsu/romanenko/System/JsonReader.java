package ru.nsu.romanenko.System;

import com.google.gson.Gson;
import com.sun.tools.javac.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class JsonReader {
    static public Config read() {
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.json")) {
            String jsonString = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.joining("\n"));

            return new Gson().fromJson(jsonString, Config.class);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return new Config(1, 1, 1);
    }
}
