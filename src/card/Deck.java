package card;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import engine.GameManager;
import engine.board.BoardManager;
import card.standard.Ace;
import card.standard.Five;
import card.standard.Four;
import card.standard.Jack;
import card.standard.King;
import card.standard.Queen;
import card.standard.Seven;
import card.standard.Standard;
import card.standard.Suit;
import card.standard.Ten;
import card.wild.Burner;
import card.wild.Saver;

public class Deck {
    private static final String CARDS_FILE = "Cards.csv";
    static private ArrayList<Card> cardsPool;

    @SuppressWarnings("resource")
	public static void loadCardPool(BoardManager boardManager, GameManager gameManager) throws IOException {
        cardsPool = new ArrayList<>();

		BufferedReader br = new BufferedReader(new FileReader(CARDS_FILE));

		while (br.ready()) {
			String nextLine = br.readLine();
			String[] data = nextLine.split(",");
			
			if (data.length == 0) 
				throw new IOException(nextLine);

            String name = data[2];
            String description = data[3];
			
			int code = Integer.parseInt(data[0]);
			int frequency = Integer.parseInt(data[1]);
			
			for (int i = 0; i < frequency; i++) {
				Card card;
				
				if(code > 13) 
					switch(code) {
						case 14: card = new Burner(name, description, boardManager, gameManager); break;
						case 15: card = new Saver(name, description, boardManager, gameManager); break;
						default: throw new IOException(nextLine);
					}
			
				else {
	                int rank = Integer.parseInt(data[4]);
	                Suit cardSuit = Suit.valueOf(data[5]);
					switch(code) {
						case 0: card = new Standard(name, description, rank, cardSuit, boardManager, gameManager); break;
						case 1: card = new Ace(name, description, cardSuit, boardManager, gameManager); break;
						case 4: card = new Four(name, description, cardSuit, boardManager, gameManager); break;
						case 5: card = new Five(name, description, cardSuit, boardManager, gameManager); break;
						case 7: card = new Seven(name, description, cardSuit, boardManager, gameManager); break;
						case 10: card = new Ten(name, description, cardSuit, boardManager, gameManager); break;
						case 11: card = new Jack(name, description, cardSuit, boardManager, gameManager); break;
						case 12: card = new Queen(name, description, cardSuit, boardManager, gameManager); break;
						case 13: card = new King(name, description, cardSuit, boardManager, gameManager); break;
						default: throw new IOException(nextLine);
					}
				}
				
				cardsPool.add(card);
			}	
        }
    }

    public static ArrayList<Card> drawCards() {
        Collections.shuffle(cardsPool);
        ArrayList<Card> cards = new ArrayList<>(cardsPool.subList(0, 4));
        cardsPool.subList(0, 4).clear();
        return cards;
    }
    
    public static int getPoolSize() {
		return cardsPool.size();
	}

    public static void refillPool(ArrayList<Card> cards) {
        cardsPool.addAll(cards);
    }

}

