package ru.nsu.romanenko;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты для перечисления Suit.
 */
class SuitTest {

    @Test
    void testSuitValues() {
        assertEquals(4, Suit.values().length);
        assertEquals("HEARTS", Suit.HEARTS.name());
        assertEquals("SPADES", Suit.SPADES.name());
        assertEquals("DIAMONDS", Suit.DIAMONDS.name());
        assertEquals("CLUBS", Suit.CLUBS.name());
    }
}