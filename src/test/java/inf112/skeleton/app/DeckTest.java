package inf112.skeleton.app;

import static org.junit.jupiter.api.Assertions.*;

import inf112.skeleton.app.sprites.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import inf112.skeleton.app.cards.Card;
import inf112.skeleton.app.cards.Deck;
import java.util.ArrayList;

class DeckTest {
	Deck deck;
	ArrayList<Card> currentDeck;

	@BeforeEach
	public void setUp() {
		deck = new Deck();
		currentDeck = deck.getCurrentDeck();
	}
	
	@Test
	public void testPriority() {
		boolean isEquals = false;

		for(Card card : currentDeck) {
			for(Card card2 : currentDeck) {
				if (!card.equals(card2) && card.getPriority() == card2.getPriority()) {
					isEquals = true;
					break;
				}
			}
		}
		assertFalse(isEquals, "Should be false because no cards should have the same priority, but was true");
	}

	@Test
	public void testNumCards() {
		int move1 = 0;
		int move2 = 0;
		int move3 = 0;
		int uTurn = 0;
		int backUp = 0;
		int turnLeft = 0;
		int turnRight = 0;

		for(Card card : currentDeck) {
			switch (card.getType()) {
				case "move1":
					move1++;
					break;
				case "move2":
					move2++;
					break;
				case "move3":
					move3++;
					break;
				case "uTurn":
					uTurn++;
					break;
				case "backUp":
					backUp++;
					break;
				case "turnLeft":
					turnLeft++;
					break;
				case "turnRight":
					turnRight++;
					break;
			}
		}
		assertEquals(18, move1);
		assertEquals(12, move2);
		assertEquals(6, move3);
		assertEquals(6, uTurn);
		assertEquals(6, backUp);
		assertEquals(18, turnLeft);
		assertEquals(18, turnRight);
	}
	
	
	@Test
	public void dealTest() {
		Player player = new Player(1,1,Player.texturePath, 1);
		assertEquals(9, player.getPc(), "Player initiated with wrong damage");
		ArrayList<Card> hand = deck.deal(player.getPc());

		boolean validDeck;
		assertEquals(9, hand.size(), "Hand should have size 9, but doesn't");

		player.damage();
		hand = deck.deal(player.getPc());
		assertEquals(8, hand.size(), "Hand should have size 8, but doesn't");


	}

	@Test
	public void resetDeckTest() {

		deck.deal(9); // To remove some cards
		ArrayList<Card> oldDeck = deck.getCurrentDeck();
		deck.resetDeck();
		ArrayList<Card> newDeck = deck.getCurrentDeck();

		assertNotEquals(oldDeck,newDeck,"Expected the deck to reset and therefore different, but wasn't");

	}

	@Test
	public void getCurrentDeckTest(){
		assertEquals(currentDeck, deck.getCurrentDeck());
	}

	@Test
	public void canRemoveCardFromDeck() {
		int initialDeckSize = currentDeck.size();
		Card cardToRemove = new Card();
		cardToRemove.create("move1", 500);
		deck.removeCardFromDeck(cardToRemove);
		assertEquals(initialDeckSize -1, deck.getCurrentDeck().size(), "The deck did not decrease in size");
		assertFalse(deck.getCurrentDeck().contains(cardToRemove), "Card still in deck, but deck decreased in size");

	}

}