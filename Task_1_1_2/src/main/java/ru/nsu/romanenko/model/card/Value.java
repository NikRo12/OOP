package ru.nsu.romanenko.model.card;

/**
 * Перечисление для значения карты.
 */
public enum Value {
    TWO(2, "Two"),
    THREE(3, "Three"),
    FOUR(4, "Four"),
    FIVE(5, "Five"),
    SIX(6, "Six"),
    SEVEN(7, "Seven"),
    EIGHT(8, "Eight"),
    NINE(9, "Nine"),
    TEN(10, "Ten"),
    JACK(10, "Jack"),
    QUEEN(10, "Queen"),
    KING(10, "King"),
    ACE(11, "Ace");

    private final int numericalValue;
    private final String stringValue;

    Value(int numericalValue, String stringValue) {
        this.numericalValue = numericalValue;
        this.stringValue = stringValue;
    }

    public int getNumericalValue() {
        return numericalValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}