package ru.nsu.romanenko.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @Test
    void equalsSameCoordinates() {
        assertEquals(new Position(3, 4), new Position(3, 4));
    }

    @Test
    void notEqualsDifferentX() {
        assertNotEquals(new Position(3, 4), new Position(5, 4));
    }

    @Test
    void notEqualsDifferentY() {
        assertNotEquals(new Position(3, 4), new Position(3, 9));
    }

    @Test
    void hashCodeConsistent() {
        assertEquals(new Position(3, 4).hashCode(), new Position(3, 4).hashCode());
    }

    @Test
    void hashCodeDiffersForDifferentPositions() {
        assertNotEquals(new Position(1, 2).hashCode(), new Position(2, 1).hashCode());
    }

    @Test
    void getters() {
        Position p = new Position(7, 11);
        assertEquals(7, p.getHorizontal());
        assertEquals(11, p.getVertical());
    }

    @Test
    void notEqualsNull() {
        assertNotEquals(null, new Position(1, 1));
    }
}
