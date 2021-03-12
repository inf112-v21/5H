package inf112.skeleton.app.net;

import inf112.skeleton.app.cards.Card;

import java.util.ArrayList;

/**
 * Class that holds all the moves for one turn, ordered by when the move should be played.
 * getMoves().get(0) is the first card that should be played while getMoves().get(size of moves -1) is the last card to play.
 *
 * Is sent from server to all clients.
 */
public class PlayerMoves {
    private ArrayList<Card> moves; //ArrayList of moves ordered by when the move should be played.

    /**
     * @param moves The list of moves you want to send to clients
     */
    public void setMoves(ArrayList<Card> moves){
        this.moves = moves;
    }

    /**
     * @return The list of moves received from server.
     */
    public ArrayList<Card> getMoves(){
        return moves;
    }
}
