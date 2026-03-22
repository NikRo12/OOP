package ru.nsu.romanenko.model;

public enum GameState {
    WAITING,
    RUNNING,
    WON,
    LOST;

    public String getMessage() {
        return switch (this) {
            case WAITING -> "Press SPACE to start";
            case RUNNING -> "";
            case WON -> "You won!";
            case LOST -> "Game Over";
        };
    }
}
