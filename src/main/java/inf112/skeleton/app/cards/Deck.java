package inf112.skeleton.app.Cards;

import java.util.*;
import com.esotericsoftware.kryonet.Connection;
import inf112.skeleton.app.GameServer;


public class Deck {
	private ArrayList<String> currentDeck = new ArrayList<String>();
	private ArrayList<String> player1 = new ArrayList<String>();
	private ArrayList<String> player2 = new ArrayList<String>();
	private ArrayList<String> player3 = new ArrayList<String>();
	private ArrayList<String> player4 = new ArrayList<String>();
	
	
	
	public ArrayList<String> createDeck () {
		//ArrayList<String> currentDeck = new ArrayList<String>();
		int i = 0;
		while (i<5) {
			currentDeck.add(MoveCard.createMoveCard1());
			currentDeck.add(MoveCard.createMoveCard2());
			currentDeck.add(MoveCard.createMoveCard3());
			currentDeck.add(RotateRCard.createRotateRCard());
			currentDeck.add(RotateLCard.createRotateLCard());
			currentDeck.add(BackupCard.createBackupCard());
			currentDeck.add(TurnCard.createTurnCard());
			i++;
		}
		currentDeck.add(MoveCard.createMoveCard1());
		Collections.shuffle(currentDeck);
		return currentDeck;
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
	
	public ArrayList<String> smallDeck(int i1,int i2,int i3,int i4,int i5) {
		player1.addAll(i1, currentDeck);
		player1.addAll(i2, currentDeck);
		player1.addAll(i3, currentDeck);
		player1.addAll(i4, currentDeck);
		player1.addAll(i5, currentDeck);
		return player1;
		
		
	}

	public void shuffle() {
		//Collections.shuffle(createDeck());
	
	}
}
