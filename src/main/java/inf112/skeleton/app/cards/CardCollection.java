package inf112.skeleton.app.cards;
import com.esotericsoftware.kryonet.Client;

import java.util.ArrayList;
import java.util.List;

public class CardCollection {
	
	private List<Card> cards = new ArrayList<Card>();
	private CardCollection subCards = new CardCollection();
	
	public void addCard(Card card) {
		cards.add(card);
	}
	
	public List<Card> getCard() {
		return cards;
	}
	
	public void selectCards(int i1,int i2,int i3,int i4,int i5) {
		subCards.addCard(cards.get(i1));
		subCards.addCard(cards.get(i2));
		subCards.addCard(cards.get(i3));
		subCards.addCard(cards.get(i4));
		subCards.addCard(cards.get(i5));
	
	}

	public CardCollection sendSubCards() {
		return subCards;
	}
	
	public void sendSubCards(Client client) {
		client.sendTCP(subCards);
		
	}
}
