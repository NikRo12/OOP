package ru.nsu.romanenko.model.game;

import java.util.Scanner;
import ru.nsu.romanenko.model.card.Card;
import ru.nsu.romanenko.model.card.Deck;
import ru.nsu.romanenko.view.Input;
import ru.nsu.romanenko.view.Output;

/**
 * Class implementing game process.
 */
public class GamePlay {

    public static void gaming(PointsCounter user, PointsCounter dealer,
                              Deck deck, boolean isPlayingUser) {
        if (isPlayingUser) {
            while (user.getPoints() < 21) {
                boolean wantsCard = Input.getUserCardChoice();
                if (!wantsCard) {
                    break;
                }
                pressOne(user, dealer, deck, true);
            }
        } else {
            while (dealer.getPoints() < 17) {
                pressOne(user, dealer, deck, false);
            }
        }
    }

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
}