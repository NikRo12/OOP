package ru.nsu.romanenko;

import ru.nsu.romanenko.controller.Controller;
import ru.nsu.romanenko.view.Localization;

import java.util.Locale;

/**
 * Основной класс реализующий процесс игры BlackJack.
 */
public class Main {

    /**
     * Основной метод программы.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        Controller.BlackJack(2);
    }
}