package ru.nsu.romanenko.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

    @Test
    void waitingMessageNotEmpty() {
        assertFalse(GameState.WAITING.getMessage().isBlank());
    }

    @Test
    void runningMessageIsEmpty() {
        assertEquals("", GameState.RUNNING.getMessage());
    }

    @Test
    void wonMessageNotEmpty() {
        assertFalse(GameState.WON.getMessage().isBlank());
    }

    @Test
    void lostMessageNotEmpty() {
        assertFalse(GameState.LOST.getMessage().isBlank());
    }

    @Test
    void allFourStatesExist() {
        assertEquals(4, GameState.values().length);
    }
}
