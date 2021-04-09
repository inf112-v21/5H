package inf112.skeleton.app.net;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import inf112.skeleton.app.cards.Hand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class for Server to listen to clients
 */
public class GameServerListener extends Listener {

    //Keep tracks of how many connections we have
    private static int numConnections;
    private final HashMap<Integer, Connection> players = new HashMap<>(); // A map from playerNumber to connection.
    private final HashMap<Connection, Integer> reversePlayers = new HashMap<>(); // A map from connection to playerNumber.
    private final ArrayList<Connection> isConnected = new ArrayList<>(); // A list of all currently connected clients.

    //How many connections are allowed, based on how many players game can handle
    private int maxConnections;

    //Received moves, mapped from Player.getShortName() to Hand
    public ArrayList<Hand> receivedMoves;
    public int numReceivedMoves;

    public String receivedMove = "NoMove";

    public GameServerListener(int maxPlayers) {
        numConnections = 0;
        maxConnections = maxPlayers - 1;
        receivedMoves = new ArrayList<>();
    }

    /**
    * Method that runs when someone connects to server
     */
    @Override
    public void connected(Connection c) {
        System.out.println("Client connected");

        if (numConnections < maxConnections) { // Checks if more connections are allowed
            if(players.containsValue(c)){
                isConnected.add(c);
                System.out.println("Player " + (reversePlayers.get(c)+2) + " reconnected!");
            }
            else{
                players.put(numConnections, c); // Adds the connection object to players map, player -> conn
                reversePlayers.put(c, numConnections); //Adds the connection to player map, conn -> player
                isConnected.add(c); //Adds client to list over currently connected clients
                numConnections++; // Increment connections counter
            }
        }
        else {
            System.out.println("Too many players already"); // Prints error message if too many connected.
        }
    }
    /*
    Method that runs when a player disconnects from the server
    Removes them from the players list and registers that connections is one less
     */
    @Override
    public void disconnected(Connection c) {
        isConnected.remove(c);
        System.out.println("Player " + (reversePlayers.get(c)+2) + " disconnected");
    }


    /**
     * Method that is run when a tcp/udp message is received
     * @param connection the connection object
     * @param receivedObject The received object
     *
     */
    public void received (Connection connection, Object receivedObject){
        if (receivedObject instanceof MoveResponse) { // checks if the message contains a move if it does:
            MoveResponse moveResponse = (MoveResponse) receivedObject; // Typecasts the moveResponse for some reason
            System.out.println(moveResponse.move); // prints the received move
            this.receivedMove = moveResponse.move; // registers the move from received object to local variable
        }
        if(receivedObject instanceof Hand){
            Hand hand = (Hand) receivedObject;
            receivedMoves.add(hand);
            numReceivedMoves++;
        }
    }


    // will send a request of a move from a given player
    // Currently not used
    public void requestMove(Connection connection) {
        resetReceivedMove();
        RequestToClient moveRequest = new RequestToClient();
        moveRequest.setRequestType("Move");
        connection.sendTCP(moveRequest);
        //c.sendUDP(moveRequest);

    }

    /**
     * @param index the index of the player you want to get
     * @return player
     * Method that retrieves a player object from players arraylist
     * So that their connection object can be used to send TCP/UDP messages
     * returns null if index is out of bounds
     */
    public Connection getPlayer(int index) {
        if (index < numConnections) {
            if(isConnected.contains(players.get(index))){
                return players.get(index);
            }
            else{
                System.out.println("Player is currently disconnected");
                return null;
            }
        }
        else {
            System.out.println("Player doesn't exist");
            return null;
        }
    }

    /**
     * @return the list of connections
     */
    public List<Connection> getPlayers() {
        return isConnected;
    }

    /**
     * @return Amount of players
     * Method that tells you how many player-clients are connected to server
     */
    public int getConnectedPlayers() {
        return players.size();
    }
    public HashMap<Integer, Connection> getPlayerHashmap(){
        return players;
    }

    public String getReceivedMove() {
        return receivedMove;
    }
    public void setReceivedMove(String move) {
        this.receivedMove = move;
    }
    public void resetReceivedMove() {
        this.receivedMove = "NoMove";
    }
    public void resetReceivedMoves(){
        receivedMoves = new ArrayList<>();
        numReceivedMoves = 0;
    }

    public ArrayList<Hand> getReceivedMoves() {
        return receivedMoves;
    }
}
