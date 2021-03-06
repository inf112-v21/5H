package inf112.skeleton.app.net;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import inf112.skeleton.app.cards.Card;
import inf112.skeleton.app.cards.Hand;

import java.io.IOException;
import java.util.ArrayList;

public class Network {
    private Server server;
    private Client client;
    private final NetworkSettings networkSettings;
    private GameServerListener gameServerListener;
    private GameClientListener gameClientListener;
    private final int numPlayers;

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
        registerClasses(server.getKryo());
        server.bind(networkSettings.getTcpPort(), networkSettings.getUdpPort());
        server.start();
        gameServerListener = new GameServerListener(numPlayers);
        server.addListener(gameServerListener);
    }

    private void registerClasses(Kryo kryo) {
        kryo.register(RequestToClient.class);
        kryo.register(MoveResponse.class);
        kryo.register(PlayerMoves.class);
        kryo.register(Hand.class);
        kryo.register(Card.class);
        kryo.register(ArrayList.class);
        kryo.register(GameInfoTCP.class);
    }

    /**
     * @throws IOException
     * Starts a client and connects it to server
     * Uses the NetworkSettings.java file to figure out which ip the server has and ports it uses
     * adds a listener of type GameClient.java to client
     * the listeners handles all incoming traffic
     */
    public void startClient() throws IOException {
        client = new Client();
        registerClasses(client.getKryo());
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
    public Server getServer() { return server;}

}
