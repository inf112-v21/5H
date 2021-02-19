package inf112.skeleton.app;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class GameClient extends Listener {

    static Client client;
    // Ip for server and ports to listen to
    static String ip;
    static int tcpPort;
    static int udpPort;
    private boolean needMoveInput = false;

    private String currentRequest = "none";

    public void received (Connection c, Object p) {

        if (p instanceof requestFromClient) {
            requestFromClient receivedRequest = (requestFromClient) p;
            currentRequest = receivedRequest.getRequestType();
            //String message = receivedRequest.getRequestMessage();
            if (currentRequest.equals("Move")) {
                needMoveInput = true;
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


    public void resetNeedMoveInput() {
        needMoveInput = false;
    }
    public boolean getNeedMoveInput() {
        return needMoveInput;
    }
}
