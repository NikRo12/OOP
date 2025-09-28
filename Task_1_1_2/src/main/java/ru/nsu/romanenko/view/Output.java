package ru.nsu.romanenko.view;

import ru.nsu.romanenko.model.card.Card;

/**
 * Output class for displaying game information.
 */
public class Output {

    /**
     * Print welcome message.
     */
    public static void printHello() {
        System.out.println(Localization.get("welcome"));
    }

    /**
     * Print round number.
     *
     * @param round round number
     */
    public static void printRound(int round) {
        System.out.println(Localization.get("round") + " " + round);
    }

    /**
     * Print game information.
     *
     * @param userInform user information
     * @param dealerInform dealer information
     */
    public static void printInfo(String userInform, String dealerInform) {
        System.out.println("\t" + Localization.get("your_cards") + " " + userInform);
        System.out.println("\t" + Localization.get("dealer_cards") + " " + dealerInform);
    }

    /**
     * Print start of round information.
     *
     * @param userInform user information
     * @param dealerInform dealer information
     */
    public static void printStart(String userInform, String dealerInform) {
        System.out.println(Localization.get("dealer_puts"));
        printInfo(userInform, dealerInform);
        System.out.println(Localization.get("your_move"));
        System.out.println("-----------");
    }

    /**
     * Print dealer move information.
     *
     * @param userInform user information
     * @param dealerInform dealer information
     */
    public static void printDealerMove(String userInform, String dealerInform) {
        System.out.println(Localization.get("dealer_move"));
        System.out.println("-----------");
        System.out.println(Localization.get("dealer_opened"));
        printInfo(userInform, dealerInform);
    }

    /**
     * Print opened card information.
     *
     * @param randCard random card
     * @param point card points
     * @param user true if user card, false if dealer card
     */
    public static void printOpenCard(Card randCard, int point, boolean user) {
        if (user) {
            System.out.println(Localization.get("open_card_user")
                    + randCard.getValue() + " " + randCard.getSuit()
                    + " (" + point + ")");
        } else {
            System.out.println(Localization.get("open_card_dealer")
                    + randCard.getValue() + " " + randCard.getSuit()
                    + " (" + point + ")");
        }
    }

    /**
     * Print card choice prompt.
     */
    public static void printToGetCard() {
        System.out.println(Localization.get("choose_action"));
    }

    /**
     * Print card choice error.
     */
    public static void printToGetCardError() {
        System.out.println(Localization.get("choose_action_error"));
    }

    /**
     * Print scanner exception.
     */
    public static void printScannerException() {
        System.out.println(Localization.get("scanner_error"));
    }

    /**
     * Print win message.
     */
    public static void printWin() {
        System.out.println(Localization.get("win"));
    }

    /**
     * Print lose message.
     */
    public static void printLose() {
        System.out.println(Localization.get("lose"));
    }

    /**
     * Print result.
     *
     * @param userScore user score
     * @param dealerScore dealer score
     */
    public static void printResult(int userScore, int dealerScore) {
        System.out.println(Localization.get("total_score"));
        System.out.println(Localization.get("your_score") + userScore);
        System.out.println(Localization.get("dealer_score") + dealerScore);
    }

    /**
     * Print final result.
     *
     * @param userScore user score
     * @param dealerScore dealer score
     */
    public static void printFinalResult(int userScore, int dealerScore) {
        System.out.println("\n" + Localization.get("game_over"));
        System.out.println(Localization.get("final_score"));
        System.out.println(Localization.get("your_score") + userScore);
        System.out.println(Localization.get("dealer_score") + dealerScore);

        if (userScore > dealerScore) {
            System.out.println(Localization.get("congratulations"));
        } else if (userScore < dealerScore) {
            System.out.println(Localization.get("dealer_wins"));
        } else {
            System.out.println(Localization.get("tie"));
        }
    }
}