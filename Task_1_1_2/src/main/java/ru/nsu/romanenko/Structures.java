package ru.nsu.romanenko;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * Перечисление для масти
 */
enum Suit {
    Hearts,
    Spades,
    Diamonds,
    Clubs
}

/**
 * Перечисление для значения карты
 */
enum Value {
    Two,
    Three,
    Four,
    Five,
    Six,
    Seven,
    Eight,
    Nine,
    Ten,
    Jack,
    Queen,
    King,
    Ace
}

/**
 * Класс карты
 * @param suit масть
 * @param value значение
 */

record Card(Suit suit, Value value) {

    public String getSuit() {
        return suit.name();
    }

    public String getValue() {
        return value.name();
    }
}

/**
 * Класс колоды
 */

class Deck {
    private final ArrayList<Card> cards = new ArrayList<>();

    public Deck() {
        initializeDeck();
    }

    /**
     * Заполнение колоды картами и их тусовка
     */

    private void initializeDeck() {
        for (Suit suit : Suit.values()) {
            for (Value value : Value.values()) {
                cards.add(new Card(suit, value));
            }
        }

        Collections.shuffle(cards);
    }

    /**
     * Перезаполнение колоды в случае недостатка карт
     */

    public void refill() {
        initializeDeck();
    }


    /**
     * Извлечение случайной карты из колоды
     * @return случайная карта класса Card
     */
    public Card getRandomCard() {
        if (cards.isEmpty()) {
            refill();
        }

        Random rand = new Random();
        int randomIndex = rand.nextInt(cards.size());
        return cards.remove(randomIndex);
    }
}