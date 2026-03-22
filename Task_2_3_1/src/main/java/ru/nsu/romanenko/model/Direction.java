package ru.nsu.romanenko.model;

public enum Direction {
    UP,
    DOWN,
    RIGHT,
    LEFT;

    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case RIGHT -> LEFT;
            case LEFT -> RIGHT;
        };
    }

    public Position next(Position current) {
        return switch (this) {
            case UP -> new Position(current.getHorizontal(), current.getVertical() - 1);
            case DOWN -> new Position(current.getHorizontal(), current.getVertical() + 1);
            case RIGHT -> new Position(current.getHorizontal() + 1, current.getVertical());
            case LEFT -> new Position(current.getHorizontal() - 1, current.getVertical());
        };
    }
}


