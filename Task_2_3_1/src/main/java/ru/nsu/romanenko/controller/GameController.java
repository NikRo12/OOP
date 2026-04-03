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
    private int currentSpeed;
    private boolean isTicking = false;

    public GameController(GameConfig config) {
        this.config = config;
        this.state = GameState.WAITING;
        this.currentSpeed = config.startSpeed();
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
    }

    public void start() {
        field.init();
        updateTimeline();
        if (view != null) view.render(field, state);
    }

    private void updateTimeline() {
        if (timeline != null) timeline.stop();

        try {
            timeline = new Timeline(new KeyFrame(Duration.millis(currentSpeed), e -> tick()));
            timeline.setCycleCount(Timeline.INDEFINITE);
        } catch (Exception | Error e) {
            timeline = null;
        }
    }

    public void handleSpace() {
        if (state == GameState.WAITING) {
            state = GameState.RUNNING;
            if (timeline != null) timeline.play();
        } else if (state == GameState.LOST || state == GameState.WON) {
            if (state == GameState.WON) {
                currentSpeed = Math.max(40, (int) (currentSpeed * 0.8));
            } else {
                currentSpeed = config.startSpeed();
            }

            initSnakeAndField();
            start();

            state = GameState.RUNNING;
            if (timeline != null) timeline.play();
        }
    }

    void tick() {
        if (isTicking || state != GameState.RUNNING) return;
        isTicking = true;

        try {
            playerSnake.move();
            playerSnake.wrapHead(field.getHorizontalSize(), field.getVerticalSize());
            checkCollisions();

            if (view != null) {
                view.render(field, state);
            }
        } finally {
            isTicking = false;
        }
    }

    private void checkCollisions() {
        Position head = playerSnake.getHead();

        if (field.isObstacle(head)) {
            state = GameState.LOST;
            if (timeline != null) timeline.stop();
            return;
        }

        List<Position> body = playerSnake.getBody();
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                state = GameState.LOST;
                if (timeline != null) timeline.stop();
                return;
            }
        }

        if (field.isFood(head)) {
            Food food = field.getFoodAt(head);
            food.apply(playerSnake);
            field.replaceFood(head);
        }

        if (playerSnake.getSize() >= config.winCells()) {
            state = GameState.WON;
            if (timeline != null) timeline.stop();
        }
    }

    public void handleInput(Direction direction) {
        if (state == GameState.RUNNING) {
            playerSnake.setDirection(direction);
        }
    }

    public void setView(GameView view) {
        this.view = view;
    }

    public GameState getState() {
        return state;
    }

    public GameField getField() {
        return field;
    }
}