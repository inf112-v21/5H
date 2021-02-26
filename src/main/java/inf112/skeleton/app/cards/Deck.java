package inf112.skeleton.app.cards;

import java.util.*;
import com.esotericsoftware.kryonet.Connection;
import inf112.skeleton.app.GameServer;


public class Deck {
	private List<Card> currentDeck = new ArrayList();
	private CardCollection player1 = new CardCollection();
	private CardCollection player2 = new CardCollection();
	private CardCollection player3 = new CardCollection();
	private CardCollection player4 = new CardCollection();
	
	public Deck() {
		createDeck();
	}
	
	
	
	public void createDeck () {
		//ArrayList<String> currentDeck = new ArrayList<String>();
		for (int i = 0; i<18; i++) {
			currentDeck.add(new Card("move1",490+i*10));
		}
		Collections.shuffle(currentDeck);
	}
	
	public void deal() {
		createDeck();
		int i = 0;
		while (currentDeck.size() > 0) {
			player1.addAll(i, currentDeck);
			player2.addAll(i+1, currentDeck);
			player3.addAll(i+2, currentDeck);
			player4.addAll(i+3, currentDeck);
			i++;
			//System.out.println(GameServer.players[1]);
		}
		
	}
	
	public void clearList() {
		currentDeck.clear();
		
	}
	


}
