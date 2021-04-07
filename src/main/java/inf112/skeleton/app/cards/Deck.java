package inf112.skeleton.app.cards;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
	private ArrayList<Card> currentDeck;

	/**
	 * Initialize a new deck
	 */
	public Deck() {
		currentDeck = new ArrayList<>();
		createDeck();
	}

	/**
	 * Creates a new deck with all possible cards.
	 *
	 * Thanks to user @hskrfn822 on https://boardgamegeek.com/thread/645061/need-specific-list-cards for providing the
	 * necessary data in regards to priority for this.
	 */
	public void createDeck () {
		for (int i = 0; i<=34; i++) {
			if(i<=17) {
				Card card = new Card();
				card.create("move1", 490 + (i*10));
				currentDeck.add(card);
			}
			if(i<=11){
				Card card = new Card();
				card.create("move2",670+(i*10));
				currentDeck.add(card);
			}
			if(i<=5){
				Card card = new Card();
				card.create("move3",790+(i*10));
				currentDeck.add(card);
			}
			if(i<=5){
				Card card = new Card();
				card.create("backUp",430+(i*10));
				currentDeck.add(card);
			}
			if(i<=5){
				Card card = new Card();
				card.create("uTurn",10+(i*10));
				currentDeck.add(card);
			}
			if(i%2==0){
				Card card = new Card();
				card.create("turnRight",80+(i*10));
				currentDeck.add(card);

				Card card2 = new Card();
				card2.create("turnLeft",70+(i*10));
				currentDeck.add(card2);
			}

		}
		Collections.shuffle(currentDeck);
	}

	public ArrayList<Card> deal(int cardsToDeal) {
		ArrayList<Card> hand = new ArrayList<>();
		Random random = new Random();
		for(int i=0; i<cardsToDeal; i++){
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
	public ArrayList<Card> getCurrentDeck() {
		return currentDeck;
	}

	/**
	 * @param card card that you want removed
	 * Method is used when you need to remove a card from the deck due to it being locked to a player
	 */
	public void removeCardFromDeck(Card cardToRemove) {
		currentDeck.removeIf(card -> card.equals(cardToRemove));

	}
}
