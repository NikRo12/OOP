package ru.nsu.romanenko.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import ru.nsu.romanenko.model.GameField;
import ru.nsu.romanenko.model.GameState;
import ru.nsu.romanenko.model.Position;
import ru.nsu.romanenko.model.food.Food;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GameView {
    private final Canvas canvas;
    private final double cellWidth;
    private final double cellHeight;
    private final int fieldWidth;
    private final int fieldHeight;

    private enum CellType {
        EMPTY, OBSTACLE, APPLE, GOLDEN_APPLE, SNAKE_HEAD, SNAKE_BODY
    }

    private final CellType[][] cellCache;
    private GameState lastState = null;
    private boolean forceFullRedraw = true;

    public GameView(Canvas canvas, int fieldWidth, int fieldHeight) {
        this.canvas = canvas;
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;

        this.cellWidth = canvas.getWidth() / fieldWidth;
        this.cellHeight = canvas.getHeight() / fieldHeight;

        this.cellCache = new CellType[fieldWidth][fieldHeight];
        for (int x = 0; x < fieldWidth; x++) {
            Arrays.fill(this.cellCache[x], CellType.EMPTY);
        }
    }

    public void render(GameField field, GameState state) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        if (state != lastState) {
            forceFullRedraw = true;
            lastState = state;
        }

        if (forceFullRedraw) {
            gc.setFill(Color.web("#0a0e1a"));
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            for (int x = 0; x < fieldWidth; x++) {
                Arrays.fill(cellCache[x], null);
            }
            forceFullRedraw = false;
        }

        CellType[][] currentGrid = new CellType[fieldWidth][fieldHeight];
        for (int x = 0; x < fieldWidth; x++) {
            Arrays.fill(currentGrid[x], CellType.EMPTY);
        }

        for (Position pos : field.getObstacles().keySet()) {
            if (isValid(pos)) currentGrid[
                    pos.horizontal()][pos.vertical()] = CellType.OBSTACLE;
        }

        for (Map.Entry<Position, Food> entry : field.getFoods().entrySet()) {
            Position pos = entry.getKey();
            if (isValid(pos)) {
                boolean isGolden = entry.getValue().getClass().getSimpleName().contains("Golden");
                currentGrid[pos.horizontal()][
                        pos.vertical()] = isGolden ? CellType.GOLDEN_APPLE : CellType.APPLE;
            }
        }

        List<Position> body = field.getSnake().getBody();
        for (int i = 0; i < body.size(); i++) {
            Position pos = body.get(i);
            if (isValid(pos)) {
                currentGrid[pos.horizontal()][
                        pos.vertical()] = (i == 0) ? CellType.SNAKE_HEAD : CellType.SNAKE_BODY;
            }
        }

        for (int x = 0; x < fieldWidth; x++) {
            for (int y = 0; y < fieldHeight; y++) {
                if (currentGrid[x][y] != cellCache[x][y]) {
                    drawCell(gc, x, y, currentGrid[x][y]);
                    cellCache[x][y] = currentGrid[x][y];
                }
            }
        }

        if (state == GameState.RUNNING) {
            drawScore(gc, field.getSnake().getSize());
        } else if (state == GameState.WAITING) {
            drawWaitingOverlay(gc);
        } else if (state == GameState.WON || state == GameState.LOST) {
            drawEndOverlay(gc, state);
        }
    }

    private void drawCell(GraphicsContext gc, int x, int y, CellType type) {
        double px = x * cellWidth;
        double py = y * cellHeight;

        gc.setFill(Color.web("#0a0e1a"));
        gc.fillRect(px, py, cellWidth, cellHeight);

        gc.setStroke(Color.web("#131929"));
        gc.setLineWidth(1);
        gc.strokeRect(px, py, cellWidth, cellHeight);

        if (type == CellType.OBSTACLE) {
            gc.setFill(Color.web("#334466"));
            gc.fillRoundRect(px + 1, py + 1, cellWidth - 2, cellHeight - 2, 5, 5);
        } else if (type == CellType.APPLE) {
            gc.setFill(Color.RED);
            gc.fillOval(px + 2, py + 2, cellWidth - 4, cellHeight - 4);
        } else if (type == CellType.GOLDEN_APPLE) {
            gc.setFill(Color.GOLD);
            gc.fillOval(px + 2, py + 2, cellWidth - 4, cellHeight - 4);
        } else if (type == CellType.SNAKE_HEAD) {
            gc.setFill(Color.web("#00ff88"));
            gc.fillRoundRect(px + 1, py + 1, cellWidth - 2, cellHeight - 2, 8, 8);
        } else if (type == CellType.SNAKE_BODY) {
            gc.setFill(Color.web("#009950"));
            gc.fillRoundRect(px + 1, py + 1, cellWidth - 2, cellHeight - 2, 8, 8);
        }
    }

    private boolean isValid(Position p) {
        return p.horizontal() >= 0 && p.horizontal() < fieldWidth &&
                p.vertical() >= 0 && p.vertical() < fieldHeight;
    }

    private void drawScore(GraphicsContext gc, int size) {
        gc.setFill(Color.web("#0a0e1a", 0.9));
        gc.fillRect(5, 5, 110, 25);

        gc.setStroke(Color.web("#131929"));
        gc.setLineWidth(1);
        gc.strokeRect(5, 5, 110, 25);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Monospaced", FontWeight.BOLD, 15));
        gc.fillText("SCORE: " + size, 10, 22);
    }

    private void drawWaitingOverlay(GraphicsContext gc) {
        drawMessage(gc, "SNAKE GAME", "Press SPACE to start");
    }

    private void drawEndOverlay(GraphicsContext gc, GameState state) {
        String title = (state == GameState.WON) ? "YOU WIN!" : "GAME OVER";
        drawMessage(gc, title, "Press SPACE to restart");
    }

    private void drawMessage(GraphicsContext gc, String title, String sub) {
        double cx = canvas.getWidth() / 2;
        double cy = canvas.getHeight() / 2;

        gc.setFill(Color.web("#0a0e1a", 0.9));
        gc.fillRect(cx - 150, cy - 50, 300, 100);

        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("Monospaced", FontWeight.BOLD, 25));
        gc.fillText(title, cx, cy - 10);

        gc.setFont(Font.font("Monospaced", 15));
        gc.fillText(sub, cx, cy + 20);
        gc.setTextAlign(TextAlignment.LEFT);
    }
}