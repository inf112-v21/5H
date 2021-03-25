package inf112.skeleton.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import inf112.skeleton.app.cards.Card;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardTest {
	private Card card;
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
