package ru.nsu.romanenko.tests.model.game;

import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.model.card.Card;
import ru.nsu.romanenko.model.card.Deck;
import ru.nsu.romanenko.model.card.Suit;
import ru.nsu.romanenko.model.card.Value;
import ru.nsu.romanenko.model.game.GamePlay;
import ru.nsu.romanenko.model.game.PointsCounter;
import ru.nsu.romanenko.local.Localization;

import static org.junit.jupiter.api.Assertions.*;

public class GamePlayTest
{
    @BeforeEach
    void setUp() {
        Localization.setLocale(new Locale("en"));
    }

    @Test
    void testDealerGameplay() {
        PointsCounter user = new PointsCounter(false);
        PointsCounter dealer = new PointsCounter(true);
        Deck deck = new Deck();

        dealer.appendCard(deck.getRandomCard());
        dealer.appendCard(deck.getRandomCard());

        int initialPoints = dealer.getPoints();

        assertDoesNotThrow(() -> {
            GamePlay.gaming(user, dealer, deck, false);
        });

        assertTrue(dealer.getPoints() >= 17 || dealer.getPoints() > initialPoints,
                "Dealer should have at least 17 points or more points than initially");
    }

    @Test
    void testDealerStopsAtSeventeenOrMore() {
        PointsCounter user = new PointsCounter(false);
        PointsCounter dealer = new PointsCounter(true);
        Deck deck = new Deck();

        GamePlay.gaming(user, dealer, deck, false);

        assertTrue(dealer.getPoints() >= 17,
                "Dealer should have at least 17 points or be busted");
    }

    @Test
    void testThreeAcesScenario() {
        PointsCounter player = new PointsCounter(false);

        Card ace1 = new Card(Suit.HEARTS, Value.ACE);
        Card ace2 = new Card(Suit.SPADES, Value.ACE);
        Card ace3 = new Card(Suit.DIAMONDS, Value.ACE);

        player.appendCard(ace1);
        player.appendCard(ace2);
        player.appendCard(ace3);

        assertEquals(13, player.getPoints(),
                "Player should have 13 points with three aces (11 + 1 + 1)");
    }

    @Test
    void testAceValueAdjustmentWithHighCards() {
        PointsCounter player = new PointsCounter(false);

        Card king = new Card(Suit.HEARTS, Value.KING);
        Card queen = new Card(Suit.SPADES, Value.QUEEN);
        Card ace = new Card(Suit.DIAMONDS, Value.ACE);

        player.appendCard(king);
        player.appendCard(queen);
        player.appendCard(ace);

        assertEquals(21, player.getPoints());
    }

    @Test
    void testMultipleAcesComplexScenario() {
        PointsCounter player = new PointsCounter(false);

        Card ace1 = new Card(Suit.HEARTS, Value.ACE);
        Card king = new Card(Suit.SPADES, Value.KING);
        Card ace2 = new Card(Suit.DIAMONDS, Value.ACE);

        player.appendCard(ace1);
        player.appendCard(king);
        player.appendCard(ace2);

        assertEquals(22, player.getPoints());
    }

    @Test
    void testAceStaysElevenWhenPossible() {
        PointsCounter player = new PointsCounter(false);

        Card ace = new Card(Suit.HEARTS, Value.ACE);
        Card six = new Card(Suit.SPADES, Value.SIX);

        player.appendCard(ace);
        player.appendCard(six);

        assertEquals(17, player.getPoints(),
                "Ace should remain 11 when it doesn't cause bust");
    }
}