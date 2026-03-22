package ru.nsu.romanenko.model.food;

import ru.nsu.romanenko.model.Snake;

public class Apple implements Food{
    private static final int GROW_UP_VALUE = 1;

    @Override
    public void apply(Snake snake) {
        snake.growUp(GROW_UP_VALUE);
    }
}
