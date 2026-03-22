package ru.nsu.romanenko.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.model.food.Apple;
import ru.nsu.romanenko.model.food.Food;
import ru.nsu.romanenko.model.food.GoldenApple;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameFieldTest {

    private GameConfig config;
    private Snake snake;
    private GameField field;

    @BeforeEach
    void setUp() {
        config = new GameConfig(10, 10, 3, 0.0, 20, 200, 30);
        snake = new Snake(new Position(5, 5), Direction.DOWN);
        field = new GameField(config, List.of(new Apple(), new GoldenApple()), snake);
        field.init();
    }

    @Test
    void initPlacesCorrectFoodCount() {
        assertEquals(3, field.getFoods().size());
    }

    @Test
    void initPlacesNoObstaclesWhenRatioZero() {
        assertTrue(field.getObstacles().isEmpty());
    }

    @Test
    void initPlacesNoFoodOnSnakeBody() {
        for (Position p : snake.getBody()) {
            assertFalse(field.isFood(p), "Food placed on snake body at " + p);
        }
    }

    @Test
    void initNoFoodWithinTwoCellsOfHead() {
        Position head = snake.getHead();
        for (Position p : field.getFoods().keySet()) {
            int dist = Math.abs(p.getHorizontal() - head.getHorizontal())
                     + Math.abs(p.getVertical() - head.getVertical());
            assertTrue(dist > 2, "Food too close to head: " + p);
        }
    }

    @Test
    void isObstacleReturnsTrueAfterInit() {
        GameConfig configWithObstacles = new GameConfig(10, 10, 1, 0.1, 20, 200, 30);
        Snake s2 = new Snake(new Position(5, 5), Direction.DOWN);
        GameField f2 = new GameField(configWithObstacles, List.of(new Apple()), s2);
        f2.init();
        assertFalse(f2.getObstacles().isEmpty());
        Position obstaclePos = f2.getObstacles().keySet().iterator().next();
        assertTrue(f2.isObstacle(obstaclePos));
    }

    @Test
    void isFoodReturnsTrueForPlacedFood() {
        Position foodPos = field.getFoods().keySet().iterator().next();
        assertTrue(field.isFood(foodPos));
    }

    @Test
    void isFoodReturnsFalseForEmptyCell() {
        assertFalse(field.isFood(new Position(5, 5)));
    }

    @Test
    void getFoodAtReturnsCorrectFood() {
        Position foodPos = field.getFoods().keySet().iterator().next();
        assertNotNull(field.getFoodAt(foodPos));
    }

    @Test
    void replaceFoodMaintainsFoodCount() {
        Position foodPos = field.getFoods().keySet().iterator().next();
        field.replaceFood(foodPos);
        assertEquals(3, field.getFoods().size());
    }

    @Test
    void replaceFoodRemovesEatenFood() {
        Position foodPos = field.getFoods().keySet().iterator().next();
        field.replaceFood(foodPos);
        assertFalse(field.isFood(foodPos));
    }

    @Test
    void getSizeReturnsCorrectDimensions() {
        assertEquals(10, field.getHorizontalSize());
        assertEquals(10, field.getVerticalSize());
    }

    @Test
    void getFoodsReturnsUnmodifiableMap() {
        assertThrows(UnsupportedOperationException.class,
                () -> field.getFoods().put(new Position(0, 0), new Apple()));
    }

    @Test
    void getObstaclesReturnsUnmodifiableMap() {
        assertThrows(UnsupportedOperationException.class,
                () -> field.getObstacles().put(new Position(0, 0), null));
    }
}
