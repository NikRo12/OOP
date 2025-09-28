package ru.nsu.romanenko.tests.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.view.Localization;

class LocalizationTest {

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