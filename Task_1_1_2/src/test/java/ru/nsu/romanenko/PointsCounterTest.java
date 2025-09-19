package ru.nsu.romanenko;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для класса PointsCounter.
 */
class PointsCounterTest {

    @Test
    void testAppendCardNonAce() {
        PointsCounter counter = new PointsCounter(false);
        Card card = new Card(Suit.HEARTS, Value.TEN);
        int points = counter.appendCard(card);
        assertEquals(10, points);
        assertEquals(10, counter.getPoints());
    }

    @Test
    void testAppendCardAceLowPoints() {
        PointsCounter counter = new PointsCounter(false);
        Card ace = new Card(Suit.HEARTS, Value.ACE);
        int points = counter.appendCard(ace);
        assertEquals(11, points);
        assertEquals(11, counter.getPoints());
    }

    @Test
    void testAppendCardAceHighPoints() {
        PointsCounter counter = new PointsCounter(false);
        Card ten = new Card(Suit.HEARTS, Value.TEN);
        Card ace = new Card(Suit.SPADES, Value.ACE);

        counter.appendCard(ten);
        counter.appendCard(ten); // 20 points
        int points = counter.appendCard(ace);

        assertEquals(1, points);
        assertEquals(21, counter.getPoints());
    }

    @Test
    void testGetInformUser() {
        PointsCounter counter = new PointsCounter(false);
        Card card1 = new Card(Suit.HEARTS, Value.TEN);
        Card card2 = new Card(Suit.SPADES, Value.ACE);

        counter.appendCard(card1);
        counter.appendCard(card2);

        String inform = counter.getInform();
        assertTrue(inform.contains("TEN HEARTS (10)"));
        assertTrue(inform.contains("ACE SPADES (11)"));
        assertTrue(inform.contains("=> 21"));
    }

    @Test
    void testGetInformDealer() {
        PointsCounter counter = new PointsCounter(true);
        Card card = new Card(Suit.HEARTS, Value.TEN);
        counter.appendCard(card);

        String inform = counter.getInform();
        assertTrue(inform.contains("<closed card>"));
    }
}