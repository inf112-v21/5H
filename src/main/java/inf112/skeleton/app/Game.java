package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.kryonet.Connection;
import inf112.skeleton.app.cards.Card;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.cards.Hand;
import inf112.skeleton.app.net.GameServerListener;
import inf112.skeleton.app.net.Network;
import inf112.skeleton.app.net.NetworkSettings;
import inf112.skeleton.app.net.PlayerMoved;
import inf112.skeleton.app.sprites.AbstractGameObject;
import inf112.skeleton.app.sprites.Direction;
import inf112.skeleton.app.sprites.Flag;
import inf112.skeleton.app.sprites.Player;

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
    private ArrayList<Flag> flagList;           //List of all flags on map (This will be used in the future to fix order for flag pickup)
    private Player winner;                      //The player that won the game
    private final int numPlayers;                     //Amount of players expected
    private final boolean isServer;             // If true you start a server, if false you start a client and try to connect to server
    private Phase phase;                        //Phase the game is in.
    private Hand hand;                          //The hand of this player
    private boolean hasPrintedHandInfo;          //true if player has been informed of the state of the hand and selected cards, false if changes has been made since last print.
    private boolean hasPrintedState;            //true if player has been informed of current state of game, false otherwise.

    //Network related variables:
    private final Network network;
    private GameServerListener gameServerListener;
    private boolean moveMessagePrinted = false; // Holds whether a moveMessage has been printed to console or not
    private PlayerMoved playerMoved;
    private ArrayList<Hand> allMoves; //All moves received by server from Client(s)


    //Viewport variables
    private OrthographicCamera cameraCards;
    private static final int NUM_VIEWPORTS = 3;
    Texture textureTest;
    private Viewport viewport;
    private SpriteBatch spriteBatchCards;
    private Sprite spriteCards;
    private Stage stageCards;



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

        if (isServer) { //If this instance of game is supposed to be a server it starts one
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
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();


        //Viewport for Cards - tar opp nederste delen av skjermen.
        cameraCards = new OrthographicCamera(Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight()/3);
        viewport = new FitViewport(cameraCards.viewportWidth, cameraCards.viewportHeight,cameraCards);
        viewport.setScreenBounds(Gdx.graphics.getWidth(),0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/3);

        // Prøver å displaye et bildet der.
        spriteBatchCards = new SpriteBatch();
        stageCards = new Stage(viewport,spriteBatchCards);
        // for å sentrere cameraet -> stageCards.getViewport().getCamera().position.setZero();

        spriteCards = new Sprite(new Texture("src/main/resources/cards/cards.jpg"));
        //spriteBatchCards.draw(spriteCards);

        viewport.apply();

//        batchCards = new SpriteBatch();
        //float aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
//        spriteCards.setSize(Gdx.graphics.getWidth(), (Gdx.graphics.getHeight()/3));
//        //cameraCards.position.set((float)(Gdx.graphics.getWidth()/2), (float)(Gdx.graphics.getHeight()/2));
//        cameraCards.translate(cameraCards.viewportHeight/2,cameraCards.viewportHeight/2);



        board = new Board();            //Initialize a board
        board.readBoard(1);  //Read board info from file (for now hardcoded to 1 since we only have Board1.txt)
        spriteMap = new HashMap<>();
        //For all game objects on map, add the identifying string and a corresponding sprite to spriteMap.
        for(AbstractGameObject ago : board.getObjectMap().values()){
            spriteMap.put(ago.getShortName(), new Sprite(new Texture(ago.getTexturePath())));
        }
        alivePlayerList = new ArrayList<>();
        alivePlayerList.addAll(board.getPlayerList());
        flagList = board.getFlagList();
        boardSize = board.getSize();
        hasPrintedHandInfo = false;
    }
    @Override
    public void resize(int width, int height) {
        //viewport.update(width,height);
    }


    @Override
    public void render() {
        camera.update();
        cameraCards.update();



//        spriteBatchCards.begin();
//        stageCards.draw();
//        spriteBatchCards.end();

        //spriteBatchCards.setProjectionMatrix(viewport.getCamera().combined);



        //spriteCards.draw(viewport);


        //ViewportStuff try
//        cameraCards.update();
//        batchCards.begin();
//        //batchCards.setProjectionMatrix(cameraCards.combined);
//        spriteCards.draw(batchCards);
//        batchCards.end();

        //Gjøre Gameviduet om til en Viewport og plasserer den øverst til høyere
        int viewportHeight = Gdx.graphics.getHeight()/3;
        int viewportWidth = Gdx.graphics.getWidth()/ 6;
        Gdx.gl.glViewport(0, viewportHeight, (viewportWidth*5), viewportHeight*2);


        if(isServer) { // If this is a server it runs the move logic.
            serverRenderLogic();
        }
        else {  //Else it runs client logic
            clientRenderLogic();
        }

        //The rendering part of the function:
        batch.begin();
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
        batch.end();
    }


    /**
     * Handles the logic specific to when the instance is a server.
     * Creates and sends data as opposed to receiving and handling data like in clientRenderLogic.
     */
    private void serverRenderLogic() {
        if(phase == Phase.WAIT_CONNECT){
            if(!hasPrintedState){
                System.out.println("Awaiting " + (numPlayers-1) + " more players. Please stand by.");
            }
            hasPrintedState = true;
            if(gameServerListener.getConnectedPlayers()+1 == numPlayers){
                phase = Phase.DEAL_CARDS;
                System.out.println("Changed phase to Deal Cards");
            }
        }
        else if(phase == Phase.DEAL_CARDS){ //If we are in the DEAL_CARDS phase
            dealCardsToAllPlayers(); //Sends a hand of cards to all players
            System.out.println("Dealt cards");
            phase = Phase.CARD_SELECT;
            gameServerListener.resetReceivedMoves();
        }
        else if(phase == Phase.CARD_SELECT){ //If player should choose cards
            if (!(hand.getNumberOfCardsSelected() == 5)) { //If the player has not registered enough cards.
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
                hasPrintedState = false;
            }
        }
        else if(phase == Phase.WAIT_FOR_CLIENT_MOVE){
            if(gameServerListener.numReceivedMoves == gameServerListener.getConnectedPlayers()){
                phase = Phase.MOVE;
                allMoves = gameServerListener.receivedMoves;
                hasPrintedState = false;
            }
            else{
                if(!hasPrintedState){
                    System.out.println("Awaiting client card submissions, please wait.");
                    hasPrintedState = true;
                }
            }
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
        // Checks if this client has yet to submit moves
        if (network.getGameClientListener().hasReceivedHand()) {
            hand = network.getGameClientListener().getHand();
            phase = Phase.CARD_SELECT;
            if (!moveMessagePrinted) { // If it has not printed that it's your move yet, it will
                System.out.println("Your move");
                moveMessagePrinted = true;
            }
            if (!(hand.getNumberOfCardsSelected() == 5)) { //If the player has not registered enough cards.
                if(!hasPrintedHandInfo){
                    printCardInfo();
                    hasPrintedHandInfo = true;
                }
                if(selectMove()){ //If a move is done
                    hasPrintedHandInfo = false; //Update this value so it prints the new state of hand.
                }
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
                System.out.println("Moves sent"); // Tells player of client that their move has been registered.
                network.getGameClientListener().resetNeedMoveInput(); // Set the needMoveInput to false as no input is longer needed until it receives request to move again
                moveMessagePrinted = false; // Reset moveMessagePrinted
                phase = Phase.MOVE;
                network.getGameClientListener().resetHandReceived(); //Reset the bool for having received a hand of cards
            }
        }
        if(phase == Phase.MOVE){
            if(network.getGameClientListener().playerHasMoved()){ //If the client has been notified of another player's move
                playerMoved = network.getGameClientListener().getPlayerMoved(); //Get the notification object
                AbstractGameObject abstractGameObject = board.getObjectMap().get(playerMoved.getShortName()); //Get the object from notification string
                if(abstractGameObject instanceof Player){ //If the object is a player
                    Player playerThatHasMoved = (Player) abstractGameObject; //Cast to player
                    move(playerThatHasMoved, spriteMap.get(playerMoved.getShortName()), playerMoved.getMove()); //Move player locally.
                }
                network.getGameClientListener().resetPlayerHasMoved();
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
        sprite.setSize(camera.viewportWidth/boardSize, camera.viewportWidth/boardSize);
        sprite.setX(x*(camera.viewportWidth/boardSize));
        sprite.setY(y*(camera.viewportHeight/boardSize));
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
                    endTurn();
                }
                break;
        }
        if(playerObject.getScore() >= 3){   //If win condition
            phase = Phase.FINISHED;
            winner = playerObject;
        }
    }

    /**
     * Progresses the game 1 single move at a time, handles logic for selecting which player should move,
     * and fetches the necessary information for the move() function to use.
     */
    private void doOnePlayerMove() {
        //Loop through all the moves in this turn, and select the one with highest priority in position 0 to be the move to be executed.
        Hand handToMove = allMoves.get(0);
        for(Hand hand : allMoves){ //Loop through all moves
            if(hand.getNumberOfCardsSelected() > handToMove.getNumberOfCardsSelected()){ //If the current hand has more cards than selected hand (so it is one move behind)
                handToMove = hand; //Select the new hand.
                continue;
            }
            if(hand.getNumberOfCardsSelected() == handToMove.getNumberOfCardsSelected()){ //If they are on equal amounts of cards
                if(hand.getSelectedCards().size() > 0){
                    if(hand.getFirstCard().getPriority() > handToMove.getFirstCard().getPriority()){ //If the new hand has higher priority on first card
                        handToMove = hand; //Select the new hand.
                    }
                }
            }
        }
        Card card = handToMove.getSelectedCards().remove(0);    //Get the move Card
        String move = card.getType();   //Get the move
        String shortName = handToMove.getPlayerShortName(); //Get name of player to move
        //Create object to inform client(s) of moves.
        playerMoved = new PlayerMoved();
        playerMoved.setShortName(shortName);
        playerMoved.setMove(move);
        Player player = (Player) board.getObjectMap().get(shortName); //Get Player
        Sprite playerSprite = spriteMap.get(shortName); //Get player as Sprite
        move(player, playerSprite, move);   //Move player on server

        //Move player on clients:
        for(Connection connection : gameServerListener.getPlayers()){
            connection.sendTCP(playerMoved);
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
        if(isServer){ //Server related code:
            boolean turnOver = true; //Signals that all queued moves are done
            for(Hand hand : allMoves){
                if(hand.getNumberOfCardsSelected() != 0){ //If there exists a move that is waiting, turn is not over.
                    turnOver = false;
                    break;
                }
            }
            if(turnOver){ //If turn is done, set phase to next phase.
                phase = Phase.DEAL_CARDS;
                gameServerListener.receivedMoves = new ArrayList<>();
            }
            ArrayList<Player> toBeRemoved = new ArrayList<>(); //List of players that died this round
            for(Player player : alivePlayerList){
                if(player.isDead() || player.getPlayerNum() > numPlayers){    //If player died
                    toBeRemoved.add(player); //Add to list over dead players
                    System.out.println(player.getName() + " died!");
                }
            }
            alivePlayerList.removeAll(toBeRemoved); //Remove dead players from list of alive players
            if(alivePlayerList.size() == 1){ //If there is only one player left, the player wins.
                winner = alivePlayerList.get(0);
                phase = Phase.FINISHED;
            }


        }
        else { //Client related code:
            if(network.getGameClientListener().playerHasMoved()){ //Make sure you don't infinitely iterate over a single move
                network.getGameClientListener().resetPlayerHasMoved();
                return;
            }
        }

        //Pause between moves so the user can see whats happening.
        try {
            Thread.sleep(500);
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
}
