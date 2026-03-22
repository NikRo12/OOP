package ru.nsu.romanenko.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import ru.nsu.romanenko.model.GameField;
import ru.nsu.romanenko.model.GameState;
import ru.nsu.romanenko.model.Position;
import ru.nsu.romanenko.model.food.Food;
import ru.nsu.romanenko.model.Snake;

import java.util.List;
import java.util.Map;

public class GameView {
    private final Canvas canvas;
    private final double cellSize;
    private final int fieldWidth;
    private final int fieldHeight;

    private static final Color BG_DARK      = Color.web("#0a0e1a");
    private static final Color GRID_LINE    = Color.web("#131929");

    private static final Color SNAKE_HEAD   = Color.web("#00ff88");
    private static final Color SNAKE_BODY1  = Color.web("#00cc6a");
    private static final Color SNAKE_BODY2  = Color.web("#009950");

    private static final Color APPLE_COLOR  = Color.web("#ff4455");
    private static final Color APPLE_SHINE  = Color.web("#ff8899");
    private static final Color GOLDEN_COLOR = Color.web("#ffcc00");
    private static final Color GOLDEN_SHINE = Color.web("#ffe566");

    private static final Color OBSTACLE     = Color.web("#334466");
    private static final Color OBSTACLE_EDGE= Color.web("#4466aa");

    private static final Color TEXT_MAIN    = Color.web("#e0eaff");
    private static final Color TEXT_ACCENT  = Color.web("#00ff88");

    public GameView(Canvas canvas, int fieldWidth, int fieldHeight) {
        this.canvas = canvas;
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.cellSize = Math.min(
                canvas.getWidth() / fieldWidth,
                canvas.getHeight() / fieldHeight
        );
    }

    public void render(GameField field, GameState state) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawBackground(gc);
        drawGrid(gc);
        drawObstacles(gc, field);
        drawFood(gc, field);
        drawSnake(gc, field);

