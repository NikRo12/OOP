package ru.nsu.romanenko.System;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest {

    @Test
    void testConfigDeserialization() {
        String json = "{\"bakersCount\":3,\"couriersCount\":2,\"warehouseCapacity\":10}";
        com.google.gson.Gson gson = new com.google.gson.Gson();
        Config config = gson.fromJson(json, Config.class);
        assertNotNull(config);
    }

    @Test
    void testReadFallbackOnException() {
        Config config = JsonReader.read();
        assertNotNull(config);
    }
}