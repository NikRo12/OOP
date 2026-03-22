package ru.nsu.romanenko.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.model.Direction;
import ru.nsu.romanenko.model.GameConfig;
import ru.nsu.romanenko.model.GameState;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    private GameConfig config;

    @BeforeEach
    void setUp() {
        config = new GameConfig(10, 10, 2, 0.0, 5, 200, 30);
    }

    @Test
    void initialStateIsWaiting() {
        GameController controller = new GameController(config);
        assertEquals(GameState.WAITING, controller.getState());
    }

    @Test
    void handleInputIgnoredWhenWaiting() {
        GameController controller = new GameController(config);
        assertDoesNotThrow(() -> controller.handleInput(Direction.LEFT));
        assertEquals(GameState.WAITING, controller.getState());
    }

    @Test
    void fieldIsNotNullAfterConstruction() {
        GameController controller = new GameController(config);
        assertNotNull(controller.getField());
    }

    @Test
    void fieldHasCorrectDimensionsFromConfig() {
        GameController controller = new GameController(config);
        assertEquals(10, controller.getField().getHorizontalSize());
        assertEquals(10, controller.getField().getVerticalSize());
    }

    @Test
    void stopDoesNotThrowBeforeStart() {
        GameController controller = new GameController(config);
        assertDoesNotThrow(controller::stop);
    }
}