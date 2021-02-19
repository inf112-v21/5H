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
import java.util.concurrent.TimeUnit;


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
    public boolean isServer;
    public Server server;
    public Client client;
    public GameServer gameServer;
    public GameClient gameClient;
    private final NetworkSettings networkSettings = new NetworkSettings();

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
    //START SERVER:
    public void startServer() throws IOException {
        server = new Server();
        server.getKryo().register(requestFromClient.class);
        server.getKryo().register(MoveResponse.class);
        server.bind(networkSettings.tcpPort, networkSettings.udpPort);
        server.start();
        gameServer = new GameServer(numPlayers);
        server.addListener(gameServer);
    }

    //START CLIENT
    public void startClient() throws IOException {
        client = new Client();
        client.getKryo().register(requestFromClient.class);
        client.getKryo().register(MoveResponse.class);
        client.start();
        client.connect(5000, networkSettings.ip, networkSettings.tcpPort, networkSettings.udpPort);
        gameClient = new GameClient();
        client.addListener(gameClient);

    }
    public Game(boolean k) {
        isServer = k;
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
        if (isServer) {
            try {
                startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                startClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void render() {
        camera.update();
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (isServer) {
            doOnePlayerMove();
        }
        else {

                if (!client.isConnected()) {
                    try {
                    client.connect(5000, networkSettings.ip, networkSettings.tcpPort, networkSettings.udpPort); } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (gameClient.getNeedMoveInput()) {
                    String move_string = createClientMove();
                    if (!move_string.equals("NoMove") ) {
                        System.out.println("Your move");
                        MoveResponse moveToSend = new MoveResponse(move_string);
                        client.sendTCP(moveToSend);
                        System.out.println("Move sent");
                        gameClient.resetNeedMoveInput();
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
     * Handles input logic.
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
            clientMoveString = "noMove";
        }
        return clientMoveString;
    }

    private void doClientMove(Player playerObject, Sprite playerSprite, String move) {
        if(move.equals("debugWin")){ //Debug win mode
            playerObject.addScore(3);
        }
        else if(move.equals("debugRestart")){ //Debug restart
            dispose();
            create();
        }
        else if(move.equals("debugD")){ //Debug restart
            playerObject.damage();
            System.out.println("PC:" + playerObject.getPc() + "| HP: " + playerObject.getHp());
            if(playerObject.isDead()){
                endTurn();
            }
        }
        else if(move.equals("move1")){ //Move in the direction the player is facing
            Direction dir = playerObject.getDirection();
            Pair pair = dirMap.get(dir);
            playerObject.move(pair.getX(), pair.getY());
            endTurn();
        }
        else if(move.equals("turnRight")){ //Rotate the player right
            playerSprite.rotate90(true);
            playerObject.setDirection(getNewDirection(playerObject.getDirection(), true));
            endTurn();
        }
        else if(move.equals("turnLeft")){ //Rotate the player left
            playerSprite.rotate90(false);
            playerObject.setDirection(getNewDirection(playerObject.getDirection(), false));
            endTurn();
        }
        if(playerObject.getScore() >= 3){   //If win condition
            isFinished = true;
            winner = playerObject;
        }
    }

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
        if (turn == 1 || turn == 3 || turn == 4) {
            serverMove(playerObject, playerSprite);
        } else if (turn == 2) {
            Connection connectedClient = gameServer.getPlayer(0);
            if (connectedClient!= null) {
                //gameServer.request_move(connectedClient);
                gameServer.resetReceivedMove();
                requestFromClient moveRequest = new requestFromClient();
                moveRequest.setRequestType("Move");
                connectedClient.sendTCP(moveRequest);
                if (gameServer.receivedMove.equals("empty")) {
                    String move = gameServer.getReceivedMove();
                    doClientMove(playerObject, playerSprite, move);
                    System.out.println("Connected client moved!");
                }
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
