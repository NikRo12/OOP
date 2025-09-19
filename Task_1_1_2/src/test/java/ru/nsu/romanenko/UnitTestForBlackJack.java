package ru.nsu.romanenko;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.util.Scanner;

class PointsCounterTest {

    @Test
    void testAppendCardNonAce() {
        PointsCounter counter = new PointsCounter(0);
        Card card = new Card(Suit.Hearts, Value.Ten);
        int points = counter.appendCard(card);
        assertEquals(10, points);
        assertEquals(10, counter.getPoints());
    }

    @Test
    void testAppendCardAceLowPoints() {
        PointsCounter counter = new PointsCounter(0);
        Card ace = new Card(Suit.Hearts, Value.Ace);
        int points = counter.appendCard(ace);
        assertEquals(11, points);
        assertEquals(11, counter.getPoints());
    }

    @Test
    void testAppendCardAceHighPoints() {
        PointsCounter counter = new PointsCounter(0);
        Card ten = new Card(Suit.Hearts, Value.Ten);
        Card ace = new Card(Suit.Spades, Value.Ace);

        counter.appendCard(ten);
        counter.appendCard(ten); // 20 points
        int points = counter.appendCard(ace);

        assertEquals(1, points);
        assertEquals(21, counter.getPoints());
    }

    @Test
    void testGetInformUser() {
        PointsCounter counter = new PointsCounter(0);
        Card card1 = new Card(Suit.Hearts, Value.Ten);
        Card card2 = new Card(Suit.Spades, Value.Ace);

        counter.appendCard(card1);
        counter.appendCard(card2);

        String inform = counter.getInform();
        assertTrue(inform.contains("Ten Hearts (10)"));
        assertTrue(inform.contains("Ace Spades (11)"));
        assertTrue(inform.contains("=> 21"));
    }

    @Test
    void testGetInformDealer() {
        PointsCounter counter = new PointsCounter(1);
        Card card = new Card(Suit.Hearts, Value.Ten);
        counter.appendCard(card);

        String inform = counter.getInform();
        assertTrue(inform.contains("<closed card>"));
    }
}

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

class GamePlayTest {

    @Test
    void testUserTurn() {
        PointsCounter user = new PointsCounter(0);
        PointsCounter dealer = new PointsCounter(1);
        Deck deck = new Deck();
        String input = "1\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        GamePlay.Gaming(user, dealer, deck, 1, scanner);

        assertTrue(user.getPoints() >= 0);
    }

    @Test
    void testDealerTurn() {
        PointsCounter user = new PointsCounter(0);
        PointsCounter dealer = new PointsCounter(1);
        Deck deck = new Deck();
        Scanner scanner = new Scanner(System.in);

        GamePlay.Gaming(user, dealer, deck, 0, scanner);

        assertTrue(dealer.getPoints() >= 17 || dealer.getPoints() == 0);
    }
}

class CardTest {

    @Test
    void testCardCreation() {
        Card card = new Card(Suit.Hearts, Value.Ace);
        assertEquals("Hearts", card.getSuit());
        assertEquals("Ace", card.getValue());
    }
}