package ru.nsu.romanenko;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

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
        final Card heartTen = new Card(Suit.HEARTS, Value.TEN);
        final Card spadeKing = new Card(Suit.SPADES, Value.KING);
        final Card diamondAce = new Card(Suit.DIAMONDS, Value.ACE);
        final Card clubThree = new Card(Suit.CLUBS, Value.THREE);

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