package inf112.skeleton.app;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameServer extends Listener {

    //Keep tracks of how many connections we have
    private static int connections;
    private final List<Connection> players = new ArrayList<Connection>(); // Holds all the connection objects of the players

    //How many connections are allowed, based on how many players game can handle
    private static int maxConnections;
    public String receivedMove = "empty";

    public GameServer(int maxPlayers) throws IOException {
        connections = 0;
        maxConnections = maxPlayers -1;
    }

    /**
    * Method that runs when someone connects to server
     */
    @Override
    public void connected(Connection c) {
        System.out.println("Client connected");

        if (connections < maxConnections) { // Checks if more connections are allowed
            connections++; // If they are we add to the connections counter
            players.add(c); // Adds the connection object to players arraylist
        } else {
            System.out.println("Too many players already"); // Prints error message if too many connected.
        }
    }
    /*
    Method that runs when a player disconnects from the server
    Removes them from the players list and registers that connections is one less
     */
    @Override
    public void disconnected(Connection c) {
        connections--;
        players.remove(c);
        System.out.println("Player disconnected");
    }


    /**
     * @param c
     * @param p
     * Method that is run when a tcp/udp message is received
     */
    public void received (Connection c, Object p){
        if (p instanceof  MoveResponse) { // checks if the message contains a move if it does:
            MoveResponse moveResponse = (MoveResponse) p; // Typecasts the moveResponse for some reason
            System.out.println(moveResponse.move); // prints the received move
            this.receivedMove = moveResponse.move; // registers the move from received object to local variable
        }

    }


    // will send a request of a move from a given player
    // Currently not used
    public void request_move(Connection c) {
        resetReceivedMove();
        requestToClient moveRequest = new requestToClient();
        moveRequest.setRequestType("Move");
        c.sendTCP(moveRequest);
        //c.sendUDP(moveRequest);

    }

    /**
     * @param index
     * @return player
     * Method that retrieves a player object from players arraylist
     * So that their connection object can be used to send TCP/UDP messages
     * returns null if index is out of bounds
     */
    public Connection getPlayer(int index) {
        if (index < players.size()) {
            return players.get(index);
        } else {
            System.out.println("Player doesn't exist");
            return null;
        }

    }

    /**
     * @return Amount of players
     * Method that tells you how many player-clients are connected to server
     */
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
