package ru.nsu.romanenko.controller;

import ru.nsu.romanenko.view.Output;
import ru.nsu.romanenko.model.card.Card;
import ru.nsu.romanenko.model.card.Deck;
import ru.nsu.romanenko.model.game.GamePlay;
import ru.nsu.romanenko.model.game.PointsCounter;

import java.util.Scanner;

public class Controller {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Deck deck = new Deck();

    /**
     * Основной метод игры в BlackJack.
     *
     * @param totalRounds количество раундов для игры
     */
    public static void BlackJack(int totalRounds) {
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

            GamePlay.gaming(user, dealer, deck, true, scanner);

            if (user.getPoints() == 21) {
                Output.printWin();
                userScore++;
            } else if (user.getPoints() > 21) {
                Output.printLose();
                dealerScore++;
            } else {
                dealer.appendCard(hiddenCard);
                Output.printDealerMove(user.getInform(), dealer.getInform());

                GamePlay.gaming(user, dealer, deck, false, scanner);

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
        scanner.close();
    }

    /**
     * Инициализация раунда - раздача начальных карт.
     *
     * @param user счетчик очков пользователя
     * @param dealer счетчик очков дилера
     * @return скрытая карта дилера
     */
    private static Card initRound(PointsCounter user, PointsCounter dealer) {
        Card userCard1 = deck.getRandomCard();
        Card userCard2 = deck.getRandomCard();
        user.appendCard(userCard1);
        user.appendCard(userCard2);

        Card dealerCard1 = deck.getRandomCard();
        Card dealerCard2 = deck.getRandomCard();
        dealer.appendCard(dealerCard1);

        return dealerCard2;
    }
}