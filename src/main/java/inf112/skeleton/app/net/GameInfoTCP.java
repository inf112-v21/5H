package inf112.skeleton.app.net;

public class GameInfoTCP {
    private int numPlayers; //Number of players for the game, to inform client users.

    /**
     * @param numPlayers The number of players this game should have.
     */
    public void setNumPlayers(int numPlayers){
        this.numPlayers = numPlayers;
    }

    /**
     * @return The number of players this game should have.
     */
    public int getNumPlayers(){
        return numPlayers;
    }
}
