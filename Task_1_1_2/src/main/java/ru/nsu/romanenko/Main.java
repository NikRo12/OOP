package ru.nsu.romanenko;

import java.util.Scanner;

/**
 * Основной класс реализующий процесс игры BlackJack
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Deck deck = new Deck();

        System.out.println("Welcome to BlackJack");
        int user_score = 0;
        int diller_score = 0;

        for(int round = 1; round < 5; round++)
        {
            PointsCounter user = new PointsCounter(0);
            PointsCounter diller = new PointsCounter(1);

            System.out.println("Round " + round);
            System.out.println("Diller puts cards");

            Card user_card1 = deck.getRandomCard();
            Card user_card2 = deck.getRandomCard();

            user.appendCard(user_card1);
            user.appendCard(user_card2);

            Card diller_card1 = deck.getRandomCard();
            Card diller_card2 = deck.getRandomCard();

            diller.appendCard(diller_card1);

            System.out.println("\tYour cards " + user.getInform());
            System.out.println("\tDiller's cards " + diller.getInform());
            System.out.println("Your move");
            System.out.println("-----------");

            GamePlay.Gaming(user, diller, deck, 1, scanner);
            if(user.getPoints() == 21)
            {
                System.out.println("You win");
                user_score++;
            }

            else if(user.getPoints() > 21)
            {
                System.out.println("More than 21, you lost");
                diller_score++;
            }

            else
            {
                System.out.println("Diller's move");
                System.out.println("-----------");

                diller.appendCard(diller_card2);
                System.out.println("Diller opened closed card");
                System.out.println("\tYour cards " + user.getInform());
                System.out.println("\tDiller's cards " + diller.getInform());
                GamePlay.Gaming(user, diller, deck, 0, scanner);

                if(diller.getPoints() > 21)
                {
                    System.out.println("Diller's card out of 21, you win");
                    user_score++;
                }

                else if(user.getPoints() > diller.getPoints())
                {
                    System.out.println("You win");
                    user_score++;
                }

                else
                {
                    System.out.println("You lost");
                    diller_score++;
                }
            }

            System.out.println("Total score:");
            System.out.println("Yours score is --- " + user_score);
            System.out.println("Diller's score is --- " + diller_score);
        }
    }
}