package ru.nsu.romanenko.model.card;

public record Card(Suit suit, Value value) {

    public String getSuit() {
        return suit.name();
    }

    public String getValue() {
        return value.name();
    }

    public int getNumericalValue() {
        return value.getNumericalValue();
    }
}