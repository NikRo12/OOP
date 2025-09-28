package ru.nsu.romanenko.tests.view;

import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.view.Localization;

import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

class LocalizationTest {

    @Test
    void testDefaultEnglishLocalization() {
        assertEquals("Welcome to BlackJack", Localization.get("welcome"));
        assertEquals("Round", Localization.get("round"));
        assertEquals("You win", Localization.get("win"));
        assertEquals("You lost", Localization.get("lose"));
    }

    @Test
    void testUnknownKeyReturnsKey() {
        assertEquals("unknown_key", Localization.get("unknown_key"));
    }

    @Test
    void testLocaleChange() {
        Locale originalLocale = Locale.getDefault();

        try {
            Localization.setLocale(Locale.US);
            assertEquals("Welcome to BlackJack", Localization.get("welcome"));

            Localization.setLocale(Locale.FRENCH);
            assertEquals("Welcome to BlackJack", Localization.get("welcome"));
        } finally {
            Localization.setLocale(originalLocale);
        }
    }
}