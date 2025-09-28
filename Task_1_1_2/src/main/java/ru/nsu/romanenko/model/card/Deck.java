package ru.nsu.romanenko.model.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Класс колоды карт.
 */
public class Deck {
    private final ArrayList<Card> cards = new ArrayList<>();

    /**
     * Конструктор колоды.
     */
    public Deck() {
        initializeDeck();
    }

    /**
     * Заполнение колоды картами и их перемешивание.
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
     * Перезаполнение колоды в случае недостатка карт.
     */
    public void refill() {
        initializeDeck();
    }

    /**
     * Извлечение случайной карты из колоды.
     *
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