package ru.nsu.romanenko.local;

import java.util.HashMap;
import java.util.Map;

public class RussianLocalization extends Localization {
    private final Map<String, String> strings = new HashMap<>();

    public RussianLocalization() {
        strings.put("welcome", "Добро пожаловать в БлэкДжек");
        strings.put("round", "Раунд");
        strings.put("your_cards", "Ваши карты");
        strings.put("dealer_cards", "Карты дилера");
        strings.put("dealer_puts", "Дилер кладет карты");
        strings.put("your_move", "Ваш ход");
        strings.put("dealer_move", "Ход дилера");
        strings.put("dealer_opened", "Дилер открыл закрытую карту");
        strings.put("open_card_user", "Вы открыли карту: ");
        strings.put("open_card_dealer", "Дилер открыл карту: ");
        strings.put("choose_action", "Нажмите \"1\" чтобы взять карту или \"0\" чтобы пасовать...");
        strings.put("choose_action_error", "Пожалуйста, введите только 1 или 0");
        strings.put("scanner_error", "Ошибка ввода, попробуйте снова");
        strings.put("win", "Вы выиграли");
        strings.put("lose", "Вы проиграли");
        strings.put("total_score", "Общий счет:");
        strings.put("your_score", "Ваш счет --- ");
        strings.put("dealer_score", "Счет дилера --- ");
        strings.put("game_over", "=== ИГРА ОКОНЧЕНА ===");
        strings.put("final_score", "Финальный счет:");
        strings.put("congratulations", "Поздравляем! Вы выиграли игру!");
        strings.put("dealer_wins", "Дилер выиграл игру. Удачи в следующий раз!");
        strings.put("tie", "Ничья!");
    }

    @Override
    public String getString(String key) {
        return strings.getOrDefault(key, key);
    }
}