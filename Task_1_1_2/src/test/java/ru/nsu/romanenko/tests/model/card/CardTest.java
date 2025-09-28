package ru.nsu.romanenko.tests.model.card;

import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.model.card.Card;
import ru.nsu.romanenko.model.card.Suit;
import ru.nsu.romanenko.model.card.Value;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void testCardCreation() {
        Card card = new Card(Suit.HEARTS, Value.ACE);

        assertEquals(Suit.HEARTS, card.suit());
        assertEquals(Value.ACE, card.value());
    }

    @Test
    void testGetSuit() {
        Card card = new Card(Suit.DIAMONDS, Value.KING);

        assertEquals("DIAMONDS", card.getSuit());
    }

    @Test
    void testGetValue() {
        Card card = new Card(Suit.CLUBS, Value.QUEEN);

        assertEquals("QUEEN", card.getValue());
    }

    @Test
    void testGetNumericalValue() {
        Card card = new Card(Suit.SPADES, Value.TEN);

        assertEquals(10, card.getNumericalValue());
    }

    @Test
    void testAceNumericalValue() {
        Card card = new Card(Suit.HEARTS, Value.ACE);

        assertEquals(11, card.getNumericalValue());
    }
}