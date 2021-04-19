package inf112.skeleton.app.cards;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> fullHand;
    private ArrayList<Card> selectedCards;
    private ArrayList<Card> playerSelectedCards;
    private boolean hasLockedCards;
    private ArrayList<Card> lockedCards;
    private String playerShortName;


    /**
     * Initializes the player hand.
     * @param cards An ArrayList of Cards to choose from
     */
    public void create(ArrayList<Card> cards, String playerShortName){
        fullHand = cards;
        selectedCards = new ArrayList<>();
        playerSelectedCards = new ArrayList<>();
        this.playerShortName = playerShortName;
        hasLockedCards = fullHand.size() < 5;
    }

    /**
     * Selects a card at index cardNum from the Hand.
     * @param cardNum The integer index of card in hand.
     * @return true if the move was registered, false otherwise.
     */
    public boolean selectCard(int cardNum){
        if (cardNum > fullHand.size()-1) {
            System.out.println("Card selection outside current hand size");
            return false;
        }
        if(playerSelectedCards.contains(fullHand.get(cardNum))){
            unSelect(cardNum);
            System.out.println("Unselected");
        }
        else if(selectedCards.size() >= 5){
            System.out.println("Max amount selected!");
            return false;
        }
        else{
            playerSelectedCards.add(fullHand.get(cardNum));
            System.out.println("Card: "+ fullHand.get(cardNum).getType() + " selected.");
        }
        if (hasLockedCards) {
            selectedCards = new ArrayList<>();
            selectedCards.addAll(playerSelectedCards);
            selectedCards.addAll(lockedCards);
        }
        else {
            selectedCards = playerSelectedCards;
        }
        return true;
    }

    /**
     * Unselects a card at index cardNum from the Hand.
     * @param cardNum The integer index of card in hand.
     */
    public void unSelect(int cardNum){
        playerSelectedCards.remove(fullHand.get(cardNum));
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

    public void setSelectedCards(ArrayList<Card> selectedCards) {
        this.selectedCards = selectedCards;
    }

    public ArrayList<Card> getSelectedCardsCopy() {
        return new ArrayList<>(selectedCards);
    }

    public Card getFirstCard() {
        return selectedCards.get(0);
    }

    public void setLockedCards(ArrayList<Card> lockedCards) {
        this.lockedCards = lockedCards;
        selectedCards = lockedCards;
    }

    public Hand getCopy() {
        Hand copy = new Hand();
        copy.create(getAllCards(), getPlayerShortName());
        copy.setSelectedCards(getSelectedCardsCopy());
        return copy;
    }
}
