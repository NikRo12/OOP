package ru.nsu.romanenko;

import java.util.Scanner;

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
        Scanner scanner = new Scanner(System.in);
        Deck deck = new Deck();

        System.out.println("Welcome to BlackJack");
        int userScore = 0;
        int dealerScore = 0;

        for (int round = 1; round < 5; round++) {
            PointsCounter user = new PointsCounter(false);
            final PointsCounter dealer = new PointsCounter(true);

            System.out.println("Round " + round);
            System.out.println("Dealer puts cards");

            Card userCard1 = deck.getRandomCard();
            Card userCard2 = deck.getRandomCard();

            user.appendCard(userCard1);
            user.appendCard(userCard2);

            Card dealerCard1 = deck.getRandomCard();
            Card dealerCard2 = deck.getRandomCard();

            dealer.appendCard(dealerCard1);

            System.out.println("\tYour cards " + user.getInform());
            System.out.println("\tDealer's cards " + dealer.getInform());
            System.out.println("Your move");
            System.out.println("-----------");

            GamePlay.gaming(user, dealer, deck, true, scanner);
            if (user.getPoints() == 21) {
                System.out.println("You win");
                userScore++;
            } else if (user.getPoints() > 21) {
                System.out.println("More than 21, you lost");
                dealerScore++;
            } else {
                System.out.println("Dealer's move");
                System.out.println("-----------");

                dealer.appendCard(dealerCard2);
                System.out.println("Dealer opened closed card");
                System.out.println("\tYour cards " + user.getInform());
                System.out.println("\tDealer's cards " + dealer.getInform());
                GamePlay.gaming(user, dealer, deck, false, scanner);

                if (dealer.getPoints() > 21) {
                    System.out.println("Dealer's card out of 21, you win");
                    userScore++;
                } else if (user.getPoints() > dealer.getPoints()) {
                    System.out.println("You win");
                    userScore++;
                } else {
                    System.out.println("You lost");
                    dealerScore++;
                }
            }

            System.out.println("Total score:");
            System.out.println("Yours score is --- " + userScore);
            System.out.println("Dealer's score is --- " + dealerScore);
        }
    }
}