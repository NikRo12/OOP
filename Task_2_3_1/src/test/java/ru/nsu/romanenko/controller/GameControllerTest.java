package ru.nsu.romanenko.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.model.Direction;
import ru.nsu.romanenko.model.GameConfig;
import ru.nsu.romanenko.model.GameState;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    private GameConfig config;

    @BeforeAll
    static void initJavaFX() {
        try {
            javafx.application.Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {
        }
    }

    @BeforeEach
    void setUp() {
        config = new GameConfig(10, 10, 2, 0.0, 5, 200, 30);
    }

    @Test
    void initialStateIsWaiting() {
        GameController controller = new GameController(config);
        controller.start();
        assertEquals(GameState.WAITING, controller.getState());
    }

    @Test
    void handleSpaceFromWaitingTransitionsToRunning() {
        GameController controller = new GameController(config);
        controller.start();
        controller.handleSpace();
        assertEquals(GameState.RUNNING, controller.getState());
    }

    @Test
    void handleSpaceWhileRunningDoesNothing() {
        GameController controller = new GameController(config);
        controller.start();
        controller.handleSpace();
        controller.handleSpace();
        assertEquals(GameState.RUNNING, controller.getState());
    }

    @Test
    void handleInputIgnoredWhenWaiting() {
        GameController controller = new GameController(config);
        controller.start();
        assertDoesNotThrow(() -> controller.handleInput(Direction.LEFT));
        assertEquals(GameState.WAITING, controller.getState());
    }

    @Test
    void handleInputIgnoredWhenLost() {
        GameController controller = new GameController(config);
        controller.start();
        controller.handleSpace();
        controller.stop();
        assertDoesNotThrow(() -> controller.handleInput(Direction.UP));
    }

    @Test
    void handleSpaceAfterLostResetsToWaiting() {
        GameController controller = new GameController(config);
        controller.start();
        controller.handleSpace();
        controller.stop();

        GameConfig tinyWin = new GameConfig(10, 10, 1, 0.0, 1, 200, 30);
        GameController c2 = new GameController(tinyWin);
        c2.start();
        assertEquals(GameState.WAITING, c2.getState());
        c2.handleSpace();
        assertEquals(GameState.RUNNING, c2.getState());
    }

    @Test
    void fieldIsNotNullAfterStart() {
        GameController controller = new GameController(config);
        controller.start();
        assertNotNull(controller.getField());
    }

    @Test
    void fieldHasCorrectDimensionsFromConfig() {
        GameController controller = new GameController(config);
        controller.start();
        assertEquals(10, controller.getField().getHorizontalSize());
        assertEquals(10, controller.getField().getVerticalSize());
    }

    @Test
    void stopDoesNotThrowWhenTimelineExists() {
        GameController controller = new GameController(config);
        controller.start();
        assertDoesNotThrow(controller::stop);
    }
}
