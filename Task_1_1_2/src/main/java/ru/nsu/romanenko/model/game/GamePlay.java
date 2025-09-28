package ru.nsu.romanenko.model.game;

import java.util.Scanner;

import ru.nsu.romanenko.model.card.Card;
import ru.nsu.romanenko.model.card.Deck;
import ru.nsu.romanenko.view.Output;

/**
 * Class implementing game process.
 */
public class GamePlay {

    /**
     * Функция реализующая логику нового хода игрока.
     *
     * @param user счетчик очков пользователя
     * @param dealer счетчик очков дилера
     * @param deck текущая колода
     * @param isPlayingUser в данный момент играет пользователь или дилер
     */
    private static void pressOne(PointsCounter user, PointsCounter dealer,
                                 Deck deck, boolean isPlayingUser) {
        Card randCard = deck.getRandomCard();
        int point;
        if (isPlayingUser) {
            point = user.appendCard(randCard);
        } else {
            point = dealer.appendCard(randCard);
        }

        Output.printOpenCard(randCard, point, isPlayingUser);

        Output.printInfo(user.getInform(), dealer.getInform());
    }

    /**
     * Функция реализующая непосредственно процесс игры.
     *
     * @param user счетчик очков пользователя
     * @param dealer счетчик очков дилера
     * @param deck текущая колода
     * @param isPlayingUser в данный момент играет пользователь или дилер
     * @param scanner объект сканера для считывания ввода из консоли
     */
    public static void gaming(PointsCounter user, PointsCounter dealer,
                              Deck deck, boolean isPlayingUser, Scanner scanner) {
        if (isPlayingUser) {
            while (user.getPoints() < 21) {
                Output.printToGetCard();
                try {
                    String input = scanner.nextLine().trim();
                    if (input.equals("0")) {
                        break;
                    } else if (input.equals("1")) {
                        pressOne(user, dealer, deck, true);
                    } else {
                        Output.printToGetCardError();
                    }
                } catch (Exception e) {
                    Output.printScannerException();
                    scanner.nextLine();
                }
            }
        } else {
            while (dealer.getPoints() < 17) {
                pressOne(user, dealer, deck, false);
            }
        }
    }
}