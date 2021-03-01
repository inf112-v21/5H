package inf112.skeleton.app.networkTests;

import com.esotericsoftware.kryonet.Connection;
import inf112.skeleton.app.net.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;



public class GameServerListenerTest {

    GameServerListener gameServerListener;
    Connection connection;
    NetworkSettings serverNetworkSettings;
    NetworkSettings clientNetworkSettings;
    Network serverNetwork;
    Network clientNetwork;
    int numPlayers = 8;


    @BeforeEach
    public void setUp() throws IOException {

        serverNetworkSettings = new NetworkSettings("server", "localhost", 3074, 3074);
        clientNetworkSettings = new NetworkSettings("client", "localhost", 3074, 3074);
        serverNetwork = new Network(serverNetworkSettings, numPlayers);
        serverNetwork.startServer();
        clientNetwork = new Network(clientNetworkSettings, numPlayers);
        clientNetwork.startClient();
        gameServerListener = serverNetwork.getGameServerListener();
        connection = gameServerListener.getPlayer(0);
    }

    @AfterEach
    private void closeAllConnections() { //Closes all connections
        serverNetwork.getServer().close();
        clientNetwork.getClient().close();
    }


    @Test
    public void connectedTest(){
        int before = gameServerListener.getPlayers().size();

        gameServerListener.connected(connection);

        int after = gameServerListener.getPlayers().size();

        assertNotEquals(before,after);

    }

    @Test
    public void disconnectedTest(){

        gameServerListener.connected(connection); //Connects a player
        int before1 = gameServerListener.getPlayers().size(); //Stores number of players

        gameServerListener.disconnected(connection); //Disconnects a player
        int after1 = gameServerListener.getPlayers().size(); //Stores new number of players

        assertNotEquals(before1,after1);

    }

    @Test
    public void receivedTest(){

        RequestToClient requestToClient = new RequestToClient();//RequestToClient class to be used as argument for the receive method.
        String requestType = "Move";
        requestToClient.setRequestType(requestType);

        gameServerListener.received(connection, requestToClient); //Sends requestType to the gameserver

        assertNotEquals(requestType, gameServerListener.getReceivedMove()); //Checks that the requestType has changed
    }

    @Test
    public void getPlayerTest(){

        gameServerListener.connected(connection);
        assertEquals(gameServerListener.getPlayer(0), gameServerListener.getPlayerHashmap().get(0), "Should get the added player, but didn't");

        assertNull(gameServerListener.getPlayer(numPlayers+1), "Should return null because player index is higher than allowed, but didnt.");

    }

    @Test
    public void getConnectedPlayersTest(){
        int size = gameServerListener.getConnectedPlayers();
        assertEquals(1, size);
    }

    @Test
    public void getPlayerHashMapTest(){
        assertNotNull(gameServerListener.getPlayerHashmap());
    }

    @Test
    public void setGetReceivedMoveTest(){
        String move = "Move1";
        gameServerListener.setReceivedMove(move);
        assertEquals(move, gameServerListener.getReceivedMove());
    }

    @Test
    public void resetReceivedMoveTest(){
        String move = "Move1";
        gameServerListener.setReceivedMove(move);
        gameServerListener.resetReceivedMove();
        assertNotEquals(move,gameServerListener.getReceivedMove());
    }



}
