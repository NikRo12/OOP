package ru.nsu.romanenko.model;

public record GameConfig(
        int fieldSizeN,
        int fieldSizeM,
        int foodCount,
        double obstacleRatio,
        int winCells,
        int startSpeed,
        int cellSize
) {}
