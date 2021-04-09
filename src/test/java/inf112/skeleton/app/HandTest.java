package inf112.skeleton.app;

import static org.junit.jupiter.api.Assertions.*;

import inf112.skeleton.app.cards.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import inf112.skeleton.app.cards.Card;
import inf112.skeleton.app.cards.Hand;
import java.util.ArrayList;

class HandTest {
	private Hand hand;
	private Deck deck;
	private ArrayList<Card> cards;
	private ArrayList<Card> currentDeck;
	private String shortname;

	@BeforeEach
	public void setUp() {
		hand = new Hand();
		deck = new Deck();
		shortname = "p1";
		currentDeck = deck.getCurrentDeck();

		hand.create(currentDeck, shortname);
	}

	@Test
	public void numberOfCardsTest() {
		int output = deck.deal(9).size();
		assertEquals(9, output, "Dealt deck should have 9 cards, but didnt");
	}

	@Test
	public void selectCardTest(){
		//Should be able to add 5 cards but not more

		assertTrue(hand.selectCard(0));
		assertTrue(hand.selectCard(1));
		assertTrue(hand.selectCard(2));
		assertTrue(hand.selectCard(3));
		assertTrue(hand.selectCard(4));
		assertFalse(hand.selectCard(5));
	}

	@Test
	public void unSelectCardTest(){

		hand.selectCard(0);
		hand.selectCard(1);
		assertEquals(hand.getAllCards().get(0), hand.getSelectedCards().get(0), "Cards should be the same, but wasn't");
		hand.unSelect(0);
		assertNotEquals(hand.getAllCards().get(0), hand.getSelectedCards().get(0), "Cards should not longer be equals due to unselect, but was");
	}

	@Test
	public void getShortNameTest(){
		assertEquals(shortname, hand.getPlayerShortName());
	}

	@Test
	public void getAllCardsTest(){
		assertEquals(currentDeck, hand.getAllCards());
	}

	@Test
	public void getSelectedCardsTest(){
		hand.selectCard(0);
		assertEquals(hand.getSelectedCards().get(0), currentDeck.get(0));
	}
		
}

