package inf112.skeleton.app.net;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import inf112.skeleton.app.cards.Hand;

/**
 * Class for Client to listen to server
 */
public class GameClientListener extends Listener {

    private boolean needMoveInput = false; // Keeps track of it a moveRequest has been received and no move sent for said request

    private PlayerMoved playerMoved;
    private boolean playerHasMoved;

    private Hand hand;
    private boolean handReceived;

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
        if(receivedObject instanceof PlayerMoved){
            playerMoved = (PlayerMoved) receivedObject;
            playerHasMoved = true;
        }
        if(receivedObject instanceof Hand){
            hand = (Hand) receivedObject;
            handReceived = true;
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

    public PlayerMoved getPlayerMoved() {
        return playerMoved;
    }

    public boolean playerHasMoved() {
        return playerHasMoved;
    }

    public void resetPlayerHasMoved() {
        playerHasMoved = false;
    }

    public Hand getHand(){
        return hand;
    }
    public void resetHandReceived(){
        handReceived = false;
    }
    public boolean hasReceivedHand(){
        return handReceived;
    }

}
