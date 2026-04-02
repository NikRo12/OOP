package ru.nsu.romanenko.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.model.Direction;
import ru.nsu.romanenko.model.GameConfig;
import ru.nsu.romanenko.model.GameState;
import ru.nsu.romanenko.model.Snake;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    private GameConfig config;

    @BeforeEach
    void setUp() {
        config = new GameConfig
                (10, 10, 2, 0.0, 5, 200);
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
    void testGameLostOnSelfCollision() {
        GameConfig config = new GameConfig
                (10, 10, 0, 0.0, 100, 200);
        GameController controller = new GameController(config);

        controller.handleSpace();

        Snake snake = controller.getField().getSnake();

        snake.growUp(4);
        for(int i = 0; i < 4; i++) {
            snake.move();
        }

        snake.setDirection(Direction.LEFT);
        snake.move();
        snake.setDirection(Direction.UP);
        snake.move();
        snake.setDirection(Direction.RIGHT);

        controller.tick();

        assertEquals(GameState.LOST, controller.getState(),
                "Змейка должна врезаться в себя");
    }

    @Test
    void testGameWon() {
        GameConfig winConfig = new GameConfig
                (10, 10, 0, 0.0, 2, 200);
        GameController controller = new GameController(winConfig);

        controller.handleSpace();

        Snake snake = controller.getField().getSnake();

        snake.growUp(1);

        snake.move();

        controller.tick();

        assertEquals(GameState.WON, controller.getState(),
                "Состояние должно быть WON при достижении лимита");
    }
}