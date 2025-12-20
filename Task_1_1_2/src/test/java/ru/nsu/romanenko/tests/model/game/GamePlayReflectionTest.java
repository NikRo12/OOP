package ru.nsu.romanenko.tests.model.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.nsu.romanenko.model.game.Handler.pressOne;

import java.lang.reflect.Method;
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

public class GamePlayReflectionTest {

    @BeforeEach
    void setUp() {
        Localization.setLocale(new Locale("en"));
    }

    @Test
    void testPressOneUserIncrementsPoints() throws Exception {
        PointsCounter user = new PointsCounter(false);
        PointsCounter dealer = new PointsCounter(true);
        Deck deck = new Deck();

        int initialPoints = user.getPoints();
        pressOne(user, dealer, deck, true);

        assertTrue(user.getPoints() > initialPoints);
    }

    @Test
    void testPressOneDealerIncrementsPoints() throws Exception {
        PointsCounter user = new PointsCounter(false);
        PointsCounter dealer = new PointsCounter(true);
        Deck deck = new Deck();

        int initialPoints = dealer.getPoints();
        pressOne(user, dealer, deck, false);

        assertTrue(dealer.getPoints() > initialPoints);
    }

    @Test
    void testPressOneWithAceUser() throws Exception {
        PointsCounter user = new PointsCounter(false);
        PointsCounter dealer = new PointsCounter(true);

        Deck deck = new Deck() {
            @Override
            public Card getRandomCard() {
                return new Card(Suit.HEARTS, Value.ACE);
            }
        };
        pressOne(user, dealer, deck, true);

        assertEquals(11, user.getPoints());
    }

    @Test
    void testPressOneWithKingDealer() throws Exception {
        PointsCounter user = new PointsCounter(false);
        PointsCounter dealer = new PointsCounter(true);

        Deck deck = new Deck() {
            @Override
            public Card getRandomCard() {
                return new Card(Suit.SPADES, Value.KING);
            }
        };
        pressOne(user, dealer, deck, false);

        assertEquals(10, dealer.getPoints());
    }

    @Test
    void testPressOneMultipleCallsUser() throws Exception {
        PointsCounter user = new PointsCounter(false);
        PointsCounter dealer = new PointsCounter(true);
        Deck deck = new Deck();

        int pointsAfterFirst = 0;
        int pointsAfterSecond = 0;

        pressOne(user, dealer, deck, true);
        pointsAfterFirst = user.getPoints();

        pressOne(user, dealer, deck, true);
        pointsAfterSecond = user.getPoints();

        assertTrue(pointsAfterSecond > pointsAfterFirst);
    }
}