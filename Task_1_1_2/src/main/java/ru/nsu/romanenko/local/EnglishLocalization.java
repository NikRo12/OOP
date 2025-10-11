package ru.nsu.romanenko.local;

import java.util.HashMap;
import java.util.Map;

public class EnglishLocalization extends Localization {
    private final Map<String, String> strings = new HashMap<>();

    public EnglishLocalization() {
        strings.put("welcome", "Welcome to BlackJack");
        strings.put("round", "Round");
        strings.put("your_cards", "Your cards");
        strings.put("dealer_cards", "Dealer's cards");
        strings.put("dealer_puts", "Dealer puts cards");
        strings.put("your_move", "Your move");
        strings.put("dealer_move", "Dealer's move");
        strings.put("dealer_opened", "Dealer opened closed card");
        strings.put("open_card_user", "You've opened card: ");
        strings.put("open_card_dealer", "Dealer opened card: ");
        strings.put("choose_action", "Press \"1\" to get card or \"0\" to pass...");
        strings.put("choose_action_error", "Please enter only 1 or 0");
        strings.put("scanner_error", "Input error, please try again");
        strings.put("win", "You win");
        strings.put("lose", "You lost");
        strings.put("total_score", "Total score:");
        strings.put("your_score", "Your score is --- ");
        strings.put("dealer_score", "Dealer's score is --- ");
        strings.put("game_over", "=== GAME OVER ===");
        strings.put("final_score", "Final score:");
        strings.put("congratulations", "Congratulations! You won the game!");
        strings.put("dealer_wins", "Dealer wins the game. Better luck next time!");
        strings.put("tie", "It's a tie!");
    }

    @Override
    protected String getString(String key) {
        return strings.getOrDefault(key, key);
    }
}