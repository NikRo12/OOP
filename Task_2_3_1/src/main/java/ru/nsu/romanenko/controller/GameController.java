package ru.nsu.romanenko.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import ru.nsu.romanenko.model.*;
import ru.nsu.romanenko.model.food.Apple;
import ru.nsu.romanenko.model.food.Food;
import ru.nsu.romanenko.model.food.GoldenApple;
import ru.nsu.romanenko.view.GameView;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private GameField field;
    private final GameConfig config;
    private GameState state;
    private Timeline timeline;
    private GameView view;

    private Snake playerSnake;
    private int startSpeed;
    private int speed;
    private static final int CONST_SPEED_UP = -100;

    public GameController(GameConfig config) {
        this.config = config;
        this.state = GameState.WAITING;
        initSnakeAndField();
    }

    private void initSnakeAndField() {
        List<Food> foodTypes = new ArrayList<>();
        foodTypes.add(new Apple());
        foodTypes.add(new GoldenApple());

        playerSnake = new Snake(
                new Position(config.fieldSizeN() / 2, config.fieldSizeM() / 2),
                Direction.DOWN
        );
        field = new GameField(config, foodTypes, playerSnake);

        this.startSpeed = config.startSpeed();
        this.speed = startSpeed;
    }

    public void start() {
        field.init();
        setSpeed(speed);
        if (view != null) {
            view.render(field, state);
        }
    }

    public void handleSpace() {
        if (state == GameState.WAITING) {
            state = GameState.RUNNING;
            timeline.play();
        } else if (state == GameState.LOST || state == GameState.WON) {
            stop();
            initSnakeAndField();

            if (state == GameState.WON) {
                speed += CONST_SPEED_UP;
            }

            if (state == GameState.LOST) {
                speed = startSpeed;
            }

            state = GameState.WAITING;
            field.init();
            setSpeed(speed);
            if (view != null) {
                view.render(field, state);
            }
        }
    }

    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    private void tick() {
        if (state != GameState.RUNNING) {
            stop();
            return;
        }

        playerSnake.move();
        playerSnake.wrapHead(field.getHorizontalSize(), field.getVerticalSize());
        handleCollision(playerSnake);

        if (view != null) {
            view.render(field, state);
        }

        if (state != GameState.RUNNING) {
            stop();
        }
    }

    private void handleCollision(Snake snake) {
        Position head = snake.getHead();

        if (field.isObstacle(head)) {
            resolveSnakeDeath(snake);
            return;
        }

        List<Position> body = playerSnake.getBody();
        int startIndex = (playerSnake == snake) ? 1 : 0;
        for (int i = startIndex; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                resolveSnakeDeath(snake);
                return;
            }
        }

        if (field.isFood(head)) {
            Food food = field.getFoodAt(head);
            food.apply(snake);
            field.replaceFood(head);

            if (snake == playerSnake && snake.getSize() >= config.winCells()) {
                state = GameState.WON;
            }
        }
    }

    private void resolveSnakeDeath(Snake snake) {
        if (snake == playerSnake) {
            state = GameState.LOST;
        }
    }

    private void setSpeed(int millisPerTick) {
        boolean wasRunning = timeline != null
                && timeline.getStatus() == Timeline.Status.RUNNING;
        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline(
                new KeyFrame(Duration.millis(millisPerTick), event -> tick())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);

        if (wasRunning) {
            timeline.play();
        }
    }

    public void handleInput(Direction direction) {
        if (state != GameState.RUNNING) {
            return;
        }
        playerSnake.setDirection(direction);
    }

    public GameState getState() {
        return state;
    }

    public GameField getField() {
        return field;
    }

    public void setView(GameView view) {
        this.view = view;
    }
}