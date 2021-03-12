package inf112.skeleton.app.cards;

public class Card {
	private String type;
	private int priority;
	private String shortName;

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

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getShortName() {
		return shortName;
	}

	@Override
	public String toString() {
		return type + " [" + priority + "]";
	}
}
