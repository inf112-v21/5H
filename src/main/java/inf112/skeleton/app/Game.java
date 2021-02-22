package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import inf112.skeleton.app.Sprites.AbstractGameObject;
import inf112.skeleton.app.Sprites.Direction;
import inf112.skeleton.app.Sprites.Flag;
import inf112.skeleton.app.Sprites.Player;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for handling GUI and game setup/logic
 */
public class Game implements ApplicationListener {
    private SpriteBatch batch;
    private Camera camera;

    private boolean isFinished;                 //If the game is finished
    public Board board;                         //The board being played
    private int boardSize;                      //The number of tiles on board, as of now the board is always 1:1, if we allow for other board this needs to be separated into two values
    private ArrayList<Player> playerList;       //List of all players on map
    private ArrayList<Player> alivePlayerList;  //List of all players that are alive in the game
    private ArrayList<Flag> flagList;           //List of all flags on map
    private int turn;                           //Counter for turn, will be obsolete once the game is no longer turn based
    private Player winner;                      //The player that won the game
    private boolean pause;                      //If the game is paused this is true
    private int numPlayers;                     //Amount of players expected
    public boolean isServer;                    // If true you start a server, if false you start a client and try to connect to server
    public Server server;
    public Client client;
    public GameServerListener gameServerListener;
    public GameClientListener gameClientListener;

    private final NetworkSettings networkSettings;
    private String moveString = "NoMove";        // Variables that hold move string for client, NoMove means no move have been done yet
    private boolean sentMoveRequest = false;     // Holds whether Server has sent a MoveRequest to client or not
    private boolean moveMessagePrinted = false; // Holds whether a moveMessage has been printed to console or not

    //Map that holds Direction and the corresponding movement. I.e. north should move player x += 0, y += 1
    private final HashMap<Direction, Pair> dirMap = new HashMap<>(){{
        put(Direction.NORTH, new Pair(0, 1));
        put(Direction.WEST, new Pair(-1, 0));
        put(Direction.EAST, new Pair(1, 0));
        put(Direction.SOUTH, new Pair(0, -1));
    }};
    private HashMap<String, Sprite> spriteMap;    //Map for getting the corresponding sprite to a string, i.e. g => (Sprite) ground

    //Enum to keep track of current game phase (card select, move, ...)
    private enum phase {
        CARD_SELECT,
        MOVE,
    }


    /**
     * Constructor for the game class.
     * @param settings The server connection settings object
     */
    public Game(NetworkSettings settings) {
        networkSettings = settings;
        isServer = networkSettings.getState().equals("Server");
    }

