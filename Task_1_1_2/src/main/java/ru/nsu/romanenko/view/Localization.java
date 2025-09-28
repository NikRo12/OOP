package ru.nsu.romanenko.view;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Localization {
    private static Locale currentLocale = Locale.CHINA;

    private static final Map<String, String> EN = new HashMap<>();

    static {
        EN.put("welcome", "Welcome to BlackJack");
        EN.put("round", "Round");
        EN.put("your_cards", "Your cards");
        EN.put("dealer_cards", "Dealer's cards");
        EN.put("dealer_puts", "Dealer puts cards");
        EN.put("your_move", "Your move");
        EN.put("dealer_move", "Dealer's move");
        EN.put("dealer_opened", "Dealer opened closed card");
        EN.put("open_card_user", "You've opened card: ");
        EN.put("open_card_dealer", "Dealer opened card: ");
        EN.put("choose_action", "Press \"1\" to get card or \"0\" to pass...");
        EN.put("choose_action_error", "Please enter only 1 or 0");
        EN.put("scanner_error", "Input error, please try again");
        EN.put("win", "You win");
        EN.put("lose", "You lost");
        EN.put("total_score", "Total score:");
        EN.put("your_score", "Your score is --- ");
        EN.put("dealer_score", "Dealer's score is --- ");
        EN.put("game_over", "=== GAME OVER ===");
        EN.put("final_score", "Final score:");
        EN.put("congratulations", "Congratulations! You won the game!");
        EN.put("dealer_wins", "Dealer wins the game. Better luck next time!");
        EN.put("tie", "It's a tie!");
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
    }

    public static String get(String key) {
        switch (currentLocale.getLanguage()) {
            default:
                return EN.getOrDefault(key, key);
        }
    }
}