        if (state == GameState.WAITING) {
            drawWaitingOverlay(gc);
        } else if (state == GameState.WON || state == GameState.LOST) {
            drawEndOverlay(gc, state);
        } else {
            drawScore(gc, field.getSnake().getSize());
        }
    }

    private void drawBackground(GraphicsContext gc) {
        gc.setFill(Color.web("#05080f"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(BG_DARK);
        gc.fillRect(0, 0, fieldWidth * cellSize, fieldHeight * cellSize);
    }

    private void drawGrid(GraphicsContext gc) {
        double fw = fieldWidth * cellSize;
        double fh = fieldHeight * cellSize;
        gc.setStroke(GRID_LINE);
        gc.setLineWidth(0.5);
        for (int x = 0; x <= fieldWidth; x++) {
            gc.strokeLine(x * cellSize, 0, x * cellSize, fh);
        }
        for (int y = 0; y <= fieldHeight; y++) {
            gc.strokeLine(0, y * cellSize, fw, y * cellSize);
        }
        for (int i = 0; i < 3; i++) {
            gc.setStroke(Color.web("#0a0e1a", 0.3 * (i + 1)));
            gc.setLineWidth(3 - i);
            gc.strokeRect(i, i, fw - i * 2, fh - i * 2);
        }
    }

    private void drawObstacles(GraphicsContext gc, GameField field) {
        for (Position pos : field.getObstacles().keySet()) {
            double x = pos.getHorizontal() * cellSize;
            double y = pos.getVertical() * cellSize;
            double s = cellSize - 1;

            gc.setFill(OBSTACLE);
            gc.fillRoundRect(x + 1, y + 1, s - 1, s - 1, 3, 3);

            gc.setStroke(OBSTACLE_EDGE);
            gc.setLineWidth(1);
            gc.strokeLine(x + 2, y + 2, x + s, y + 2);
            gc.strokeLine(x + 2, y + 2, x + 2, y + s);

            gc.setStroke(Color.web("#4466aa", 0.4));
            gc.setLineWidth(0.5);
            gc.strokeLine(x + 2, y + cellSize / 2, x + s, y + cellSize / 2);
            gc.strokeLine(x + cellSize / 2, y + 2, x + cellSize / 2, y + s);
        }
    }

    private void drawFood(GraphicsContext gc, GameField field) {
        for (Map.Entry<Position, Food> entry : field.getFoods().entrySet()) {
            Position pos = entry.getKey();
            boolean isGolden = entry.getValue().getClass().getSimpleName().equals("GoldenApple");
            double x = pos.getHorizontal() * cellSize;
            double y = pos.getVertical() * cellSize;
            double cx = x + cellSize / 2;
            double cy = y + cellSize / 2;
            double r = cellSize * 0.38;

            Color base  = isGolden ? GOLDEN_COLOR : APPLE_COLOR;
            Color shine = isGolden ? GOLDEN_SHINE : APPLE_SHINE;

            gc.setFill(Color.color(base.getRed(), base.getGreen(), base.getBlue(), 0.15));
            gc.fillOval(cx - r * 1.7, cy - r * 1.7, r * 3.4, r * 3.4);

            gc.setFill(base);
            gc.fillOval(cx - r, cy - r, r * 2, r * 2);

            gc.setFill(Color.color(shine.getRed(), shine.getGreen(), shine.getBlue(), 0.7));
            gc.fillOval(cx - r * 0.55, cy - r * 0.65, r * 0.6, r * 0.4);

            gc.setStroke(isGolden ? Color.web("#a07800") : Color.web("#882222"));
            gc.setLineWidth(1.2);
            gc.strokeLine(cx + 1, cy - r, cx + 3, cy - r - 3);
        }
    }

    private void drawSnake(GraphicsContext gc, GameField field) {
        List<Position> body = field.getSnake().getBody();
        if (body.isEmpty()) return;

        int size = body.size();
        for (int i = size - 1; i >= 0; i--) {
            Position pos = body.get(i);
            double x = pos.getHorizontal() * cellSize;
            double y = pos.getVertical() * cellSize;
            double s = cellSize - 2;

            if (i == 0) {
                gc.setFill(Color.color(0, 1, 0.53, 0.18));
                gc.fillRoundRect(x - 2, y - 2, s + 6, s + 6, 6, 6);

                gc.setFill(SNAKE_HEAD);
                gc.fillRoundRect(x + 1, y + 1, s, s, 4, 4);

                double ex = (pos.getHorizontal() * cellSize) + cellSize * 0.3;
                double ey = (pos.getVertical() * cellSize) + cellSize * 0.3;
                gc.setFill(Color.web("#0a0e1a"));
                gc.fillOval(ex - 1, ey - 1, cellSize * 0.2, cellSize * 0.2);
                gc.fillOval(ex + cellSize * 0.25, ey - 1, cellSize * 0.2, cellSize * 0.2);
                gc.setFill(Color.WHITE);
                gc.fillOval(ex - 0.5, ey - 0.5, cellSize * 0.1, cellSize * 0.1);
                gc.fillOval(ex + cellSize * 0.27, ey - 0.5, cellSize * 0.1, cellSize * 0.1);
            } else {
                double t = (double) i / size;
                Color bodyColor = i % 2 == 0 ? SNAKE_BODY1 : SNAKE_BODY2;
                double alpha = Math.max(0.4, 1.0 - t * 0.5);
                gc.setFill(Color.color(bodyColor.getRed(), bodyColor.getGreen(), bodyColor.getBlue(), alpha));
                gc.fillRoundRect(x + 1, y + 1, s, s, 4, 4);
            }
        }
    }

    private void drawScore(GraphicsContext gc, int size) {
        gc.setFont(Font.font("Monospaced", FontWeight.BOLD, 13));
        String score = "SIZE: " + size;
        gc.setFill(Color.web("#00ff88", 0.7));
        gc.fillText(score, 8, 18);
    }

    private void drawWaitingOverlay(GraphicsContext gc) {
        double cx = fieldWidth * cellSize / 2;
        double cy = fieldHeight * cellSize / 2;

        gc.setFill(Color.web("#0a0e1a", 0.82));
        gc.fillRoundRect(cx - 170, cy - 90, 340, 180, 16, 16);

        gc.setStroke(Color.web("#00ff88", 0.5));
        gc.setLineWidth(1.5);
        gc.strokeRoundRect(cx - 170, cy - 90, 340, 180, 16, 16);

        gc.setFill(TEXT_ACCENT);
        gc.setFont(Font.font("Monospaced", FontWeight.BOLD, 32));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("SNAKE", cx, cy - 30);

        gc.setFill(Color.web("#e0eaff", 0.6));
        gc.setFont(Font.font("Monospaced", FontWeight.NORMAL, 13));
        gc.fillText("Use arrow keys to move", cx, cy + 5);

        gc.setFill(TEXT_MAIN);
        gc.setFont(Font.font("Monospaced", FontWeight.BOLD, 15));
        gc.fillText("[ SPACE ] — Start Game", cx, cy + 40);

        gc.setTextAlign(TextAlignment.LEFT);
    }

    private void drawEndOverlay(GraphicsContext gc, GameState state) {
        double cx = fieldWidth * cellSize / 2;
        double cy = fieldHeight * cellSize / 2;

        gc.setFill(Color.web("#0a0e1a", 0.85));
        gc.fillRoundRect(cx - 160, cy - 80, 320, 160, 16, 16);

        boolean won = state == GameState.WON;
        Color accent = won ? Color.web("#00ff88") : Color.web("#ff4455");

        gc.setStroke(Color.color(accent.getRed(), accent.getGreen(), accent.getBlue(), 0.6));
        gc.setLineWidth(1.5);
        gc.strokeRoundRect(cx - 160, cy - 80, 320, 160, 16, 16);

        gc.setFill(accent);
        gc.setFont(Font.font("Monospaced", FontWeight.BOLD, 30));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(won ? "YOU WON!" : "GAME OVER", cx, cy - 15);

        gc.setFill(Color.web("#e0eaff", 0.55));
        gc.setFont(Font.font("Monospaced", FontWeight.NORMAL, 13));
        gc.fillText(won ? "Congratulations!" : "Better luck next time", cx, cy + 20);

        gc.setFill(TEXT_MAIN);
        gc.setFont(Font.font("Monospaced", FontWeight.BOLD, 14));
        gc.fillText("[ SPACE ] — Play Again", cx, cy + 55);

        gc.setTextAlign(TextAlignment.LEFT);
    }
}