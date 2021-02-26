package inf112.skeleton.app.cards;

public class Card {
	
	private String type;
	private int priority;
	
	public Card(String type, int priority) {
		this.type = type;
		this.priority = priority;
		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	

}
