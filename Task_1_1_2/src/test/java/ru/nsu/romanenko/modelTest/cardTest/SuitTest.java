package ru.nsu.romanenko.modelTest.cardTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.model.card.Suit;

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