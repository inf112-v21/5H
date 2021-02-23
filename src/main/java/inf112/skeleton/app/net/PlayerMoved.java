package inf112.skeleton.app.net;

/**
 * Class that holds the necessary info about a player move.
 * This is sent to clients when a player is registered to be moving at the server.
 */
public class PlayerMoved {

    private String shortName; //Player ID, i.e. "p1" for Player 1, "p2" for Player 2...
    private String move; //The moveString, from Game::moveToString()

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    public String getShortName() {
        return shortName;
    }

    public void setMove(String move){
        this.move = move;
    }
    public String getMove(){
        return move;
    }

}