    @Override
    public void create() {
        numPlayers = 4;

        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        board = new Board();            //Initialize a board
        board.readBoard(1);  //Read board info from file (for now hardcoded to 1 since we only have Board1.txt)

        spriteMap = new HashMap<>();
        //For all game objects on map, add the identifying string and a corresponding sprite to spriteMap.
        for(AbstractGameObject ago : board.getObjectMap().values()){
            spriteMap.put(ago.getShortName(), new Sprite(new Texture(ago.getTexturePath())));
        }
        playerList = board.getPlayerList();
        alivePlayerList = new ArrayList<>();
        alivePlayerList.addAll(playerList);
        flagList = board.getFlagList();
        boardSize = board.getSize();
        isFinished = false;
        turn = 1;
        pause = false;
        if (isServer) { // If this instance of game is supposed to be a server it starts one
            try {
                startServer();
            } catch (IOException e) { // Exception thrown if it cannot bind ports
                e.printStackTrace();
            }
        } else { // If this instance of game is not supposed to be a server it starts a client and (tries to) connect to a server
            try {
                startClient();
            } catch (IOException e) { //Exception thrown if it cannot connect to given server in 5 seconds
                e.printStackTrace();
            }
        }
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
        client.addListener(gameClientListener);
    }


    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void render() {
        camera.update();
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (isServer) { // If this is a server it runs the move logic.
            doOnePlayerMove();
        }
        else {
            if (!client.isConnected()) { // Checks if a disconnection has happened, and reconnect if it's the case.
                try {
                    reconnectClient();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (gameClientListener.getNeedMoveInput()) { // Checks if it is this client's turn to move
                if (!moveMessagePrinted) { // If it has not printed that it's your move yet, it will
                    System.out.println("Your move");
                    moveMessagePrinted = true;
                }
                if (moveString.equals("NoMove") ) { // If no move has been registered, it will try to register  a move from the keyboard
                    moveString = createClientMove();
                } else { // If a move is registered it will start the sending process
                    MoveResponse moveToSend = new MoveResponse();  //Creates an object that can be sent to server with the intended move
                    moveToSend.setMove(moveString); // registers the move on the object
                    if (client.isConnected()) { // Checks once more if a disconnect has happened
                        client.sendTCP(moveToSend); // Sends the move object to the server
                    } else { // reconnects if necessary
                        try {
                            reconnectClient();
                            client.sendTCP(moveToSend); // Sends the move object to the server
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Move sent"); // Tells player of client that their move has been registered.
                    gameClientListener.resetNeedMoveInput(); // Set the needMoveInput to false as no input is longer needed until it receives request to move again
                    moveString = "NoMove"; // Resets move string
                    moveMessagePrinted = false; // Reset moveMessagePrinted
                }
                }
            }
        batch.begin();
        if(isFinished){ //If the game is over
            Sprite winnerSprite = spriteMap.get(winner.getShortName());
            winnerSprite.setSize(camera.viewportWidth, camera.viewportHeight);
            winnerSprite.setX(1);
            winnerSprite.setY(1);
            winnerSprite.draw(batch);
            Sprite text = new Sprite(new Texture("src\\main\\tex\\win.png"));
            text.setX(camera.viewportWidth/5);
            text.setY(camera.viewportHeight/5);
            text.draw(batch);
            batch.end();
            pause();
            return;
        }
        //Loop over all cells in the board
        for(int x = 0; x < boardSize; x++){
            for(int y = 0; y < boardSize; y++){
                //render ground under whole board
                Sprite ground = spriteMap.get("g");
                renderSprite(ground, x, y);
                ground.draw(batch);

                //Render anything on top of ground
                AbstractGameObject object = board.getPosition(x, y);
                if(!object.getShortName().equals("g")){
                    Sprite sprite = spriteMap.get(object.getShortName());
                    renderSprite(sprite, x , y);
                    sprite.draw(batch);
                }
            }
        }
        batch.end();
    }

    /**
     * Help function that sets the sprite to its correct size and position.
     * @param sprite The sprite to be adjusted
     * @param x The x value of the sprite
     * @param y The y value of the sprite
     */
    private void renderSprite(Sprite sprite, int x, int y) {
        sprite.setSize(camera.viewportWidth/boardSize, camera.viewportWidth/boardSize);
        sprite.setX(x*(camera.viewportWidth/boardSize));
        sprite.setY(y*(camera.viewportHeight/boardSize));
    }


    /**
     * @param playerObject the player object
     * @param playerSprite the player object as sprite
     * Method for the server player to  select move, and change the necessary objects to register the moves
     */
    private void serverMove(Player playerObject, Sprite playerSprite) {
        if(Gdx.input.isKeyPressed(Input.Keys.F)){ //Debug win mode
            playerObject.addScore(3);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.R)){ //Debug restart
            dispose();
            create();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.D)){ //Debug restart
            playerObject.damage();
            System.out.println("PC:" + playerObject.getPc() + "| HP: " + playerObject.getHp());
            if(playerObject.isDead()){
                endTurn();
            }
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.UP)){ //Move in the direction the player is facing
            Direction dir = playerObject.getDirection();
            Pair pair = dirMap.get(dir);
            playerObject.move(pair.getX(), pair.getY());
            endTurn();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){ //Rotate the player right
            playerSprite.rotate90(true);
            playerObject.setDirection(getNewDirection(playerObject.getDirection(), true));
            endTurn();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){ //Rotate the player left
            playerSprite.rotate90(false);
            playerObject.setDirection(getNewDirection(playerObject.getDirection(), false));
            endTurn();
        }
        if(playerObject.getScore() >= 3){   //If win condition
            isFinished = true;
            winner = playerObject;
        }
    }

    /**
     * @return moveString
     * Register keyboard input from the client and returns a string representing said (valid) input
     */
    private String createClientMove() {
        String clientMoveString;
        if(Gdx.input.isKeyPressed(Input.Keys.F)){ //Debug win mode
            clientMoveString = "debugWin";
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.R)){ //Debug restart
            clientMoveString = "debugRestart";
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.D)){ //Debug restart
            clientMoveString = "debugD";
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.UP)){ //Move in the direction the player is facing
            clientMoveString = "move1";
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){ //Rotate the player right
            clientMoveString = "turnRight";
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){ //Rotate the player left
            clientMoveString = "turnLeft";
        } else {
            clientMoveString = "NoMove";
        }
        return clientMoveString;
    }

    /**
     * @param playerObject the player object
     * @param playerSprite the player object as sprite
     * @param move
     * Takes a moveString received from a client and registers it on the Server
     * Will change the needed objects to change the representation on the board
     */
    private void doClientMove(Player playerObject, Sprite playerSprite, String move) {
        switch (move) {
            case "debugWin":  //Debug win mode
                playerObject.addScore(3);
                break;
            case "debugRestart":  //Debug restart
                dispose();
                create();
                break;
            case "debugD":  //Debug restart
                playerObject.damage();
                System.out.println("PC:" + playerObject.getPc() + "| HP: " + playerObject.getHp());
                if (playerObject.isDead()) {
                    endTurn();
                }
                break;
            case "move1":  //Move in the direction the player is facing
                Direction dir = playerObject.getDirection();
                Pair pair = dirMap.get(dir);
                playerObject.move(pair.getX(), pair.getY());
                endTurn();
                break;
            case "turnRight":  //Rotate the player right
                playerSprite.rotate90(true);
                playerObject.setDirection(getNewDirection(playerObject.getDirection(), true));
                endTurn();
                break;
            case "turnLeft":  //Rotate the player left
                playerSprite.rotate90(false);
                playerObject.setDirection(getNewDirection(playerObject.getDirection(), false));
                endTurn();
                break;
        }
        if(playerObject.getScore() >= 3){   //If win condition
            isFinished = true;
            winner = playerObject;
        }
    }

    /**
     * Does one player move
     * Will determine if the server host should move a piece or it should request a move from a connected client
     * Currently Server host will take controls of players 1, 3 and 4 and client will get player 2 if connected.
     * This method will be run many times and a move might not be registered on the first run through of the method
     * Therefore there is support for sending requests only once but being able to receive them untill a valid move is made
     * TODO: Add support for more than one connected client.
     */
    private void doOnePlayerMove() {
        if(pause){
            if(Gdx.input.isKeyPressed(Input.Keys.R)){ //Debug restart
                dispose();
                create();
            }
            return;
        }
        Player playerObject = playerList.get(turn-1);   //Get the player whose turn it is
        Sprite playerSprite = spriteMap.get(playerObject.getShortName());   //Get the sprite for the above player
        //If player is dead you skip to the next turn
        //int connectedPlayers = server.getConnectedPlayers()
        if(playerObject.isDead()){
            return;
        }
        if (turn == 1 || turn == 3 || turn == 4) { // If the turn belongs to player 1, 3 or 4 let server player make move
            serverMove(playerObject, playerSprite);
        } else if (turn == 2) { // if player 2s turn
            Connection connectedClient = gameServerListener.getPlayer(0); // Get connected player at index 0 (first connected players)
            if (connectedClient!= null && !sentMoveRequest) { // Checks that there is a player connected from index 0 and that a move request has not already been sent this turn
                //gameServer.request_move(connectedClient);
                gameServerListener.resetReceivedMove(); // Resets received move so that we can make sure when we get one from client
                requestToClient moveRequest = new requestToClient(); // Makes a new request object that can be sent to client
                moveRequest.setRequestType("Move"); // Registers "Move" as the request in the request object
                connectedClient.sendTCP(moveRequest); // Sends the requestObject to the retrieved client
                sentMoveRequest = true; // Changes sentMoveRequest so that no more moverequests will be sent this turn
                }
            if (!gameServerListener.receivedMove.equals("empty")) { // checks if received move has been changed from "empty"
                String move = gameServerListener.getReceivedMove(); // retrieves move from the gameServer listener
                doClientMove(playerObject, playerSprite, move); // Does the retrieved move on the given playerobject with doClientMove method
                System.out.println("Connected client moved!"); //Logs to console that a move has been done
                sentMoveRequest = false; // Resets the sentMoveRequest as this turn is now over
                }
            }
        /*else {
                serverMove(playerObject,playerSprite);
            }*/
        }

    /**
     * Function for rotating a player from one direction to another.
     * @param dir initial direction
     * @param clockwise If true then clockwise, otherwise counter-clockwise
     * @return The new direction
     */
    private Direction getNewDirection(Direction dir, boolean clockwise){
        if(clockwise){
            if(dir.equals(Direction.NORTH)){
                return Direction.EAST;
            }
            if(dir.equals(Direction.EAST)){
                return Direction.SOUTH;
            }
            if(dir.equals(Direction.SOUTH)){
                return Direction.WEST;
            }
            if(dir.equals(Direction.WEST)){
                return Direction.NORTH;
            }
        }
        else{
            if(dir.equals(Direction.NORTH)){
                return Direction.WEST;
            }
            if(dir.equals(Direction.EAST)){
                return Direction.NORTH;
            }
            if(dir.equals(Direction.SOUTH)){
                return Direction.EAST;
            }
            if(dir.equals(Direction.WEST)){
                return Direction.SOUTH;
            }
        }
        return Direction.NORTH; //will never reach this
    }

    /**
     * Called after each move, adds a small pause between turns to prevent accidental moves.
     */
    private void endTurn(){
        System.out.println("Turn: " + turn + " finished");
        Player playerHasMoved = playerList.get(turn-1);
        if(playerHasMoved.isDead()){    //If player died
            alivePlayerList.remove(playerHasMoved);
            System.out.println(playerHasMoved.getName() + " died!");
            if(alivePlayerList.size() == 1){
                winner = alivePlayerList.get(0);
                isFinished = true;
            }
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Need to keep incrementing turn until we find a player that is alive
        boolean foundNext = false;
        while(!foundNext){
            turn++;
            if(turn > board.getPlayerList().size()){
                turn = 1;
            }
            Player playerToMove = playerList.get(turn-1);
            if(playerToMove.isDead()){
                continue;
            }
            foundNext = true;
        }

        System.out.println("---------------------------");
        System.out.println("Player " + turn + " to move");
    }

    @Override
    public void pause() {
        pause = true;
    }

    @Override
    public void resume() {
        pause = false;

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
