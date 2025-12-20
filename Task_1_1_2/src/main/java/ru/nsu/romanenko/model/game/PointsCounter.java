package ru.nsu.romanenko.model.game;

import java.util.ArrayList;
import ru.nsu.romanenko.model.card.Card;

/**
 * Class implementing points counter for user and dealer.
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
    public PointsCounter(boolean isDealer) {
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
        int cardPoint = card.getNumericalValue();

        if (val.equals("ACE") && points + cardPoint > 21) {
            cardPoint = 1;
        }

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