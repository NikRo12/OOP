package ru.nsu.romanenko;

import java.util.ArrayList;
import java.util.Scanner;


/**
 * Класс реализующий счетчик очков для пользователя и диллера
 */
class PointsCounter
{
    private int points;
    private record StringCard(String value, String suit, int card_point) {};
    private final ArrayList<StringCard> cardPoints = new ArrayList<>();
    private final int is_diller;

    PointsCounter(int is_diller)
    {
        points = 0;
        this.is_diller = is_diller;
    }

    /**
     * Добавление карты в руки игрока (пользователь или диллер)
     * @param card любая карта класса Card
     * @return количество очков, которая дает эта карта
     */

    public int appendCard(Card card)
    {
        String val = card.getValue();
        String suit = card.getSuit();

        int card_point = switch (val) {
            case "Two" -> 2;
            case "Three" -> 3;
            case "Four" -> 4;
            case "Five" -> 5;
            case "Six" -> 6;
            case "Seven" -> 7;
            case "Eight" -> 8;
            case "Nine" -> 9;
            case "Ten", "Jack", "Queen", "King" -> 10;
            case "Ace" -> {
                if(points + 11 <= 21) {
                    yield 11;
                }

                else {
                    yield 1;
                }
            }

            default -> throw new IllegalArgumentException("Unknown card: " + val);
        };

        cardPoints.add(new StringCard(val, suit, card_point));
        points += card_point;
        return card_point;
    }

    /**
     * Информация по игроку (карты, очки, общие очки)
     * @return соответствующую строку информации
     */
    public String getInform() {
        StringBuilder result = new StringBuilder("[");
        int count = 0;
        int total = cardPoints.size();

        for (StringCard entry : cardPoints) {
            String value = entry.value;
            String suit = entry.suit;
            Integer card_point = entry.card_point;

            result.append(value).append(" ").append(suit)
                    .append(" (").append(card_point).append(")");

            if (++count < total) {
                result.append(", ");
            }
        }

        if(is_diller == 1 && cardPoints.size() == 1)
        {
            result.append(", <closed card>");
        }

        result.append("] => ").append(this.points);
        return result.toString();
    }

    public int getPoints()
    {
        return points;
    }
}

/**
 * Класс реализующий игровой процесс
 */
class GamePlay
{
    /**
     * Функция реализующая логику нового хода игрока
     * @param user Счетчик очков пользователя
     * @param diller Счетчик очков диллера
     * @param deck Текущая колода
     * @param is_playing_user в данный момент играет пользователь или диллер (1 - пользователь, 0 - диллер)
     */
    private static void pressOne(PointsCounter user, PointsCounter diller, Deck deck, int is_playing_user)
    {
        Card rand_card = deck.getRandomCard();
        if(is_playing_user == 1)
        {
            System.out.println("You've opened card: " + rand_card.getValue() + " " + rand_card.getSuit() + " (" + user.appendCard(rand_card) + ")");
        }

        else
        {
            System.out.println("Diller opened card: " + rand_card.getValue() + " " + rand_card.getSuit() + " (" + diller.appendCard(rand_card) + ")");

        }

        System.out.println("\tYour cards " + user.getInform());
        System.out.println("\tDiller's cards " + diller.getInform());
    }


    /**
     * Функция реализующая непосредственно процесс игры
     * @param user Счетчик очков пользователя
     * @param diller Счетчик очков диллера
     * @param deck Текущая колода
     * @param is_playing_user В данный момент играет пользователь или диллер (1 - пользователь, 0 - диллер)
     * @param scanner Объект сканера для считывания ввода из консоли
     */
    static void Gaming(PointsCounter user, PointsCounter diller, Deck deck, int is_playing_user, Scanner scanner)
    {
        if(is_playing_user == 1)
        {
            while (user.getPoints() < 21)
            {
                System.out.println("Press \"1\" to get card or \"0\" to pass...");
                try
                {
                    String input = scanner.nextLine().trim();
                    if (input.equals("0"))
                    {
                        break;
                    }

                    else if (input.equals("1"))
                    {
                        pressOne(user, diller, deck, is_playing_user);
                    }

                    else
                    {
                        System.out.println("Please enter only 1 or 0");
                    }
                }

                catch (Exception e) {
                    System.out.println("Input error, please try again");
                    scanner.nextLine();
                }
            }
        }

        else
        {
            try
            {
                Thread.sleep(2500);
            }

            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }

            while(diller.getPoints() < 17)
            {
                pressOne(user, diller, deck, is_playing_user);
            }
        }
    }
}

