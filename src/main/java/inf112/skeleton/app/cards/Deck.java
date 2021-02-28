package inf112.skeleton.app.cards;

import java.util.*;

public class Deck {
	private ArrayList<Card> currentDeck;

	/**
	 * Initialize a new deck
	 */
	public Deck() {
		createDeck();
		currentDeck = new ArrayList<>();
	}

	/**
	 * Creates a new deck with all possible cards.
	 *
	 * Thanks to user @hskrfn822 on https://boardgamegeek.com/thread/645061/need-specific-list-cards for providing the
	 * necessary data in regards to priority for this.
	 */
	public void createDeck () {
		for (int i = 0; i<=340; i=i+10) {
			if(i<=170) {
				currentDeck.add(new Card("move1", 490 + (i)));
			}
			if(i<=110){
				currentDeck.add(new Card("move2",670+(i)));
			}
			if(i<=40){
				currentDeck.add(new Card("move3",790+(i)));
			}
			if(i<=40){
				currentDeck.add(new Card("backUp",430+(i)));
			}
			if(i<=50){
				currentDeck.add(new Card("uTurn",10+(i)));
			}
			currentDeck.add(new Card("turnRight",80+(i)));
			currentDeck.add(new Card("turnLeft",70+(i)));
		}
		Collections.shuffle(currentDeck);
	}

	public ArrayList<Card> deal() {
		ArrayList<Card> hand = new ArrayList<>();
		Random random = new Random();
		for(int i=0; i<9; i++){
			hand.add(currentDeck.remove(random.nextInt(currentDeck.size())));
		}
		return hand;
	}

	/**
	 * Resets the deck, adding all cards back in it.
	 */
	public void resetDeck(){
		currentDeck = new ArrayList<>();
		createDeck();
	}
}
