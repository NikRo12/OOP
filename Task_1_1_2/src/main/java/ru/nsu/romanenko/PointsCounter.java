package ru.nsu.romanenko;

import java.util.ArrayList;

/**
 * Класс реализующий счетчик очков для пользователя и дилера.
 */
public class PointsCounter {
    private int points;
    private record StringCard(String value, String suit, int cardPoint) { }
    private final ArrayList<StringCard> cardPoints = new ArrayList<>();
    private final boolean isDealer;

    /**
     * Конструктор счетчика очков.
     *
     * @param isDealer флаг, указывающий является ли игрок дилером
     */
    PointsCounter(boolean isDealer) {
        points = 0;
        this.isDealer = isDealer;
    }

    /**
     * Добавление карты в руки игрока (пользователь или дилер).
     *
     * @param card любая карта класса Card
     * @return количество очков, которая дает эта карта
     */
    public int appendCard(Card card) {
        String val = card.getValue();
        String suit = card.getSuit();

        int cardPoint = switch (val) {
            case "TWO" -> 2;
            case "THREE" -> 3;
            case "FOUR" -> 4;
            case "FIVE" -> 5;
            case "SIX" -> 6;
            case "SEVEN" -> 7;
            case "EIGHT" -> 8;
            case "NINE" -> 9;
            case "TEN", "JACK", "QUEEN", "KING" -> 10;
            case "ACE" -> {
                if (points + 11 <= 21) {
                    yield 11;
                } else {
                    yield 1;
                }
            }
            default -> throw new IllegalArgumentException("Unknown card: " + val);
        };

        cardPoints.add(new StringCard(val, suit, cardPoint));
        points += cardPoint;
        return cardPoint;
    }

    /**
     * Информация по игроку (карты, очки, общие очки).
     *
     * @return соответствующую строку информации
     */
    public String getInform() {
        StringBuilder result = new StringBuilder("[");
        int count = 0;
        int total = cardPoints.size();

        for (StringCard entry : cardPoints) {
            String value = entry.value();
            String suit = entry.suit();
            Integer cardPoint = entry.cardPoint();

            result.append(value).append(" ").append(suit)
                    .append(" (").append(cardPoint).append(")");

            if (++count < total) {
                result.append(", ");
            }
        }

        if (isDealer && cardPoints.size() == 1) {
            result.append(", <closed card>");
        }

        result.append("] => ").append(this.points);
        return result.toString();
    }

    /**
     * Получение текущего количества очков.
     *
     * @return количество очков
     */
    public int getPoints() {
        return points;
    }
}