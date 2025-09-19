package ru.nsu.romanenko;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса Card.
 */
class CardTest {

    @Test
    void testCardCreation() {
        Card card = new Card(Suit.HEARTS, Value.ACE);
        assertEquals("HEARTS", card.getSuit());
        assertEquals("ACE", card.getValue());
    }

    @Test
    void testCardValues() {
        Card heartTen = new Card(Suit.HEARTS, Value.TEN);
        Card spadeKing = new Card(Suit.SPADES, Value.KING);
        Card diamondAce = new Card(Suit.DIAMONDS, Value.ACE);
        Card clubThree = new Card(Suit.CLUBS, Value.THREE);

        assertEquals("HEARTS", heartTen.getSuit());
        assertEquals("TEN", heartTen.getValue());
        assertEquals("SPADES", spadeKing.getSuit());
        assertEquals("KING", spadeKing.getValue());
        assertEquals("DIAMONDS", diamondAce.getSuit());
        assertEquals("ACE", diamondAce.getValue());
        assertEquals("CLUBS", clubThree.getSuit());
        assertEquals("THREE", clubThree.getValue());
    }
}