package inf112.skeleton.app;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class Network {
    private Server server;
    private Client client;
    private NetworkSettings networkSettings;
    private GameServerListener gameServerListener;
    private GameClientListener gameClientListener;
    private int numPlayers;

    public Network(NetworkSettings networkSettings, int numPlayers){
        this.numPlayers = numPlayers;
        this.networkSettings = networkSettings;
    }


    /**
     * @throws IOException
     * Starts a server and binds ports for it
     * ports to bind is retrieved from the NetworkSettings.java class
     * Also adds a listener to the server of type GameServer.java
     * Listener handles connections and keep track of them as well as handling incoming messages
     */
    public void startServer() throws IOException {
        server = new Server();
        server.getKryo().register(requestToClient.class);
        server.getKryo().register(MoveResponse.class);
        server.bind(networkSettings.getTcpPort(), networkSettings.getUdpPort());
        server.start();
        gameServerListener = new GameServerListener(numPlayers);
        server.addListener(gameServerListener);
    }

    /**
     * @throws IOException
     * Starts a client and connects it to server
     * Uses the NetworkSettings.java file to figure out which ip the server has and ports it uses
     * adds a listener of type GameClient.java to client
     * the listeners handles all incoming traffic
     */
    //START CLIENT
    public void startClient() throws IOException {
        client = new Client();
        client.getKryo().register(requestToClient.class);
        client.getKryo().register(MoveResponse.class);
        client.start();
        client.connect(5000, networkSettings.getIp(), networkSettings.getTcpPort(), networkSettings.getUdpPort());
        gameClientListener = new GameClientListener();
        client.addListener(gameClientListener);
    }

    /**
     * @throws IOException
     * Reconnects client to server if it is no longer connected
     * Also re-adds the listener that was initially added to it.
     */
    public void reconnectClient() throws IOException {
        client.connect(5000, networkSettings.getIp(), networkSettings.getTcpPort(), networkSettings.getUdpPort());
        client.addListener(getGameClientListener());
    }


    public GameClientListener getGameClientListener(){
        return gameClientListener;
    }

    public GameServerListener getGameServerListener(){
        return gameServerListener;
    }
    public Client getClient(){
        return client;
    }

}
