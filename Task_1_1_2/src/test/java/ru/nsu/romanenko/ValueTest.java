package ru.nsu.romanenko;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Тесты для перечисления Value.
 */
class ValueTest {

    @Test
    void testValueValues() {
        assertEquals(13, Value.values().length);
        assertEquals("TWO", Value.TWO.name());
        assertEquals("ACE", Value.ACE.name());
        assertEquals("TEN", Value.TEN.name());
        assertEquals("KING", Value.KING.name());
    }
}