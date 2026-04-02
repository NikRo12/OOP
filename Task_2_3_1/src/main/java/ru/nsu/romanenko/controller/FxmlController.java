package ru.nsu.romanenko.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import ru.nsu.romanenko.model.ConfigReader;
import ru.nsu.romanenko.model.GameConfig;
import ru.nsu.romanenko.view.GameView;

import java.net.URL;
import java.util.ResourceBundle;

public class FxmlController implements Initializable {

    @FXML
    private Canvas gameCanvas;

    private GameController gameController;
    private InputController inputController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameConfig config = ConfigReader.read();

        gameCanvas.setWidth(800);
        gameCanvas.setHeight(600);

        gameController = new GameController(config);

        GameView view = new GameView(gameCanvas, config.fieldSizeN(), config.fieldSizeM());
        inputController = new InputController(gameController);

        gameController.setView(view);
        gameController.start();
    }

    @FXML
    public void handleKeyPressed(KeyEvent event) {
        inputController.handle(event);
    }
}