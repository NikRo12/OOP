package ru.nsu.romanenko.view;

import ru.nsu.romanenko.model.card.Card;
import ru.nsu.romanenko.local.Localization;

/**
 * Output class for displaying game information.
 */
public class Output {

    public static void printHello() {
        System.out.println(Localization.get("welcome"));
    }

    public static void printRound(int round) {
        System.out.println(Localization.get("round") + " " + round);
    }

    public static void printInfo(String userInform, String dealerInform) {
        System.out.println("\t" + Localization.get("your_cards") + " " + userInform);
        System.out.println("\t" + Localization.get("dealer_cards") + " " + dealerInform);
    }

    public static void printStart(String userInform, String dealerInform) {
        System.out.println(Localization.get("dealer_puts"));
        printInfo(userInform, dealerInform);
        System.out.println(Localization.get("your_move"));
        System.out.println("-----------");
    }

    public static void printDealerMove(String userInform, String dealerInform) {
        System.out.println(Localization.get("dealer_move"));
        System.out.println("-----------");
        System.out.println(Localization.get("dealer_opened"));
        printInfo(userInform, dealerInform);
    }

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

    public static void printToGetCard() {
        System.out.println(Localization.get("choose_action"));
    }

    public static void printToGetCardError() {
        System.out.println(Localization.get("choose_action_error"));
    }

    public static void printScannerException() {
        System.out.println(Localization.get("scanner_error"));
    }

    public static void printWin() {
        System.out.println(Localization.get("win"));
    }

    public static void printLose() {
        System.out.println(Localization.get("lose"));
    }

    public static void printResult(int userScore, int dealerScore) {
        System.out.println(Localization.get("total_score"));
        System.out.println(Localization.get("your_score") + userScore);
        System.out.println(Localization.get("dealer_score") + dealerScore);
    }

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