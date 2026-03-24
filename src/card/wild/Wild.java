package card.wild;

import engine.GameManager;
import engine.board.BoardManager;
import card.Card;

public abstract class Wild extends Card {

    public Wild(String name, String description, BoardManager boardManager, GameManager gameManager) {
        super(name, description, boardManager, gameManager);
    }
    
}
