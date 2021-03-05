package inf112.skeleton.app;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import inf112.skeleton.app.cards.Card;
import inf112.skeleton.app.cards.Hand;
import inf112.skeleton.app.cards.Deck;

class CardTest {
	private Card card;
	private String type;
	private int priority;
	private String move;

	@BeforeEach
	public void setUp() {
		card = new Card();
		move = "move1";
		priority = 100;
		card.create("move1", priority);
	}
	
	@Test
	public void getPriorityTest() {
		assertEquals(priority,card.getPriority());
	}
	
	@Test
	public void getTypeTest() {
		assertEquals(move, card.getType());
	}

}
