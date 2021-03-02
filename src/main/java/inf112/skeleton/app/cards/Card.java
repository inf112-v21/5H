package inf112.skeleton.app.cards;

public class Card {
	private String type;
	private int priority;

	public void create(String type, int priority){
		this.type = type;
		this.priority = priority;
	}

	public String getType() {
		return type;
	}

	public int getPriority() {
		return priority;
	}

	@Override
	public String toString() {
		return type + " [" + priority + "]";
	}
}
