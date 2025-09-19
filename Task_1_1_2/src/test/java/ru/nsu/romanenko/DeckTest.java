package ru.nsu.romanenko;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Тесты для класса Deck.
 */
class DeckTest {

    @Test
    void testDeckInitialization() {
        Deck deck = new Deck();
        for (int i = 0; i < 52; i++) {
            assertNotNull(deck.getRandomCard());
        }
    }

    @Test
    void testDeckRefill() {
        Deck deck = new Deck();
        // Exhaust the deck
        for (int i = 0; i < 52; i++) {
            deck.getRandomCard();
        }
        assertNotNull(deck.getRandomCard());
    }
}