package ru.nsu.romanenko.model.food;

import ru.nsu.romanenko.model.Snake;

public class GoldenApple implements Food{
    private static final int GROW_UP_VALUE = 2;

    @Override
    public void apply(Snake snake) {
        snake.growUp(GROW_UP_VALUE);
    }
}
