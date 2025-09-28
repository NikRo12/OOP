package ru.nsu.romanenko.model.card;

/**
 * Класс карты.
 *
 * @param suit масть
 * @param value значение
 */
public record Card(Suit suit, Value value) {

    /**
     * Получение названия масти.
     *
     * @return название масти
     */
    public String getSuit() {
        return suit.name();
    }

    /**
     * Получение названия значения карты.
     *
     * @return название значения
     */
    public String getValue() {
        return value.name();
    }

    /**
     * Получение числового значения карты.
     *
     * @return числовое значение карты
     */
    public int getNumericalValue() {
        return value.getNumericalValue();
    }
}