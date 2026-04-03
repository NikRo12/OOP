package ru.nsu.romanenko.controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.nsu.romanenko.model.Direction;

public class InputController {
    private final GameController gameController;

    public InputController(GameController gameController) {
        this.gameController = gameController;
    }

    public void handle(KeyEvent event) {
        KeyCode code = event.getCode();
        switch (code) {
            case UP    -> gameController.handleInput(Direction.UP);
            case DOWN  -> gameController.handleInput(Direction.DOWN);
            case LEFT  -> gameController.handleInput(Direction.LEFT);
            case RIGHT -> gameController.handleInput(Direction.RIGHT);
            case SPACE -> gameController.handleSpace();
        }
    }
}