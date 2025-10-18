package ru.nsu.romanenko;

import ru.nsu.romanenko.controller.Controller;
import ru.nsu.romanenko.local.Localization;
import ru.nsu.romanenko.view.Input;

import java.util.Locale;

/**
 * Main class for BlackJack game.
 */
public class Main {

    /**
     * Main method of the program.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Localization.setLocale(Locale.ENGLISH);
        Controller.blackJack(2);
        Controller.blackJack(2);
        Input.closeScanner();
    }
}