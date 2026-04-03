package ru.nsu.romanenko.model.food;

import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.model.Direction;
import ru.nsu.romanenko.model.Position;
import ru.nsu.romanenko.model.Snake;

import static org.junit.jupiter.api.Assertions.*;

public class FoodTest {

    @Test
    void appleGrowsSnakeByOneAfterMove() {
        Snake snake = new Snake(new Position(5, 5), Direction.DOWN);
        new Apple().apply(snake);
        snake.move();
        assertEquals(2, snake.getSize());
    }

    @Test
    void goldenAppleGrowsSnakeByTwoAfterTwoMoves() {
        Snake snake = new Snake(new Position(5, 5), Direction.DOWN);
        new GoldenApple().apply(snake);
        snake.move();
        snake.move();
        assertEquals(3, snake.getSize());
    }

    @Test
    void appleDoesNotMoveHead() {
        Snake snake = new Snake(new Position(5, 5), Direction.DOWN);
        Position headBefore = snake.getHead();
        new Apple().apply(snake);
        assertEquals(headBefore, snake.getHead());
    }

    @Test
    void goldenAppleDoesNotMoveHead() {
        Snake snake = new Snake(new Position(5, 5), Direction.DOWN);
        Position headBefore = snake.getHead();
        new GoldenApple().apply(snake);
        assertEquals(headBefore, snake.getHead());
    }
}