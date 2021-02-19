package inf112.skeleton.app;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class GameClient extends Listener {

    private boolean needMoveInput = false; // Keeps track of it a moverequest has been received and no move sent for said request

    private String currentRequest = "none"; // Holds the type of request the client currently has

    /**
     * @param c
     * @param p
     * Handles incoming messages from server
     */
    public void received (Connection c, Object p) {

        if (p instanceof requestToClient) { //Checks if request is a request to the client
            requestToClient receivedRequest = (requestToClient) p; //Typecasts it if it is
            currentRequest = receivedRequest.getRequestType(); // Retrieves the type of request (a string)
            //String message = receivedRequest.getRequestMessage();
            if (currentRequest.equals("Move")) { //If it is a moveRequest:
                needMoveInput = true; // Sets needMoveInput to true so that the Game.java class will realize its this clients turn to move
                System.out.println("I should move");
            }

        }
    }

    public String getCurrentRequest() {
        return currentRequest;
    }
    public void resetCurrentRequest() {
        currentRequest = "none";
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
}
