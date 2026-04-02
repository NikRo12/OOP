package ru.nsu.romanenko.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SnakeTest {

    @Test
    void initialSizeIsOne() {
        Snake snake = new Snake(new Position(5, 5), Direction.DOWN);
        assertEquals(1, snake.getSize());
    }

    @Test
    void initialHeadIsStartPosition() {
        Snake snake = new Snake(new Position(3, 7), Direction.RIGHT);
        assertEquals(new Position(3, 7), snake.getHead());
    }

    @Test
    void moveAdvancesHead() {
        Snake snake = new Snake(new Position(5, 5), Direction.RIGHT);
        snake.move();
        assertEquals(new Position(6, 5), snake.getHead());
    }

    @Test
    void moveMaintainsSize() {
        Snake snake = new Snake(new Position(5, 5), Direction.DOWN);
        snake.move();
        assertEquals(1, snake.getSize());
    }

    @Test
    void growUpThenMoveSizeIncreasedByOne() {
        Snake snake = new Snake(new Position(5, 5), Direction.DOWN);
        snake.growUp(1);
        snake.move();
        assertEquals(2, snake.getSize());
    }

    @Test
    void growUpThenMoveThreeTimesSizeIncreasedByThree() {
        Snake snake = new Snake(new Position(5, 5), Direction.DOWN);
        snake.growUp(3);
        snake.move();
        snake.move();
        snake.move();
        assertEquals(4, snake.getSize());
    }

    @Test
    void growUpGrowsOnlyDuringMoves() {
        Snake snake = new Snake(new Position(5, 5), Direction.DOWN);
        snake.growUp(2);
        snake.move();
        assertEquals(2, snake.getSize());
        snake.move();
        assertEquals(3, snake.getSize());
        snake.move();
        assertEquals(3, snake.getSize());
    }

    @Test
    void setDirectionChangesDirection() {
        Snake snake = new Snake(new Position(5, 5), Direction.DOWN);
        snake.setDirection(Direction.RIGHT);
        snake.move();
        assertEquals(new Position(6, 5), snake.getHead());
    }

    @Test
    void setDirectionIgnoresOpposite() {
        Snake snake = new Snake(new Position(5, 5), Direction.DOWN);
        snake.setDirection(Direction.UP);
        snake.move();
        assertEquals(new Position(5, 6), snake.getHead());
    }

    @Test
    void wrapHeadRightEdge() {
        Snake snake = new Snake(new Position(9, 5), Direction.RIGHT);
        snake.move();
        snake.wrapHead(10, 10);
        assertEquals(new Position(0, 5), snake.getHead());
    }

    @Test
    void wrapHeadLeftEdge() {
        Snake snake = new Snake(new Position(0, 5), Direction.LEFT);
        snake.move();
        snake.wrapHead(10, 10);
        assertEquals(new Position(9, 5), snake.getHead());
    }

    @Test
    void wrapHeadBottomEdge() {
        Snake snake = new Snake(new Position(5, 9), Direction.DOWN);
        snake.move();
        snake.wrapHead(10, 10);
        assertEquals(new Position(5, 0), snake.getHead());
    }

    @Test
    void wrapHeadTopEdge() {
        Snake snake = new Snake(new Position(5, 0), Direction.UP);
        snake.move();
        snake.wrapHead(10, 10);
        assertEquals(new Position(5, 9), snake.getHead());
    }

    @Test
    void wrapHeadDoesNotChangeSize() {
        Snake snake = new Snake(new Position(9, 5), Direction.RIGHT);
        snake.growUp(2);
        snake.move();
        int sizeBefore = snake.getSize();
        snake.wrapHead(10, 10);
        assertEquals(sizeBefore, snake.getSize());
    }

    @Test
    void getBodyReturnsDefensiveCopy() {
        Snake snake = new Snake(new Position(5, 5), Direction.DOWN);
        snake.getBody().clear();
        assertEquals(1, snake.getSize());
    }

    @Test
    void setDirectionIgnoredOpposite() {
        // Змейка смотрит ВНИЗ
        Snake snake = new Snake(new Position(5, 5), Direction.DOWN);

        // Пытаемся развернуть ВВЕРХ
        snake.setDirection(Direction.UP);

        // Проверяем, что направление осталось DOWN
        snake.move();
        assertEquals(new Position(5, 6), snake.getHead(),
                "Змейка не должна разворачиваться на 180 градусов");
    }

    @Test
    void wrapAroundHorizontal() {
        int width = 10;
        int height = 10;
        // Голова на самом краю (x=9), едем направо
        Snake snake = new Snake(new Position(9, 5), Direction.RIGHT);

        snake.move(); // Станет (10, 5)
        snake.wrapHead(width, height); // Должно стать (0, 5)

        assertEquals(new Position(0, 5), snake.getHead(),
                "Змейка должна появиться с противоположной стороны поля");
    }
}