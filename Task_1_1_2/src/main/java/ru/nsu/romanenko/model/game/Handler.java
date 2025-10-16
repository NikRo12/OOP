package ru.nsu.romanenko.model.game;

import ru.nsu.romanenko.model.card.Card;
import ru.nsu.romanenko.model.card.Deck;
import ru.nsu.romanenko.view.Output;

public class Handler {
    static void pressOne(PointsCounter user, PointsCounter dealer,
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
