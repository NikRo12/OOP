package ru.nsu.romanenko;

import java.util.Scanner;

/**
 * Класс реализующий игровой процесс.
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
        if (isPlayingUser) {
            System.out.println("You've opened card: " + randCard.getValue()
                    + " " + randCard.getSuit() + " (" + user.appendCard(randCard) + ")");
        } else {
            System.out.println("Dealer opened card: " + randCard.getValue()
                    + " " + randCard.getSuit() + " (" + dealer.appendCard(randCard) + ")");
        }

        System.out.println("\tYour cards " + user.getInform());
        System.out.println("\tDealer's cards " + dealer.getInform());
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
                System.out.println("Press \"1\" to get card or \"0\" to pass...");
                try {
                    String input = scanner.nextLine().trim();
                    if (input.equals("0")) {
                        break;
                    } else if (input.equals("1")) {
                        pressOne(user, dealer, deck, isPlayingUser);
                    } else {
                        System.out.println("Please enter only 1 or 0");
                    }
                } catch (Exception e) {
                    System.out.println("Input error, please try again");
                    scanner.nextLine();
                }
            }
        } else {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            while (dealer.getPoints() < 17) {
                pressOne(user, dealer, deck, isPlayingUser);
            }
        }
    }
}