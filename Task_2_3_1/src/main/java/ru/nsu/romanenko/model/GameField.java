package ru.nsu.romanenko.model;

import ru.nsu.romanenko.model.food.Food;
import ru.nsu.romanenko.model.obstacle.Obstacle;
import ru.nsu.romanenko.model.obstacle.SimpleObstacle;

import java.util.*;

public class GameField {
    private final Map<Position, Food> foods;
    private final Map<Position, Obstacle> obstacles;
    private final Snake snake;
    private final List<Food> foodTypes;

    private final int horizontalSize;
    private final int verticalSize;
    private final int foodCount;
    private final int obstacleCount;

    private final Random random;

    public GameField(GameConfig config, List<Food> foodTypes, Snake snake) {
        this.foods = new HashMap<>();
        this.obstacles = new HashMap<>();
        this.foodTypes = new ArrayList<>(foodTypes);
        this.snake = snake;
        this.horizontalSize = config.fieldSizeN();
        this.verticalSize = config.fieldSizeM();
        this.foodCount = config.foodCount();
        this.obstacleCount = (int) ((horizontalSize * verticalSize) * config.obstacleRatio());
        this.random = new Random();
    }

    public void init() {
        Set<Position> occupied = new HashSet<>(snake.getBody());
        Position head = snake.getHead();
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                if (Math.abs(dx) + Math.abs(dy) <= 2) {
                    occupied.add(new Position(head.horizontal() + dx,
                            head.vertical() + dy));
                }
            }
        }

        for (int i = 0; i < obstacleCount; i++) {
            Position pos = randomFreePosition(occupied);
            obstacles.put(pos, new SimpleObstacle());
            occupied.add(pos);
        }

        for (int i = 0; i < foodCount; i++) {
            Position pos = randomFreePosition(occupied);
            foods.put(pos, randomFoodType());
            occupied.add(pos);
        }
    }

    public void replaceFood(Position eaten) {
        foods.remove(eaten);
        Set<Position> occupied = getOccupied();
        Position pos = randomFreePosition(occupied);
        foods.put(pos, randomFoodType());
    }

    public Food getFoodAt(Position position) {
        return foods.get(position);
    }

    public boolean isFood(Position position) {
        return foods.containsKey(position);
    }

    public boolean isObstacle(Position position) {
        return obstacles.containsKey(position);
    }

    public Map<Position, Food> getFoods() {
        return Collections.unmodifiableMap(foods);
    }

    public Map<Position, Obstacle> getObstacles() {
        return Collections.unmodifiableMap(obstacles);
    }

    public Snake getSnake() {
        return snake;
    }

    public int getHorizontalSize() {
        return horizontalSize;
    }

    public int getVerticalSize() {
        return verticalSize;
    }

    private Set<Position> getOccupied() {
        Set<Position> occupied = new HashSet<>();
        occupied.addAll(foods.keySet());
        occupied.addAll(obstacles.keySet());
        occupied.addAll(snake.getBody());
        return occupied;
    }

    private Position randomFreePosition(Set<Position> occupied) {
        List<Position> free = new ArrayList<>();
        for (int x = 0; x < horizontalSize; x++) {
            for (int y = 0; y < verticalSize; y++) {
                Position p = new Position(x, y);
                if (!occupied.contains(p)) {
                    free.add(p);
                }
            }
        }
        if (free.isEmpty()) {
            throw new RuntimeException("No free position on field");
        }
        return free.get(random.nextInt(free.size()));
    }

    private Food randomFoodType() {
        return foodTypes.get(random.nextInt(foodTypes.size()));
    }
}