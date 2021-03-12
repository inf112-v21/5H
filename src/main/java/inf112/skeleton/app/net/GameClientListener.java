package inf112.skeleton.app.net;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import inf112.skeleton.app.cards.Hand;

/**
 * Class for Client to listen to server
 */
public class GameClientListener extends Listener {

    private boolean needMoveInput = false; // Keeps track of it a moveRequest has been received and no move sent for said request

    private PlayerMoves playerMoves;
    private boolean hasReceivedMoves;

    private Hand hand;
    private boolean handReceived;

    private GameInfoTCP gameInfo;
    private boolean hasReceivedGameInfo;

    /**
     * @param connection the connection to Server
     * @param receivedObject
     * Handles incoming messages from server
     */
    public void received (Connection connection, Object receivedObject) {
        if (receivedObject instanceof RequestToClient) { //Checks if request is a request to the client
            RequestToClient receivedRequest = (RequestToClient) receivedObject; //Typecasts it if it is
            // Holds the type of request the client currently has
            String currentRequest = receivedRequest.getRequestType(); // Retrieves the type of request as a String
            if (currentRequest.equals("Move")) { //If it is a moveRequest:
                needMoveInput = true; // Sets needMoveInput to true so that the Game.java class will realize its this clients turn to move
                System.out.println("I should move");
            }
        }
        if(receivedObject instanceof PlayerMoves){
            playerMoves = (PlayerMoves) receivedObject;
            hasReceivedMoves = true;
        }
        if(receivedObject instanceof Hand){
            hand = (Hand) receivedObject;
            handReceived = true;
        }
        if(receivedObject instanceof GameInfoTCP){
            gameInfo = (GameInfoTCP) receivedObject;
            hasReceivedGameInfo = true;
        }
    }

    /**
     * Method that can be run after a move has been done to register that no move is needed at that moment.
     */
    public void resetNeedMoveInput() {
        needMoveInput = false;
    }
    public boolean getNeedMoveInput() {
        return needMoveInput;
    }

    /**
     * @return All the moves for a turn, in the form of a PlayerMoved object.
     */
    public PlayerMoves getAllPlayerMoves() {
        return playerMoves;
    }

    /**
     * @return True if client has received all the moves for one turn, false otherwise
     */
    public boolean hasReceivedAllMoves() {
        return hasReceivedMoves;
    }

    /**
     * Set hasReceivedMoves to false
     */
    public void resetHasReceivedAllMoves() {
        hasReceivedMoves = false;
    }

    /**
     * @return The hand the player has been dealt
     */
    public Hand getHand(){
        return hand;
    }

    /**
     * @return True if player has received a new hand, false otherwise
     */
    public boolean hasReceivedHand(){
        return handReceived;
    }

    /**
     * Resets handReceived to false
     */
    public void resetHandReceived(){
        handReceived = false;
    }

    /**
     * @return True if client has received game info.
     */
    public boolean hasReceivedGameInfo() {
        return hasReceivedGameInfo;
    }

    /**
     * @return the gameInfo object
     */
    public GameInfoTCP getGameInfo() {
        return gameInfo;
    }
}
