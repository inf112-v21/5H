package inf112.skeleton.app.cards;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> fullHand;
    private ArrayList<Card> selectedCards;
    private String playerShortName;

    /**
     * Initializes the player hand.
     * @param cards An ArrayList of Cards to choose from
     */
    public void create(ArrayList<Card> cards, String playerShortName){
        fullHand = cards;
        selectedCards = new ArrayList<>();
        this.playerShortName = playerShortName;
    }

    /**
     * Selects a card at index cardNum from the Hand.
     * @param cardNum The integer index of card in hand.
     * @return true if the move was registered, false otherwise.
     */
    public boolean selectCard(int cardNum){
        if(selectedCards.size() > 5){
            System.out.println("Max amount selected!");
            return false;
        }
        else if(selectedCards.contains(fullHand.get(cardNum))){
            unSelect(cardNum);
            return true;
        }
        else{
            selectedCards.add(fullHand.get(cardNum));
            System.out.println("Card: "+ fullHand.get(cardNum).getType() + " selected.");
            return true;
        }
    }

    /**
     * Unselects a card at index cardNum from the Hand.
     * @param cardNum The integer index of card in hand.
     */
    public void unSelect(int cardNum){
        selectedCards.remove(fullHand.get(cardNum));
    }

    /**
     * @return shortName of player this hand belongs to
     */
    public String getPlayerShortName() {
        return playerShortName;
    }

    /**
     * @return number of cards selected by player.
     */
    public int getNumberOfCardsSelected(){
        return selectedCards.size();
    }

    public ArrayList<Card> getAllCards() {
        return fullHand;
    }

    public ArrayList<Card> getSelectedCards(){
        return selectedCards;
    }

    public Card getFirstCard() {
        return selectedCards.get(0);
    }
}
