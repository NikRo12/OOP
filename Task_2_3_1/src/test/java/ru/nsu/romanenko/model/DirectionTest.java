package ru.nsu.romanenko.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {

    @Test
    void oppositeUp() {
        assertEquals(Direction.DOWN, Direction.UP.opposite());
    }

    @Test
    void oppositeDown() {
        assertEquals(Direction.UP, Direction.DOWN.opposite());
    }

    @Test
    void oppositeLeft() {
        assertEquals(Direction.RIGHT, Direction.LEFT.opposite());
    }

    @Test
    void oppositeRight() {
        assertEquals(Direction.LEFT, Direction.RIGHT.opposite());
    }

    @Test
    void nextUp() {
        assertEquals(new Position(5, 4), Direction.UP.next(new Position(5, 5)));
    }

    @Test
    void nextDown() {
        assertEquals(new Position(5, 6), Direction.DOWN.next(new Position(5, 5)));
    }

    @Test
    void nextRight() {
        assertEquals(new Position(6, 5), Direction.RIGHT.next(new Position(5, 5)));
    }

    @Test
    void nextLeft() {
        assertEquals(new Position(4, 5), Direction.LEFT.next(new Position(5, 5)));
    }

    @Test
    void doubleOppositeIsIdentity() {
        for (Direction d : Direction.values()) {
            assertEquals(d, d.opposite().opposite());
        }
    }
}
