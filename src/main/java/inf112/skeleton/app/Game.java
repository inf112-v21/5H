package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.kryonet.Connection;
import inf112.skeleton.app.cards.Card;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.cards.Hand;
import inf112.skeleton.app.net.*;
import inf112.skeleton.app.sprites.AbstractGameObject;
import inf112.skeleton.app.sprites.Direction;
import inf112.skeleton.app.sprites.Laser;
import inf112.skeleton.app.sprites.Player;
import org.lwjgl.opengl.GL20;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for handling GUI and game setup/logic
 */
public class Game implements ApplicationListener {
    //Libgdx variables:
    private SpriteBatch batch;
    private Camera camera;

    public Board board;                         //The board being played
    private int boardSize;                      //The number of tiles on board, as of now the board is always 1:1, if we allow for other board this needs to be separated into two values
    private ArrayList<Player> alivePlayerList;  //List of all players that are alive in the game
    private Player winner;                      //The player that won the game
    private int numPlayers;                     //Amount of players expected
    private final boolean isServer;             // If true you start a server, if false you start a client and try to connect to server
    private Phase phase;                        //Phase the game is in.
    private Hand hand;                          //The hand of this player
    private boolean hasPrintedHandInfo;          //true if player has been informed of the state of the hand and selected cards, false if changes has been made since last print.
    private boolean hasPrintedState;            //true if player has been informed of current state of game, false otherwise.
    private String statusMessage;               //Status message to print to user.

    //Optimization/fixing memory leak issue from previous versions
    private Player thisPlayer; //The current instance's player object
    private BitmapFont font; //Font for rendering text to gui
    private Texture bgTexture;

    //Network related variables:
    private final Network network;
    private GameServerListener gameServerListener;
    private boolean moveMessagePrinted = false; // Holds whether a moveMessage has been printed to console or not
    private PlayerMoves playerMoves;
    private ArrayList<Hand> allMoves; //All moves received by server from Client(s)

    //Map that holds Direction and the corresponding movement. I.e. north should move player x += 0, y += 1
    private final HashMap<Direction, Pair> dirMap = new HashMap<>(){{
        put(Direction.NORTH, new Pair(0, 1));
        put(Direction.WEST, new Pair(-1, 0));
        put(Direction.EAST, new Pair(1, 0));
        put(Direction.SOUTH, new Pair(0, -1));
    }};
    private HashMap<String, Sprite> spriteMap;    //Map for getting the corresponding sprite to a string, i.e. g => (Sprite) ground

