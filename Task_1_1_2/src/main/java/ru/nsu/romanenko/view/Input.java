package ru.nsu.romanenko.view;

import java.util.Scanner;

/**
 * Class for handling user input.
 */
public class Input {
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Gets user choice for card drawing.
     *
     * @return true if user wants to take a card, false if user wants to stop
     */
    public static boolean getUserCardChoice() {
        while (true) {
            Output.printToGetCard();
            try {
                String input = SCANNER.nextLine().trim();
                if (input.equals("0")) {
                    return false;
                } else if (input.equals("1")) {
                    return true;
                } else {
                    Output.printToGetCardError();
                }
            } catch (Exception e) {
                Output.printScannerException();
            }
        }
    }

    /**
     * Closes the scanner.
     */
    public static void closeScanner() {
        SCANNER.close();
    }
}