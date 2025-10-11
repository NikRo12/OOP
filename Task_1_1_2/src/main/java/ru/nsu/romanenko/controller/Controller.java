package ru.nsu.romanenko.controller;

import ru.nsu.romanenko.model.card.Card;
import ru.nsu.romanenko.model.card.Deck;
import ru.nsu.romanenko.model.game.GamePlay;
import ru.nsu.romanenko.model.game.PointsCounter;
import ru.nsu.romanenko.view.Input;
import ru.nsu.romanenko.view.Output;

public class Controller {
    private static final Deck DECK = new Deck();

    private static class ScoreHolder {
        int userScore;
        int dealerScore;

        ScoreHolder(int userScore, int dealerScore) {
            this.userScore = userScore;
            this.dealerScore = dealerScore;
        }
    }

    public static void blackJack(int totalRounds) {
        Output.printHello();
        ScoreHolder scores = new ScoreHolder(0, 0);

        for (int round = 1; round <= totalRounds; round++) {
            roundPlay(round, scores);

            if (round < totalRounds) {
                Output.printResult(scores.userScore, scores.dealerScore);
            }
        }

        Output.printFinalResult(scores.userScore, scores.dealerScore);
        Input.closeScanner();
    }

    private static void roundPlay(int round, ScoreHolder scores) {
        PointsCounter user = new PointsCounter(false);
        PointsCounter dealer = new PointsCounter(true);
        Card hiddenCard;

        hiddenCard = initRound(user, dealer);

        Output.printRound(round);
        Output.printStart(user.getInform(), dealer.getInform());

        GamePlay.gaming(user, dealer, DECK, true);

        if (user.getPoints() == 21) {
            Output.printWin();
            scores.userScore++;
        } else if (user.getPoints() > 21) {
            Output.printLose();
            scores.dealerScore++;
        } else {
            dealer.appendCard(hiddenCard);
            Output.printDealerMove(user.getInform(), dealer.getInform());

            GamePlay.gaming(user, dealer, DECK, false);

            if (dealer.getPoints() > 21) {
                Output.printWin();
                scores.userScore++;
            } else if (user.getPoints() > dealer.getPoints()) {
                Output.printWin();
                scores.userScore++;
            } else {
                Output.printLose();
                scores.dealerScore++;
            }
        }
    }

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