package ru.nsu.romanenko.model.game;

import ru.nsu.romanenko.model.card.Deck;
import ru.nsu.romanenko.view.Input;

import static ru.nsu.romanenko.model.game.Handler.pressOne;

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
}