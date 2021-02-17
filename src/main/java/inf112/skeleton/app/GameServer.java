package inf112.skeleton.app;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameServer extends Listener {

    static Server server;
    //Ports that will be listened to
    static int udpPort = 27960;
    static int tcpPort = 27960;

    //Keep tracks of how many connections we have
    private static int connections;
    private final List<Connection> players = new ArrayList<Connection>();

    //How many connections are allowed, based on how many players game can handle
    private static int maxConnections;
    public String receivedMove = "empty";

    public GameServer(int maxPlayers) throws IOException {
        connections = 0;
        maxConnections = maxPlayers -1;
    }

    /*
    Method that runs when someone connects to server
     */
    public void connected(Connection c) {
        System.out.println("Client connected");

        if (connections < maxConnections) {
            connections++;
            players.add(c);
        } else {
            System.out.println("Too many players already");
        }
    }
    /*
    Method that runs when a player disconnects from the server
     */
    public void disconnected(Connection c) {
        connections--;
        players.remove(c);
    }
    // will send a request of a move from a given player
    public void request_move(Connection c) {
        resetReceivedMove();
        requestFromClient moveRequest = new requestFromClient("Move");
        c.sendTCP(moveRequest);
        //c.sendUDP(moveRequest);

    }

    public Connection getPlayer(int index) {
        if (index < players.size()) {
            return players.get(index);
        } else {
            System.out.println("Player doesn't exist");
            return null;
        }

    }

    public int getConnectedPlayers() {
        return players.size();
    }

    public String getReceivedMove() {
        return receivedMove;
    }
    public void setReceivedMove(String move) {
        this.receivedMove = move;
    }
    public void resetReceivedMove() {
        this.receivedMove = "empty";
    }

}
