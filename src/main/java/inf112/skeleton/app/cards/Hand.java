package inf112.skeleton.app.cards;

import java.util.ArrayList;

public class Hand {
    private final ArrayList<Card> fullHand;
    private final ArrayList<Card> selectedCards;
    private final String playerShortName;

    /**
     * Initializes the player hand.
     * @param cards An ArrayList of Cards to choose from
     */
    public Hand(ArrayList<Card> cards, String playerShortName){
        fullHand = cards;
        selectedCards = new ArrayList<>();
        this.playerShortName = playerShortName;
    }

    /**
     * Selects a card at index cardNum from the Hand.
     * @param cardNum The integer index of card in hand.
     */
    public void selectCard(int cardNum){
        if(selectedCards.size() > 5){
            System.err.println("Max amount selected!");
        }
        else if(selectedCards.contains(fullHand.get(cardNum))){
            System.err.println("Card already selected");
        }
        else{
            selectedCards.add(fullHand.get(cardNum));
            System.out.println("Card: "+fullHand.get(cardNum).getType()+ " selected.");
        }
    }

    /**
     * Unselects a card at index cardNum from the Hand.
     * @param cardNum The integer index of card in hand.
     */
    public void unSelect(int cardNum){
        if(selectedCards.contains(fullHand.get(cardNum))){
            selectedCards.remove(fullHand.get(cardNum));
        }
        else{
            System.err.println("Card not selected, cannot be unselected");
        }
    }

    /**
     * @return shortName of player this hand belongs to
     */
    public String getPlayerShortName() {
        return playerShortName;
    }
}