    /**
     * Constructor for the game class.
     * @param settings The server connection settings object
     * @param numPlayers The number of players that this game should have, only necessary when the user is a server,
     *                   so it is hardcoded for client instances.
     */
    public Game(NetworkSettings settings, int numPlayers) {
        this.numPlayers = numPlayers;

        //Set network variables
        isServer = settings.getState().equals("server"); //True if instance is server
        network = new Network(settings, numPlayers);
        if (settings.getState().equals("test")) {
            //Do nothing as I don't want to connect to any servers for tests with this settings, only want a game object to call methods from.
            System.out.println("Making a test game object!"); //This print statement is here to satisfy codacy so don't remove
        }
        else if (isServer) { //If this instance of game is supposed to be a server it starts one
            try {
                network.startServer();
                gameServerListener = network.getGameServerListener();
            } catch (IOException e) { // Exception thrown if it cannot bind ports
                e.printStackTrace();
            }
        }

        else {  //If this instance of game is not supposed to be a server it starts a client and (tries to) connect to a server
            try {
                network.startClient();
            } catch (IOException e) { //Exception thrown if it cannot connect to given server in 5 seconds
                e.printStackTrace();
            }
        }
        phase = Phase.WAIT_CONNECT;
        hasPrintedState = false;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        board = new Board();            //Initialize a board
        board.readBoard(1);  //Read board info from file (for now hardcoded to 1 since we only have Board1.txt)

        spriteMap = new HashMap<>();
        //For all game objects on map, add the identifying string and a corresponding sprite to spriteMap.
        for(AbstractGameObject ago : board.getObjectMap().values()){
            spriteMap.put(ago.getShortName(), new Sprite(new Texture(ago.getTexturePath())));
        }
        spriteMap.put("backUp", new Sprite(new Texture("src/main/resources/tex/cards/backUp.png")));
        spriteMap.put("move1", new Sprite(new Texture("src/main/resources/tex/cards/move1.png")));
        spriteMap.put("move2", new Sprite(new Texture("src/main/resources/tex/cards/move2.png")));
        spriteMap.put("move3", new Sprite(new Texture("src/main/resources/tex/cards/move3.png")));
        spriteMap.put("turnLeft", new Sprite(new Texture("src/main/resources/tex/cards/turnLeft.png")));
        spriteMap.put("turnRight", new Sprite(new Texture("src/main/resources/tex/cards/turnRight.png")));
        spriteMap.put("uTurn", new Sprite(new Texture("src/main/resources/tex/cards/uTurn.png")));

        alivePlayerList = new ArrayList<>();
        alivePlayerList.addAll(board.getPlayerList());
        boardSize = board.getSize();
        hasPrintedHandInfo = false;
        font = new BitmapFont();
        bgTexture = new Texture("src/main/resources/tex/background.png");
        statusMessage = "";
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void render() {
        camera.update();
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(isServer) { // If this is a server it runs the move logic.
            serverRenderLogic();
        }
        else {  //Else it runs client logic
            clientRenderLogic();
        }

        //The rendering part of the function:
        batch.begin();
        batch.draw(bgTexture, 0, 0);
        if(phase == Phase.FINISHED){ //If the game is over
            Sprite winnerSprite = spriteMap.get(winner.getShortName());
            winnerSprite.setSize(camera.viewportWidth, camera.viewportHeight);
            winnerSprite.setX(1);
            winnerSprite.setY(1);
            winnerSprite.draw(batch);
            Sprite text = new Sprite(new Texture("src/main/resources/tex/win.png"));
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
                //Render ground under whole board
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
        renderUIElements();
        renderCards();
        batch.flush();
        batch.end();
    }
        
    /**
     * Handles the logic specific to when the instance is a server.
     * Creates and sends data as opposed to receiving and handling data like in clientRenderLogic.
     */
    private void serverRenderLogic() {
        if(phase == Phase.WAIT_CONNECT){
            if(!hasPrintedState){
                statusMessage = "Awaiting " + (numPlayers-1) + " more players. Please stand by.";
            }
            hasPrintedState = true;
            if(gameServerListener.getConnectedPlayers()+1 == numPlayers){
                GameInfoTCP gi = new GameInfoTCP();
                gi.setNumPlayers(numPlayers);
                for(Connection connection : gameServerListener.getPlayers()){
                    connection.sendTCP(gi);
                }
                phase = Phase.DEAL_CARDS;
                statusMessage = "Dealing cards...";
            }
        }
        else if(phase == Phase.DEAL_CARDS){ //If we are in the DEAL_CARDS phase
            dealCardsToAllPlayers(); //Sends a hand of cards to all players
            phase = Phase.CARD_SELECT;
            statusMessage = "Select cards to move. Click SUBMIT CARDS when ready (not implemented yet)";
            gameServerListener.resetReceivedMoves();
        }
        else if(phase == Phase.CARD_SELECT){ //If player should choose cards
            if ((hand.getNumberOfCardsSelected() != 5)) { //If the player has not registered enough cards.
                if(!hasPrintedHandInfo){
                    printCardInfo();
                    hasPrintedHandInfo = true;
                }
                if(selectMove()){
                    hasPrintedHandInfo = false;
                }
            }
            else{
                gameServerListener.receivedMoves.add(hand);
                phase = Phase.WAIT_FOR_CLIENT_MOVE;
                statusMessage = "Awaiting other player moves...";
                hasPrintedState = false;
            }
        }
        else if(phase == Phase.WAIT_FOR_CLIENT_MOVE){
            if(gameServerListener.numReceivedMoves == gameServerListener.getConnectedPlayers()){
                phase = Phase.SEND_CARDS;
                statusMessage = "Sending cards to all clients.";
                allMoves = gameServerListener.receivedMoves;
                hasPrintedState = false;
            }
        }
        else if (phase == Phase.SEND_CARDS){
            sendListOfAllMoves();
            phase = Phase.MOVE;
            statusMessage = "Players moving!";
        }
        else if(phase == Phase.MOVE){
            doOnePlayerMove(); //Advances moves by one
        }
    }

    /**
     * Function for handling the logic specific to clients.
     */
    private void clientRenderLogic() {
        // Checks if a disconnection has happened, and reconnects if it's the case.
        if (!network.getClient().isConnected()) {
            try {
                network.reconnectClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(network.getGameClientListener().hasReceivedGameInfo()){
            numPlayers = network.getGameClientListener().getGameInfo().getNumPlayers();
        }
        // Checks if this client has yet to submit moves
        if (network.getGameClientListener().hasReceivedHand()) {
            hand = network.getGameClientListener().getHand();
            phase = Phase.CARD_SELECT;
            if (!moveMessagePrinted) { // If it has not printed that it's your move yet, it will
                statusMessage = "Select cards to move. Click SUBMIT CARDS when ready (not implemented yet)";
                moveMessagePrinted = true;
            }
            if (!(hand.getNumberOfCardsSelected() == 5)) { //If the player has not registered enough cards.
                selectMove();
            }
            else { // If a move is registered it will start the sending process
                if (network.getClient().isConnected()) { // Checks once more if a disconnect has happened
                    network.getClient().sendTCP(hand); // Sends the move object to the server
                }
                else { // reconnects if necessary
                    try {
                        network.reconnectClient();
                        network.getClient().sendTCP(hand); // Sends the move object to the server
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                statusMessage = "Moves sent, awaiting other player moves..."; // Tells player of client that their move has been registered.
                network.getGameClientListener().resetNeedMoveInput(); // Set the needMoveInput to false as no input is longer needed until it receives request to move again
                moveMessagePrinted = false; // Reset moveMessagePrinted
                phase = Phase.MOVE;
                statusMessage = "Players moving!";
                network.getGameClientListener().resetHandReceived(); //Reset the bool for having received a hand of cards
            }
        }
        if(phase == Phase.MOVE){
            if(network.getGameClientListener().hasReceivedAllMoves()){ //If the client has been notified of another player's move
                playerMoves = network.getGameClientListener().getAllPlayerMoves(); //Get the notification object
                network.getGameClientListener().resetHasReceivedAllMoves(); //Reset the bool that says if you have received new moves.
            }
            else{
                if(playerMoves == null){
                    return;
                }
                if(playerMoves.getMoves().size() > 0){ //If there are moves to do
                    doOnePlayerMove(); //Do a move
                }
            }
        }
    }

    /**
     * Prints info about the available cards and the selected cards.
     */
    private void printCardInfo() {
        //Build a string of all cards and selected cards and print to user:
        StringBuilder allCardsString = new StringBuilder();
        StringBuilder selectedCardsString = new StringBuilder();
        ArrayList<Card> allCards = hand.getAllCards();
        ArrayList<Card> selectedCards = hand.getSelectedCards();
        for(int i=0; i<hand.getAllCards().size(); i++){
            if(!selectedCards.contains(allCards.get(i))){
                allCardsString.append(i + 1).append(". ").append(allCards.get(i).toString()).append(" | ");
            }
            if(selectedCards.size() > i){
                selectedCardsString.append(allCards.indexOf(selectedCards.get(i))+1).append(". ").append(selectedCards.get(i).toString()).append(" | ");
            }
        }
        System.out.println("=============================");
        System.out.println("Your cards:");
        System.out.println(allCardsString);
        System.out.println("Selected cards:");
        System.out.println(selectedCardsString);
        System.out.println("=============================");
    }

    /**
     * Help function that sets the sprite to its correct size and position.
     * @param sprite The sprite to be adjusted
     * @param x The x value of the sprite
     * @param y The y value of the sprite
     */
    private void renderSprite(Sprite sprite, int x, int y) {
        sprite.setSize((camera.viewportWidth/2/boardSize), camera.viewportWidth/2/boardSize);
        sprite.setX(x*((camera.viewportWidth/16*8)/boardSize));
        sprite.setY(y*((camera.viewportHeight/9*8)/boardSize));
    }

    /**
     * Renders UI elements to the user's screen.
     */
    private void renderUIElements(){
        //Draw status message
        if(!statusMessage.equals("")){
            font.getData().setScale(1);
            font.draw(batch, statusMessage, 720, 35);
        }
        if(hand == null) return;
        if(thisPlayer == null){ //Gets this instance's player object if we don't have it saved
            if(isServer){
                thisPlayer = alivePlayerList.get(0);
            }
            else{
                thisPlayer = (Player) board.getObjectMap().get(hand.getPlayerShortName());
            }
        }
        Sprite drawPlayer = spriteMap.get(thisPlayer.getShortName()); //Get player as sprite
        drawPlayer.setX(177);
        drawPlayer.setY(647);
        drawPlayer.setSize(68,68);
        drawPlayer.draw(batch); //Draw player object in the GUI

        //Draw HP and PC
        font.getData().setScale(5); //Size up font so its readable
        font.draw(batch, ""+thisPlayer.getHp(), 444, 709);
        font.draw(batch, ""+thisPlayer.getPc(), 574, 709);
    }

    /**
     * Function for displaying all and selected cards in GUI.
     */
    private void renderCards(){
        int allOffsetX = 0;   //Keep track of x offset of all cards
        int allOffsetY = 0; //Keep track of y offset of all cards
        int selectedOffsetX = 0;  //Keep track of x offset of selected cards
        if(hand == null) return;
        ArrayList<Card> allCards = hand.getAllCards();
        ArrayList<Card> selectedCards = hand.getSelectedCards();
        font.getData().setScale(1); //Reset font size to 1
        for(Card c : allCards){ //Loop through all cards and print the ones not selected
            Sprite cSprite = spriteMap.get(c.getType()); //Get sprite for current card
            if(!selectedCards.contains(c)){ //If card is selected draw it in the selectedCards box
                if(allOffsetX > 500){
                    allOffsetX = 0;
                    allOffsetY = 150;
                }
                cSprite.setScale(1f);
                cSprite.setX(645 + allOffsetX);
                cSprite.setY(530 - allOffsetY);
                cSprite.draw(batch); //Draw card
                font.setColor(Color.WHITE);
                font.draw(batch, ""+ (allCards.indexOf(c)+1), (690+allOffsetX), (549 - allOffsetY)); //Draw number used to select card
                //Draw priority:
                font.setColor(Color.YELLOW);
                font.draw(batch, ""+ c.getPriority(), (701 + allOffsetX), (666 - allOffsetY));
                font.setColor(Color.WHITE);
                allOffsetX += 100;
            }
        }
        for(Card c : selectedCards){ //Loop through selected cards and draw them
            Sprite cSprite = spriteMap.get(c.getType()); //Get sprite for current card
            int indexOfCard = allCards.indexOf(c);
            cSprite.setScale(1f);
            cSprite.setX(645 + (selectedOffsetX));
            cSprite.setY(160);
            cSprite.draw(batch);
            font.draw(batch, ""+(indexOfCard+1), (690 + selectedOffsetX), 179); //Draw number used to select card
            //Draw priority:
            font.setColor(Color.YELLOW);
            font.draw(batch, ""+ c.getPriority(), (701 + selectedOffsetX), 296);
            font.setColor(Color.WHITE);
            selectedOffsetX += 100;
        }
    }

    /**
     * Function for path tracing from a laser to an object that can be hit by it, damages players if
     * the object is a player as opposed to a wall.
     */
    public void fireLasers(ArrayList<Laser> laserList){
        for(Laser laser : laserList){
            Pair dir = dirMap.get(laser.getDirection());
            Pair currentPos = laser.getCoordinates();
            while(true) {
                if(currentPos.getX()+dir.getX() < 0 || currentPos.getX()+dir.getX() > boardSize){
                    break;
                }
                else if(currentPos.getY()+dir.getY() < 0 || currentPos.getY()+dir.getY() > boardSize){
                    break;
                }
                AbstractGameObject object = board.getPosition(currentPos.getX()+dir.getX(), currentPos.getY()+dir.getY());
                if (object.getShortName().equals("w")){
                    break;
                }
                else if(object.getShortName().matches("p\\d+")){
                    Player player = (Player) object;
                    player.damage();
                    System.out.println(player.getPc());
                    break;
                }
                currentPos = new Pair(currentPos.getX()+dir.getX(), currentPos.getY()+dir.getY());
                //Add an else here for updating texture and adding a pause between
            }
        }
    }
    public ArrayList<Laser> getPlayerLasers() {
        ArrayList<Laser> playerLasers = new ArrayList<>();

        for (Player player : alivePlayerList) {
            Laser playerLaser = new Laser(player.getCoordinates().getX(), player.getCoordinates().getY(),player.getDirection(), player.getShortName());
            playerLasers.add(playerLaser);
        }
            return playerLasers;
    }

    /**
     * Function for selecting which cards you want to use to move.
     * @return true if move was successfully submitted, false otherwise.
     */
    private boolean selectMove(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            return hand.selectCard(0);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            return hand.selectCard(1);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
            return hand.selectCard(2);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)){
            return hand.selectCard(3);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)){
            return hand.selectCard(4);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)){
            return hand.selectCard(5);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)){
            return hand.selectCard(6);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)){
            return hand.selectCard(7);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)){
            return hand.selectCard(8);
        }
        else{
            return false;
        }
    }

    /**
     * @param playerObject the player object
     * @param playerSprite the player object as sprite
     * @param move the move as a string, i.e. "move2"
     * Takes a moveString and performs the corresponding move.
     * Will change the needed objects to change the representation on the board
     */
    private void move(Player playerObject, Sprite playerSprite, String move) {
        System.out.println(playerObject.getShortName()  + " moved - " + move);
        switch (move) {
            case "move1":  //Move in the direction the player is facing
                Direction dir = playerObject.getDirection();
                Pair pair = dirMap.get(dir);
                playerObject.move(pair.getX(), pair.getY());
                endTurn();
                break;
            case "move2":  //Move in the direction the player is facing
                dir = playerObject.getDirection();
                pair = dirMap.get(dir);
                for(int i=0; i<2; i++){
                    playerObject.move(pair.getX(), pair.getY());
                    endTurn();
                }
                break;
            case "move3":  //Move in the direction the player is facing
                dir = playerObject.getDirection();
                pair = dirMap.get(dir);
                for(int i=0; i<3; i++){
                    playerObject.move(pair.getX(), pair.getY());
                    endTurn();
                }
                break;
            case "backUp":  //Move in the direction the player is facing
                dir = playerObject.getDirection();
                pair = dirMap.get(dir);
                playerObject.move(-pair.getX(), -pair.getY());
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
            case "uTurn":  //Rotate the player left
                for(int i=0; i<2; i++){
                    playerSprite.rotate90(false);
                    playerObject.setDirection(getNewDirection(playerObject.getDirection(), false));
                }
                endTurn();
                break;
        }
        if(playerObject.getScore() >= 3){   //If win condition
            phase = Phase.FINISHED;
            winner = playerObject;
        }
    }

    /**
     * Progresses the game 1 single move at a time, and fetches the necessary information for the move() function to use.
     */
    private void doOnePlayerMove() {
        Card card = playerMoves.getMoves().remove(0);    //Get the move Card
        String move = card.getType();   //Get the move
        String shortName = card.getShortName(); //Get name of player to move
        Player player = (Player) board.getObjectMap().get(shortName); //Get Player
        Sprite playerSprite = spriteMap.get(shortName); //Get player as Sprite
        move(player, playerSprite, move);   //Move player
    }

    /**
     * Function for checking if a player collides with another player, moves the second
     * @param player that is moving
     * @return true if player should move/collide with something
     */
    public boolean collision(Player player){
        Pair dir = dirMap.get(player.getDirection());
        Pair playerPos = player.getCoordinates();
        int newX = playerPos.getX()+dir.getX();
        int newY = playerPos.getY()+dir.getY();
        if ((newX > board.getSize()-1 || newX < 0) || (newY > board.getSize()-1 || newY < 0)) { //If moving out of the board
            return true; //Return true as this is a legal move/push. Dying/resetting player is handled by Player.move
        }
        if(board.getPosition(newX, newY).getShortName().matches("p\\d+")){ //If there is a player where we are moving
            Player pushedPlayer = (Player) board.getPosition(newX, newY); //Get the player
            if(collision(pushedPlayer, dir)){ //Check if pushedPlayer is allowed to move, and if he also collides handle that collision recursively
                pushedPlayer.move(dir.getX(), dir.getY()); // Move the pushed player to correct tile
                System.out.println("Moved " + pushedPlayer.getShortName());
            }
            else{ // If the pushed player can not move then this player is not allowed to either.
                return false;
            }
        }
        else return !board.getPosition(newX, newY).getShortName().matches("w"); //If player hits wall return false
        return true;
    }
    public boolean collision(Player player, Pair dir){ // Overload method for recursive calls where the direction is the same as the original player's
        Pair playerPos = player.getCoordinates();
        int newX = playerPos.getX()+dir.getX();
        int newY = playerPos.getY()+dir.getY();
        if ((newX > board.getSize()-1 || newX < 0) || (newY > board.getSize()-1 || newY < 0)) { //If moving out of the board
            return true; //Return true as this is a legal move/push. Dying/resetting player is handled by Player.move
        }
        if(board.getPosition(newX, newY).getShortName().matches("p\\d+")){ //If there is a player where we are moving
            Player pushedPlayer = (Player) board.getPosition(newX, newY); //Get the player
            if(collision(pushedPlayer, dir)){ //Check if pushedPlayer is allowed to move, and if he also collides handle that collision recursively
                pushedPlayer.move(dir.getX(), dir.getY()); // Move the pushed player to correct tile
                System.out.println("Moved " + pushedPlayer.getShortName());
            }
            else{ // If the pushed player can not move then this player is not allowed to either.
                return false;
            }
        }
        else return !board.getPosition(newX, newY).getShortName().matches("w"); //If player hits wall return false
        return true;
    }
    /**
     * Sends a list of all moves ordered by when the move should be performed to clients so that
     * they can update the board correctly.
     * This change from sending each move to list of moves was made to prevent de-synchronization between moves.
     */
    private void sendListOfAllMoves(){
        ArrayList<Card> listMoves = new ArrayList<>(); //A list to hold all moves.
        while(allMoves.size() > 0){
            //Loop through all the moves in this turn, and select the one with highest priority in position 0 to be the move to be executed.
            Hand handToMove = allMoves.get(0); //Get an initial hand
            for(Hand hand : allMoves){ //Loop through all moves
                if(hand.getNumberOfCardsSelected() > handToMove.getNumberOfCardsSelected()){ //If the current hand has more cards than selected hand (so it is one move behind)
                    handToMove = hand; //Select the new hand.
                    continue;
                }
                if(hand.getNumberOfCardsSelected() == handToMove.getNumberOfCardsSelected() && hand.getSelectedCards().size() > 0 && hand.getFirstCard().getPriority() > handToMove.getFirstCard().getPriority()){ //If they are on equal amounts of cards && If the new hand has higher priority on first card
                    handToMove = hand; //Select the new hand.

                }
            }
            Card card = handToMove.getSelectedCards().remove(0);    //Get the move Card
            String shortName = handToMove.getPlayerShortName(); //Get name of player to move
            card.setShortName(shortName);
            listMoves.add(card);

            if(handToMove.getSelectedCards().size() == 0){
                allMoves.remove(handToMove);
            }
        }
        playerMoves = new PlayerMoves();
        playerMoves.setMoves(listMoves);

        //Send moves to all clients:
        for(Connection connection : gameServerListener.getPlayers()){
            connection.sendTCP(playerMoves);
        }
    }

    /**
     * Function for sending a hand of cards to all players, from the same deck.
     */
    private void dealCardsToAllPlayers(){
        Deck deck = new Deck();
        for(Player player : alivePlayerList){ //For all alive players
            //If player is server, simply set the hand.
            if (player.getShortName().equals("p1")) {
                hand = new Hand();
                hand.create(deck.deal(), player.getShortName());
            }
            //If the player is client, create hand and send to client.
            else {
                Hand currentHand = new Hand();
                currentHand.create(deck.deal(), player.getShortName());
                Connection connectedClient = gameServerListener.getPlayer(player.getPlayerNum()-2);
                if (connectedClient != null) { // Checks that player is connected
                    connectedClient.sendTCP(currentHand); // Sends the requestObject to the retrieved client
                }
            }
        }
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
     * Called after each move, adds a small pause between turns to allow user to see every move, instead of just being
     * able to see the final position of the player. (There is probably a cleaner way to do this than sleep the thread.)
     */
    private void endTurn(){
        ArrayList<Player> toBeRemoved = new ArrayList<>(); //List of players that died this round
        for(Player player : alivePlayerList){
            if(player.isDead() || player.getPlayerNum() > numPlayers){    //If player died
                toBeRemoved.add(player); //Add to list over dead players
                int playerX = player.getCoordinates().getX();
                int playerY = player.getCoordinates().getY();
                if(board.getOriginalPosition(playerX, playerY).getShortName().equals(player.getShortName())){
                    board.updateCoordinate("g", playerX, playerY);
                }
                else{
                    board.updateCoordinate(board.getOriginalPosition(playerX, playerY).getShortName(), playerX, playerY);
                }
                System.out.println(player.getName() + " died!");
            }
        }
        alivePlayerList.removeAll(toBeRemoved); //Remove dead players from list of alive players
        if(alivePlayerList.size() == 1){ //If there is only one player left, the player wins.
            winner = alivePlayerList.get(0);
            phase = Phase.FINISHED;
        }
        else if(isServer){
            if(playerMoves.getMoves().size() == 0){
                phase = Phase.DEAL_CARDS;
            }
        }

        //Pause between moves so the user can see whats happening.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public HashMap<Direction, Pair> getDirMap() {
        return dirMap;
    }
}
