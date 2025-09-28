package ru.nsu.romanenko.tests.model.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.model.card.Card;
import ru.nsu.romanenko.model.card.Suit;
import ru.nsu.romanenko.model.card.Value;
import ru.nsu.romanenko.model.game.PointsCounter;

import static org.junit.jupiter.api.Assertions.*;

class PointsCounterTest {

    private PointsCounter userPoints;
    private PointsCounter dealerPoints;

    @BeforeEach
    void setUp() {
        userPoints = new PointsCounter(false);
        dealerPoints = new PointsCounter(true);
    }

    @Test
    void testInitialPoints() {
        assertEquals(0, userPoints.getPoints());
        assertEquals(0, dealerPoints.getPoints());
    }

    @Test
    void testAppendCard() {
        Card card = new Card(Suit.HEARTS, Value.TEN);
        int pointsAdded = userPoints.appendCard(card);

        assertEquals(10, pointsAdded);
        assertEquals(10, userPoints.getPoints());
    }

    @Test
    void testMultipleCards() {
        Card card1 = new Card(Suit.HEARTS, Value.FIVE);
        Card card2 = new Card(Suit.SPADES, Value.SEVEN);

        userPoints.appendCard(card1);
        userPoints.appendCard(card2);

        assertEquals(12, userPoints.getPoints());
    }

    @Test
    void testAceAdjustment() {
        Card firstCard = new Card(Suit.HEARTS, Value.TEN);
        Card aceCard = new Card(Suit.SPADES, Value.ACE);

        userPoints.appendCard(firstCard);
        userPoints.appendCard(aceCard);

        assertEquals(21, userPoints.getPoints());
    }

    @Test
    void testAceAsEleven() {
        Card firstCard = new Card(Suit.HEARTS, Value.FIVE);
        Card aceCard = new Card(Suit.SPADES, Value.ACE);

        userPoints.appendCard(firstCard);
        userPoints.appendCard(aceCard);

        assertEquals(16, userPoints.getPoints());
    }

    @Test
    void testGetInformUser() {
        Card card1 = new Card(Suit.HEARTS, Value.TEN);
        Card card2 = new Card(Suit.DIAMONDS, Value.FIVE);

        userPoints.appendCard(card1);
        userPoints.appendCard(card2);

        String inform = userPoints.getInform();
        assertTrue(inform.contains("TEN HEARTS (10)"));
        assertTrue(inform.contains("FIVE DIAMONDS (5)"));
        assertTrue(inform.contains("15"));
    }

    @Test
    void testGetInformDealerWithOneCard() {
        Card card = new Card(Suit.CLUBS, Value.KING);
        dealerPoints.appendCard(card);

        String inform = dealerPoints.getInform();
        assertTrue(inform.contains("KING CLUBS (10)"));
        assertTrue(inform.contains("<closed card>"));
        assertTrue(inform.contains("10"));
    }

    @Test
    void testGetInformDealerWithMultipleCards() {
        Card card1 = new Card(Suit.CLUBS, Value.KING);
        Card card2 = new Card(Suit.HEARTS, Value.SIX);

        dealerPoints.appendCard(card1);
        dealerPoints.appendCard(card2);

        String inform = dealerPoints.getInform();
        assertTrue(inform.contains("KING CLUBS (10)"));
        assertTrue(inform.contains("SIX HEARTS (6)"));
        assertFalse(inform.contains("<closed card>"));
        assertTrue(inform.contains("16"));
    }
}