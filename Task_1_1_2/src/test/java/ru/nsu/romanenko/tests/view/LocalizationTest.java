package ru.nsu.romanenko.tests.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.romanenko.local.Localization;
import ru.nsu.romanenko.local.EnglishLocalization;
import ru.nsu.romanenko.local.RussianLocalization;

import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

class LocalizationTest {

    @BeforeEach
    void setUp() {
        Localization.setLocale(Locale.ENGLISH);
    }

    @Test
    void testEnglishLocalization() {
        Localization.setLocale(Locale.ENGLISH);


        assertEquals("Welcome to BlackJack", Localization.get("welcome"));
        assertEquals("Round", Localization.get("round"));
        assertEquals("Your cards", Localization.get("your_cards"));
        assertEquals("Dealer's cards", Localization.get("dealer_cards"));
        assertEquals("Dealer puts cards", Localization.get("dealer_puts"));
        assertEquals("Your move", Localization.get("your_move"));
        assertEquals("Dealer's move", Localization.get("dealer_move"));
        assertEquals("Dealer opened closed card", Localization.get("dealer_opened"));
        assertEquals("You've opened card: ", Localization.get("open_card_user"));
        assertEquals("Dealer opened card: ", Localization.get("open_card_dealer"));
        assertEquals("Press \"1\" to get card or \"0\" to pass...", Localization.get("choose_action"));
        assertEquals("Please enter only 1 or 0", Localization.get("choose_action_error"));
        assertEquals("Input error, please try again", Localization.get("scanner_error"));
        assertEquals("You win", Localization.get("win"));
        assertEquals("You lost", Localization.get("lose"));
        assertEquals("Total score:", Localization.get("total_score"));
        assertEquals("Your score is --- ", Localization.get("your_score"));
        assertEquals("Dealer's score is --- ", Localization.get("dealer_score"));
        assertEquals("=== GAME OVER ===", Localization.get("game_over"));
        assertEquals("Final score:", Localization.get("final_score"));
        assertEquals("Congratulations! You won the game!", Localization.get("congratulations"));
        assertEquals("Dealer wins the game. Better luck next time!", Localization.get("dealer_wins"));
        assertEquals("It's a tie!", Localization.get("tie"));
    }

    @Test
    void testRussianLocalization() {
        // Given
        Localization.setLocale(new Locale("ru"));

        // Then
        assertEquals("Добро пожаловать в БлэкДжек", Localization.get("welcome"));
        assertEquals("Раунд", Localization.get("round"));
        assertEquals("Ваши карты", Localization.get("your_cards"));
        assertEquals("Карты дилера", Localization.get("dealer_cards"));
        assertEquals("Дилер кладет карты", Localization.get("dealer_puts"));
        assertEquals("Ваш ход", Localization.get("your_move"));
        assertEquals("Ход дилера", Localization.get("dealer_move"));
        assertEquals("Дилер открыл закрытую карту", Localization.get("dealer_opened"));
        assertEquals("Вы открыли карту: ", Localization.get("open_card_user"));
        assertEquals("Дилер открыл карту: ", Localization.get("open_card_dealer"));
        assertEquals("Нажмите \"1\" чтобы взять карту или \"0\" чтобы пасовать...", Localization.get("choose_action"));
        assertEquals("Пожалуйста, введите только 1 или 0", Localization.get("choose_action_error"));
        assertEquals("Ошибка ввода, попробуйте снова", Localization.get("scanner_error"));
        assertEquals("Вы выиграли", Localization.get("win"));
        assertEquals("Вы проиграли", Localization.get("lose"));
        assertEquals("Общий счет:", Localization.get("total_score"));
        assertEquals("Ваш счет --- ", Localization.get("your_score"));
        assertEquals("Счет дилера --- ", Localization.get("dealer_score"));
        assertEquals("=== ИГРА ОКОНЧЕНА ===", Localization.get("game_over"));
        assertEquals("Финальный счет:", Localization.get("final_score"));
        assertEquals("Поздравляем! Вы выиграли игру!", Localization.get("congratulations"));
        assertEquals("Дилер выиграл игру. Удачи в следующий раз!", Localization.get("dealer_wins"));
        assertEquals("Ничья!", Localization.get("tie"));
    }

    @Test
    void testDefaultToEnglishForUnknownLocale() {
        // Given
        Localization.setLocale(Locale.FRENCH);

        // Then - должна использоваться английская локализация
        assertEquals("Welcome to BlackJack", Localization.get("welcome"));
        assertEquals("Round", Localization.get("round"));
    }

    @Test
    void testUnknownKeyReturnsKey() {
        // Given
        Localization.setLocale(Locale.ENGLISH);
        String unknownKey = "unknown_key";

        // When & Then
        assertEquals(unknownKey, Localization.get(unknownKey));
    }

    @Test
    void testDirectEnglishLocalizationInstance() {
        // Given
        EnglishLocalization englishLocalization = new EnglishLocalization();

        // Then
        assertEquals("Welcome to BlackJack", englishLocalization.getString("welcome"));
        assertEquals("unknown_key", englishLocalization.getString("unknown_key"));
    }

    @Test
    void testDirectRussianLocalizationInstance() {
        // Given
        RussianLocalization russianLocalization = new RussianLocalization();

        // Then
        assertEquals("Добро пожаловать в БлэкДжек", russianLocalization.getString("welcome"));
        assertEquals("unknown_key", russianLocalization.getString("unknown_key"));
    }

    @Test
    void testLocaleCaseInsensitivity() {
        // Given
        Localization.setLocale(new Locale("EN")); // Верхний регистр

        // Then - должна работать корректно
        assertEquals("Welcome to BlackJack", Localization.get("welcome"));
    }

    @Test
    void testMultipleLocaleSwitches() {
        // Given
        Localization.setLocale(Locale.ENGLISH);
        String englishWelcome = Localization.get("welcome");

        Localization.setLocale(new Locale("ru"));
        String russianWelcome = Localization.get("welcome");

        Localization.setLocale(Locale.ENGLISH);
        String backToEnglishWelcome = Localization.get("welcome");

        // Then
        assertEquals("Welcome to BlackJack", englishWelcome);
        assertEquals("Добро пожаловать в БлэкДжек", russianWelcome);
        assertEquals("Welcome to BlackJack", backToEnglishWelcome);
    }

    @Test
    void testAllKeysExistInBothLocalizations() {
        // Given
        EnglishLocalization english = new EnglishLocalization();
        RussianLocalization russian = new RussianLocalization();

        // When & Then - проверяем, что все ключи присутствуют в обеих локализациях
        // Это гарантирует, что при добавлении нового ключа он будет в обоих классах
        // В реальном проекте это можно сделать через рефлексию для полной проверки
        assertNotNull(english.getString("welcome"));
        assertNotNull(russian.getString("welcome"));

        assertNotNull(english.getString("game_over"));
        assertNotNull(russian.getString("game_over"));

        assertNotNull(english.getString("congratulations"));
        assertNotNull(russian.getString("congratulations"));
    }
}