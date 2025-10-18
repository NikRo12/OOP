package ru.nsu.romanenko.tests.model.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.model.card.Card;
import ru.nsu.romanenko.model.card.Deck;

class DeckTest {

    @Test
    void testDeckInitialization() {
        Deck deck = new Deck();

        Card card = deck.getRandomCard();
        assertNotNull(card);
        assertNotNull(card.suit());
        assertNotNull(card.value());
    }

    @Test
    void testDeckContainsAllCards() {
        Deck deck = new Deck();
        Set<Card> uniqueCards = new HashSet<>();

        for (int i = 0; i < 52; i++) {
            Card card = deck.getRandomCard();
            uniqueCards.add(card);
        }

        assertEquals(52, uniqueCards.size());
    }

    @Test
    void testDeckRefill() {
        Deck deck = new Deck();

        for (int i = 0; i < 52; i++) {
            deck.getRandomCard();
        }

        Card card = deck.getRandomCard();
        assertNotNull(card);
    }

    @Test
    void testRandomCardDistribution() {
        Deck deck = new Deck();
        Set<Card> firstDraw = new HashSet<>();
        Set<Card> secondDraw = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            firstDraw.add(deck.getRandomCard());
        }

        Deck anotherDeck = new Deck();
        for (int i = 0; i < 10; i++) {
            secondDraw.add(anotherDeck.getRandomCard());
        }

        assertNotEquals(firstDraw, secondDraw);
    }
}