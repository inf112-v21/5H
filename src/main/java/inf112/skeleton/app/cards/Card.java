package inf112.skeleton.app.cards;

import java.util.Objects;

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


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Card card = (Card) o;
		return getPriority() == card.getPriority() && Objects.equals(getType(), card.getType());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getType(), getPriority());
	}
}
