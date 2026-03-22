package ru.nsu.romanenko.model;

import com.google.gson.Gson;
import ru.nsu.romanenko.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ConfigReader {
    public static GameConfig read() {
        try (InputStream inputStream = Main.class.getClassLoader().
                getResourceAsStream("config.json")) {
            if (inputStream == null) {
                throw new RuntimeException("Config file not found");
            }
            String jsonString = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.joining("\n"));

            return new Gson().fromJson(jsonString, GameConfig.class);
        } catch (IOException ex){
            throw new RuntimeException("Failed to read config", ex);
        }
    }
}
