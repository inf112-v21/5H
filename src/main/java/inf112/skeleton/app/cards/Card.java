package inf112.skeleton.app.cards;

public class Card {
	
	private final String type;
	private final int priority;
	
	public Card(String type, int priority) {
		this.type = type;
		this.priority = priority;
		
	}

	public String getType() {
		return type;
	}

	public int getPriority() {
		return priority;
	}
	
	

}
