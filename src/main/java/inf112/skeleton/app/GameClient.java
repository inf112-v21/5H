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

    private String currentRequest = "none";

    public GameClient() throws IOException {
        client = new Client();
        client.getKryo().register(requestFromClient.class);

        client.start();
        client.connect(5000, ip, tcpPort, udpPort);
        client.addListener(new GameClient());
    }

    public void received (Connection c, Object p) {

        if (p instanceof requestFromClient) {
            requestFromClient receivedRequest = (requestFromClient) p;
            currentRequest = receivedRequest.getRequestType();
            String message = receivedRequest.getRequestMessage();
        }
    }

    public String getCurrentRequest() {
        return currentRequest;
    }
    public void resetCurrentRequest() {
        currentRequest = "none";
    }

    public void sendMove(String move) {
        //Send move
    }
}
