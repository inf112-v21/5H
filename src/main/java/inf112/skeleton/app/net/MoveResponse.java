package inf112.skeleton.app.net;

/**
 * A class that holds a move for a given player
 */
public class MoveResponse {
    public String move; // The field that holds the move as a string

     public void setMove(String move) {
        this.move = move;
    }

    public String getMove() {
        return move;
    }
}
