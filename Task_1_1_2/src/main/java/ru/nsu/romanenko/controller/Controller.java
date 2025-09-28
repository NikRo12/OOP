package ru.nsu.romanenko.controller;

import java.util.Scanner;

import ru.nsu.romanenko.model.card.Card;
import ru.nsu.romanenko.model.card.Deck;
import ru.nsu.romanenko.model.game.GamePlay;
import ru.nsu.romanenko.model.game.PointsCounter;
import ru.nsu.romanenko.view.Output;

/**
 * Controller class for BlackJack game.
 */
public class Controller {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Deck DECK = new Deck();

    /**
     * Main method for BlackJack game.
     *
     * @param totalRounds number of rounds to play
     */
    public static void blackJack(int totalRounds) {
        Output.printHello();
        int userScore = 0;
        int dealerScore = 0;

        for (int round = 1; round <= totalRounds; round++) {
            PointsCounter user = new PointsCounter(false);
            PointsCounter dealer = new PointsCounter(true);
            Card hiddenCard;

            hiddenCard = initRound(user, dealer);

            Output.printRound(round);
            Output.printStart(user.getInform(), dealer.getInform());

            GamePlay.gaming(user, dealer, DECK, true, SCANNER);

            if (user.getPoints() == 21) {
                Output.printWin();
                userScore++;
            } else if (user.getPoints() > 21) {
                Output.printLose();
                dealerScore++;
            } else {
                dealer.appendCard(hiddenCard);
                Output.printDealerMove(user.getInform(), dealer.getInform());

                GamePlay.gaming(user, dealer, DECK, false, SCANNER);

                if (dealer.getPoints() > 21) {
                    Output.printWin();
                    userScore++;
                } else if (user.getPoints() > dealer.getPoints()) {
                    Output.printWin();
                    userScore++;
                } else {
                    Output.printLose();
                    dealerScore++;
                }
            }

            if (round < totalRounds) {
                Output.printResult(userScore, dealerScore);
            }
        }

        Output.printFinalResult(userScore, dealerScore);
        SCANNER.close();
    }

    /**
     * Initialize round - deal initial cards.
     *
     * @param user points counter for user
     * @param dealer points counter for dealer
     * @return hidden dealer card
     */
    private static Card initRound(PointsCounter user, PointsCounter dealer) {
        Card userCard1 = DECK.getRandomCard();
        Card userCard2 = DECK.getRandomCard();
        user.appendCard(userCard1);
        user.appendCard(userCard2);

        Card dealerCard1 = DECK.getRandomCard();
        Card dealerCard2 = DECK.getRandomCard();
        dealer.appendCard(dealerCard1);

        return dealerCard2;
    }
}