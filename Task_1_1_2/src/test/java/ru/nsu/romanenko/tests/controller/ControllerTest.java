package ru.nsu.romanenko.tests.controller;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.nsu.romanenko.controller.Controller;
import ru.nsu.romanenko.model.card.Card;
import ru.nsu.romanenko.model.game.PointsCounter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        Method initRoundMethod = Controller.class.getDeclaredMethod("initRound",
                PointsCounter.class, PointsCounter.class);
        initRoundMethod.setAccessible(true);

        PointsCounter user = new PointsCounter(false);
        PointsCounter dealer = new PointsCounter(true);

        Card hiddenCard = (Card) initRoundMethod.invoke(null, user, dealer);

        assertTrue(user.getPoints() > 0);
        assertTrue(dealer.getPoints() > 0);
        assertNotNull(hiddenCard);
    }
}