package ru.nsu.romanenko.tests.view;

import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.local.Localization;
import ru.nsu.romanenko.model.game.PointsCounter;

import static org.junit.jupiter.api.Assertions.*;

class LocalizationTest {

    @BeforeEach
    void setUp() {
        Localization.setLocale(new Locale("en"));
    }

    @Test
    void testGetAllEnglishMessages() {
        assertEquals("Welcome to BlackJack", Localization.get("welcome"));
        assertEquals("Round", Localization.get("round"));
        assertEquals("You win", Localization.get("win"));
        assertEquals("You lost", Localization.get("lose"));
    }

    @Test
    void testGetUnknownKeyReturnsKey() {
        assertEquals("unknown_key", Localization.get("unknown_key"));
    }

    @Test
    void testLocalizationConsistency() {
        String welcome = Localization.get("welcome");
        String round = Localization.get("round");
        String win = Localization.get("win");

        assertNotNull(welcome);
        assertNotNull(round);
        assertNotNull(win);
        assertFalse(welcome.isEmpty());
        assertFalse(round.isEmpty());
        assertFalse(win.isEmpty());
    }
}