package ru.nsu.romanenko.controllerTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.controller.Controller;
import ru.nsu.romanenko.model.card.Card;
import ru.nsu.romanenko.model.game.PointsCounter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testInitRoundDistributesCards() throws Exception {
        Method initRoundMethod = Controller.class.getDeclaredMethod("initRound", PointsCounter.class, PointsCounter.class);
        initRoundMethod.setAccessible(true);

        PointsCounter user = new PointsCounter(false);
        PointsCounter dealer = new PointsCounter(true);

        Card hiddenCard = (Card) initRoundMethod.invoke(null, user, dealer);

        assertTrue(user.getPoints() > 0);
        assertTrue(dealer.getPoints() > 0);
        assertNotNull(hiddenCard);
    }

    @Test
    void testInitRoundGivesDifferentCards() throws Exception {
        Method initRoundMethod = Controller.class.getDeclaredMethod("initRound", PointsCounter.class, PointsCounter.class);
        initRoundMethod.setAccessible(true);

        PointsCounter user1 = new PointsCounter(false);
        PointsCounter dealer1 = new PointsCounter(true);
        PointsCounter user2 = new PointsCounter(false);
        PointsCounter dealer2 = new PointsCounter(true);

        Card hiddenCard1 = (Card) initRoundMethod.invoke(null, user1, dealer1);
        Card hiddenCard2 = (Card) initRoundMethod.invoke(null, user2, dealer2);

        assertNotEquals(user1.getPoints(), user2.getPoints());
        assertNotEquals(dealer1.getPoints(), dealer2.getPoints());
    }

    @Test
    void testUserGetsTwoCardsDealerGetsOne() throws Exception {
        Method initRoundMethod = Controller.class.getDeclaredMethod("initRound", PointsCounter.class, PointsCounter.class);
        initRoundMethod.setAccessible(true);

        PointsCounter user = new PointsCounter(false);
        PointsCounter dealer = new PointsCounter(true);

        Card hiddenCard = (Card) initRoundMethod.invoke(null, user, dealer);

        assertTrue(user.getPoints() > dealer.getPoints());
    }

    @Test
    void testHiddenCardIsValid() throws Exception {
        Method initRoundMethod = Controller.class.getDeclaredMethod("initRound", PointsCounter.class, PointsCounter.class);
        initRoundMethod.setAccessible(true);

        PointsCounter user = new PointsCounter(false);
        PointsCounter dealer = new PointsCounter(true);

        Card hiddenCard = (Card) initRoundMethod.invoke(null, user, dealer);

        assertNotNull(hiddenCard.getSuit());
        assertNotNull(hiddenCard.getValue());
        assertTrue(hiddenCard.getNumericalValue() >= 2 && hiddenCard.getNumericalValue() <= 11);
    }

    @Test
    void testMultipleInitRoundsWork() throws Exception {
        Method initRoundMethod = Controller.class.getDeclaredMethod("initRound", PointsCounter.class, PointsCounter.class);
        initRoundMethod.setAccessible(true);

        for (int i = 0; i < 5; i++) {
            PointsCounter user = new PointsCounter(false);
            PointsCounter dealer = new PointsCounter(true);
            Card hiddenCard = (Card) initRoundMethod.invoke(null, user, dealer);

            assertTrue(user.getPoints() > 0);
            assertTrue(dealer.getPoints() > 0);
            assertNotNull(hiddenCard);
        }
    }

    @Test
    void testUserAndDealerHaveReasonablePoints() throws Exception {
        Method initRoundMethod = Controller.class.getDeclaredMethod("initRound", PointsCounter.class, PointsCounter.class);
        initRoundMethod.setAccessible(true);

        PointsCounter user = new PointsCounter(false);
        PointsCounter dealer = new PointsCounter(true);

        Card hiddenCard = (Card) initRoundMethod.invoke(null, user, dealer);

        assertTrue(user.getPoints() >= 4 && user.getPoints() <= 21);
        assertTrue(dealer.getPoints() >= 2 && dealer.getPoints() <= 11);
    }
}