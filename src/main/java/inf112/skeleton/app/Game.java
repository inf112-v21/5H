package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.kryonet.Connection;
import inf112.skeleton.app.cards.Card;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.cards.Hand;
import inf112.skeleton.app.net.*;
import inf112.skeleton.app.sprites.*;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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
    private boolean hasPrintedState;            //true if player has been informed of current state of game, false otherwise.
    private String statusMessage;               //Status message to print to user.
    private boolean powerDown;
    private boolean powerDownNextRound;
    private boolean heal;

    //Optimization/fixing memory leak issue from previous versions
    private Player thisPlayer; //The current instance's player object
    private BitmapFont font; //Font for rendering text to gui
    private Texture bgTexture;

    //Sound variables
    private Sound sound;

    //Network related variables:
    private final Network network;
    private GameServerListener gameServerListener;
    private boolean moveMessagePrinted = false; // Holds whether a moveMessage has been printed to console or not
    private PlayerMoves playerMoves;
    private ArrayList<Hand> allMoves; //All moves received by server from Client(s)

    //Map that holds Direction and the corresponding movement. I.e. north should move player x += 0, y += 1
    private final HashMap<Direction, Pair> dirMap = new HashMap<>() {{
        put(Direction.NORTH, new Pair(0, 1));
        put(Direction.WEST, new Pair(-1, 0));
        put(Direction.EAST, new Pair(1, 0));
        put(Direction.SOUTH, new Pair(0, -1));
    }};
    private HashMap<String, Sprite> spriteMap; //Map for getting the corresponding sprite to a string, i.e. g => (Sprite) ground
    private Hand previousHand;
    private HashMap<Player, ArrayList<Card>> playerCardsHashMap;
    private boolean lockedCards = false;
    private int amountLockedCards;
    private boolean setLockedCards = false;
    private int moveCounter;

    //Board related
    private int boardNum;
    private boolean hasCreatedBoard;

    //Buttons
    private ArrayList<Button> buttons;
    private HashMap<Card, Button> buttonMap;
    private boolean submittedCards;
    private Sprite showMute;
    private Texture muteTex;
    private Texture unMuteTex;
    private Sprite showInfo;
    private Texture powerDownOn;
    private Texture powerDownOff;
    private Sprite powerDownSprite;

    //counter1 for pause or resume when clicking the mute button.
    //counter2 for show or hide infoscreen.
    private int count = 0;
    private int count2 = 0;
    private boolean showInfoScreen = false;

    /**
     * Constructor for the game class.
     *
     * @param settings   The server connection settings object
     * @param numPlayers The number of players that this game should have, only necessary when the user is a server,
     *                   so it is hardcoded for client instances.
     */
    public Game(NetworkSettings settings, int numPlayers, int boardNum) {
        this.numPlayers = numPlayers;
        this.boardNum = boardNum;

        //Set network variables
        isServer = settings.getState().equals("server"); //True if instance is server
        network = new Network(settings, numPlayers);
        if(!settings.getState().equals("test")) {
            if (isServer) { //If this instance of game is supposed to be a server it starts one
                try {
                    network.startServer();
                    gameServerListener = network.getGameServerListener();
                } catch (IOException e) { // Exception thrown if it cannot bind ports
                    e.printStackTrace();
                }
            } else {  //If this instance of game is not supposed to be a server it starts a client and (tries to) connect to a server
                try {
                    network.startClient();
                } catch (IOException e) { //Exception thrown if it cannot connect to given server in 5 seconds
                    e.printStackTrace();
                }
            }
        }
        phase = Phase.WAIT_CONNECT;
        hasPrintedState = false;
        hasCreatedBoard = false;
        buttonMap = new HashMap<>();
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font = new BitmapFont();
        bgTexture = new Texture("src/main/resources/tex/background.png");

        muteTex = new Texture("src/main/resources/tex/symbols/muteButton.png");
        unMuteTex = new Texture("src/main/resources/tex/symbols/unMuteButton.png");
        showMute = new Sprite(unMuteTex);
        showInfo = new Sprite(new Texture("src/main/resources/tex/symbols/infoButton.png"));
        powerDownOff = new Texture("src/main/resources/tex/powerDownOff.png");
        powerDownOn = new Texture("src/main/resources/tex/powerDownOn.png");
        powerDownSprite = new Sprite(powerDownOff);
        powerDownSprite.setPosition(1059, 55);

        Stage stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        //Set up buttons for cards
        buttons = new ArrayList<>();
        for(int i=0; i<10; i++){
            Button button = new Button();
            button.setSize(0, 0);
            stage.addActor(button);
            int finalI = i;
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    hand.selectCard(finalI);
                }
            });
            buttons.add(button);
        }

        //Submit cards button
        Button submit = new Button();
        submit.setSize(200, 70);
        submit.setPosition(653, 63);
        stage.addActor(submit);
        submit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                submittedCards = true;
            }
        });


        //Powerdown button
        //Submit cards button
        Button powerDownButton = new Button();
        powerDownButton.setSize(200, 70);
        powerDownButton.setPosition(1070, 63);
        stage.addActor(powerDownButton);
        powerDownButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if(phase != Phase.CARD_SELECT){
                    return;
                }
                if(powerDownNextRound){
                    powerDownNextRound = false;
                    powerDownSprite.setTexture(powerDownOff);
                }
                else{
                    powerDownNextRound = true;
                    powerDownSprite.setTexture(powerDownOn);
                }

            }
        });
        //Insert an info button at the top corner
        Button infoButton = new Button();
        infoButton.setSize(32, 32);
        infoButton.setPosition(1213, 690);
        stage.addActor(infoButton);
        infoButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                showInfoScreen = (count2 % 2) == 0;
                count2 = count2 +1;
            }
        });
      
        //Start looping theme music
        sound = Gdx.audio.newSound(Gdx.files.internal("src/main/resources/music/rbTheme.wav"));
        sound.loop();

        //Insert mutebutton
        Button mute = new Button();
        mute.setSize(32, 32);
        mute.setPosition(1245, 690);
        stage.addActor(mute);
        mute.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if ((count % 2) == 0){
                    sound.pause();
                    count = count +1;
                    showMute.setTexture(muteTex);
                } else {
                    sound.resume();
                    count = count + 1;

                    showMute.setTexture(unMuteTex);
                }}});
        statusMessage = "Awaiting players";
    }

    /**
     * Reads the board from boardNum, and creates all necessary sprites
     */
    public void createBoard(){
        if(hasCreatedBoard){
            return;
        }
        hasCreatedBoard = true;
        board = new Board(); //Initialize a board
        board.readBoard(boardNum);  //Read board info from file

        spriteMap = new HashMap<>();
        //For all game objects on map, add the identifying string and a corresponding sprite to spriteMap.
        for (AbstractGameObject ago : board.getObjectMap().values()) {
            Sprite sprite = new Sprite(new Texture(ago.getTexturePath()));
            boolean rotate = false;
            Direction rotateDir = null;
            if (ago instanceof ConveyorBelt) {
                rotate = true;
                rotateDir = ((ConveyorBelt) ago).getDir();
            }
            else if (ago instanceof ExpressConveyorBelt) {
                rotate = true;
                rotateDir = ((ExpressConveyorBelt) ago).getDir();
            }
            else if (ago instanceof Laser) {
                rotate = true;
                rotateDir = ((Laser) ago).getDirection();
            }
            if (rotate) {
                switch (rotateDir) {
                    case WEST:
                        sprite.rotate90(false);
                        break;
                    case EAST:
                        sprite.rotate90(true);
                        break;
                    case SOUTH:
                        sprite.rotate90(true);
                        sprite.rotate90(true);
                        break;
                    default:
                        break;
                }
            }
            spriteMap.put(ago.getShortName(), sprite);
        }
        spriteMap.put("backUp", new Sprite(new Texture("src/main/resources/tex/cards/backUp.png")));
        spriteMap.put("move1", new Sprite(new Texture("src/main/resources/tex/cards/move1.png")));
        spriteMap.put("move2", new Sprite(new Texture("src/main/resources/tex/cards/move2.png")));
        spriteMap.put("move3", new Sprite(new Texture("src/main/resources/tex/cards/move3.png")));
        spriteMap.put("turnLeft", new Sprite(new Texture("src/main/resources/tex/cards/turnLeft.png")));
        spriteMap.put("turnRight", new Sprite(new Texture("src/main/resources/tex/cards/turnRight.png")));
        spriteMap.put("uTurn", new Sprite(new Texture("src/main/resources/tex/cards/uTurn.png")));
        //Create sprites for showing score/hp/pc as text scales badly to the required size
        for (int i = 0; i<10; i++) {
            String textureString = "src/main/resources/numbers/" + i + ".png";
            spriteMap.put(String.valueOf(i),new Sprite(new Texture(textureString)));
        }
        createAlivePlayerList();
        boardSize = board.getSize();
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void render() {
        //System.out.println(Gdx.input.getX() + "," + Gdx.input.getY());
        camera.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (isServer) { // If this is a server it runs the move logic.
            serverRenderLogic();
        } else {  //Else it runs client logic
            clientRenderLogic();
        }


        //The rendering part of the function:
        batch.begin();
        batch.draw(bgTexture, 0, 0);
        if (phase == Phase.WAIT_CONNECT){
            renderUIElements();
            batch.end();
            return;
        }
        if (phase == Phase.FINISHED) { //If the game is over
            Sprite winnerSprite = spriteMap.get(winner.getShortName());
            winnerSprite.setSize(camera.viewportWidth, camera.viewportHeight);
            winnerSprite.setX(1);
            winnerSprite.setY(1);
            winnerSprite.draw(batch);
            Sprite text = new Sprite(new Texture("src/main/resources/tex/win.png"));
            text.setX(camera.viewportWidth / 5);
            text.setY(camera.viewportHeight / 5);
            text.draw(batch);
            batch.end();
            pause();
            return;
        }
        //Loop over all cells in the board
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                //Render ground under whole board
                Sprite ground = spriteMap.get("g");
                renderSprite(ground, x, y);
                ground.draw(batch);

                //Render anything on top of ground
                AbstractGameObject object = board.getPosition(x, y);
                if (!object.getShortName().equals("g")) {
                    Sprite sprite = spriteMap.get(object.getShortName());
                    renderSprite(sprite, x, y);
                    sprite.draw(batch);
                }
            }
        }
        renderUIElements();
        renderCards();
        if (showInfoScreen){ displayInfo();}
        batch.flush();
        batch.end();
    }
    private void displayInfo(){
        Sprite displayRules = new Sprite(new Texture("src/main/resources/tex/infoAndCredcc.png"));
        displayRules.setSize(698,763);
        displayRules.setX(325);
        displayRules.setY(-40);
        displayRules.draw(batch);
    }

    /**
     * Handles the logic specific to when the instance is a server.
     * Creates and sends data as opposed to receiving and handling data like in clientRenderLogic.
     */
    private void serverRenderLogic() {
        if (phase == Phase.WAIT_CONNECT) {
            if (!hasPrintedState) {
                statusMessage = "Awaiting " + (numPlayers - 1) + " more players. Please stand by.";
            }
            hasPrintedState = true;
            if (gameServerListener.getConnectedPlayers() + 1 == numPlayers) {
                GameInfoTCP gi = new GameInfoTCP();
                gi.setNumPlayers(numPlayers);
                gi.setBoardNum(boardNum);
                for (Connection connection : gameServerListener.getPlayers()) {
                    connection.sendTCP(gi);
                }
                phase = Phase.DEAL_CARDS;
                createBoard();
                statusMessage = "Dealing cards...";
            }
        } else if (phase == Phase.DEAL_CARDS) { //If we are in the DEAL_CARDS phase
            dealCardsToAllPlayers(); //Sends a hand of cards to all players
            if(!buttonMap.isEmpty()){
                buttonMap = new HashMap<>();
            }
            for(int i=0; i<hand.getAllCards().size(); i++){
                buttonMap.put(hand.getAllCards().get(i), buttons.get(i));
            }
            phase = Phase.CARD_SELECT;
            submittedCards = false;
            statusMessage = "Select cards to move. Click SUBMIT CARDS when ready";
            gameServerListener.resetReceivedMoves();
        } else if (phase == Phase.CARD_SELECT) { //If player should choose cards
            if(powerDown && !submittedCards) {
                previousHand = hand.getCopy();
                submittedCards = true;
                for(int i = 0; i < hand.getNumberOfCardsSelected(); i++){
                    hand.unSelect(i);
                }
                ArrayList<Card> powerDownCards = new ArrayList<>();
                for(int i = 0; i<5; i++){
                    Card powerDownCard = new Card();
                    powerDownCard.create("powerdown", 1000);
                    powerDownCards.add(powerDownCard);
                }
                hand.setSelectedCards(powerDownCards);
            }
            else if (!powerDown && hand.getNumberOfCardsSelected() != 5) { //If the player has not registered enough cards.
                if(submittedCards){
                    statusMessage = "You need to select 5 cards to submit!";
                    submittedCards = false;
                }
                selectMove();
            }
            else if(submittedCards){
                if(powerDownNextRound){
                    heal = true;
                }
                if (!powerDown) {
                    previousHand = hand.getCopy();
                }
                gameServerListener.receivedMoves.add(hand);
                hand = previousHand; // To show in GUI
                phase = Phase.WAIT_FOR_CLIENT_MOVE;
                statusMessage = "Awaiting other player moves...";
                hasPrintedState = false;
                lockedCards = false;
                amountLockedCards = 0;
            }
            else{
                selectMove();
            }
        } else if (phase == Phase.WAIT_FOR_CLIENT_MOVE) {
            if (gameServerListener.numReceivedMoves == Math.min(gameServerListener.getConnectedPlayers(), alivePlayerList.size()-1)) {
                phase = Phase.SEND_CARDS;
                statusMessage = "Sending cards to all clients.";
                allMoves = gameServerListener.receivedMoves;
                hasPrintedState = false;
            }
        } else if (phase == Phase.SEND_CARDS) {
            createPlayerCardsHashMap();
            sendListOfAllMoves();
            phase = Phase.MOVE;
            statusMessage = "Players moving!";
        } else if (phase == Phase.MOVE) {
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
        if (network.getGameClientListener().hasReceivedGameInfo()) {
            numPlayers = network.getGameClientListener().getGameInfo().getNumPlayers();
            boardNum = network.getGameClientListener().getGameInfo().getBoardNum();
            createBoard();
        }
        // Checks if this client has yet to submit moves
        if (network.getGameClientListener().hasReceivedHand()) {
            hand = network.getGameClientListener().getHand();
            if(!buttonMap.isEmpty()){
                buttonMap = new HashMap<>();
            }
            for(int i=0; i<hand.getAllCards().size(); i++){
                buttonMap.put(hand.getAllCards().get(i), buttons.get(i));
            }
            lockedCards = network.getGameClientListener().getLockedCards();
            if (lockedCards) {
                amountLockedCards = network.getGameClientListener().getAmountLockedCards();
                if (!setLockedCards) {
                    hand.setLockedCards(getLockedCards());
                    setLockedCards = true;
                }
            }
            phase = Phase.CARD_SELECT;
            if (!moveMessagePrinted) { // If it has not printed that it's your move yet, it will
                statusMessage = "Select cards to move. Click SUBMIT CARDS when ready (not implemented yet)";
                moveMessagePrinted = true;
            }
            if(powerDown && !submittedCards) {
                previousHand = hand.getCopy();
                submittedCards = true;
                for(int i = 0; i < hand.getNumberOfCardsSelected(); i++){
                    hand.unSelect(i);
                }
                ArrayList<Card> powerDownCards = new ArrayList<>();
                for(int i = 0; i<5; i++){
                    Card powerDownCard = new Card();
                    powerDownCard.create("powerdown", 1000);
                    powerDownCards.add(powerDownCard);
                }
                hand.setSelectedCards(powerDownCards);
            }
            else if (!powerDown && hand.getNumberOfCardsSelected() != 5) { //If the player has not registered enough cards.
                if(submittedCards){
                    statusMessage = "You need to select 5 cards to submit!";
                    submittedCards = false;
                }
                selectMove();
            }
            else if (submittedCards) { // If a move is registered it will start the sending process
                if(powerDownNextRound){
                    heal = true;
                }
                if (network.getClient().isConnected()) { // Checks once more if a disconnect has happened
                    network.getClient().sendTCP(hand); // Sends the move object to the server
                    if (!powerDown) {
                        previousHand = hand.getCopy();
                    }
                } else { // reconnects if necessary
                    try {
                        network.reconnectClient();
                        network.getClient().sendTCP(hand); // Sends the move object to the server
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                statusMessage = "Moves sent, awaiting other player moves..."; // Tells player of client that their move has been registered.
                network.getGameClientListener().resetNeedMoveInput(); // Set the needMoveInput to false as no input is longer needed until it receives request to move again
                moveMessagePrinted = false; // Reset moveMessagePrinted
                phase = Phase.MOVE;
                statusMessage = "Players moving!";
                network.getGameClientListener().resetHandReceived(); //Reset the bool for having received a hand of cards
                network.getGameClientListener().resetLockedCardsAndAmountLockedCards();
                setLockedCards = false;
            }
            else{
                selectMove();
            }
        }
        if (phase == Phase.MOVE) {
            if (network.getGameClientListener().hasReceivedAllMoves()) { //If the client has been notified of another player's move
                playerMoves = network.getGameClientListener().getAllPlayerMoves(); //Get the notification object
                network.getGameClientListener().resetHasReceivedAllMoves(); //Reset the bool that says if you have received new moves.
            } else {
                if (playerMoves == null) {
                    return;
                }
                if (playerMoves.getMoves().size() > 0) { //If there are moves to do
                    doOnePlayerMove(); //Do a move
                }
            }

        }
    }

    private void createPlayerCardsHashMap() {
        playerCardsHashMap = new HashMap<>();
        for (Hand hand : gameServerListener.getReceivedMoves()) {
            String shortName = hand.getPlayerShortName(); //Get name of player who owns hand
            Player player = (Player) board.getObjectMap().get(shortName); //Get Player object who owns hand
            playerCardsHashMap.put(player, hand.getSelectedCardsCopy());
        }
    }

    private ArrayList<Card> getLockedCards() {
        ArrayList<Card> lockedCards = new ArrayList<>();
        if (!this.lockedCards) {
            return null;
        }
        int index = 5 - amountLockedCards;
        while (index < 5 && index >= 0) {
            lockedCards.add(previousHand.getSelectedCards().get(index));
            index++;
        }
        return lockedCards;
    }


    /**
     * Help function that sets the sprite to its correct size and position.
     *
     * @param sprite The sprite to be adjusted
     * @param x      The x value of the sprite
     * @param y      The y value of the sprite
     */
    private void renderSprite(Sprite sprite, int x, int y) {
        sprite.setSize((camera.viewportWidth / 2 / boardSize), camera.viewportWidth / 2 / boardSize);
        sprite.setX(x * ((camera.viewportWidth / 16 * 8) / boardSize));
        sprite.setY(y * ((camera.viewportHeight / 9 * 8) / boardSize));
    }

    /**
     * Renders UI elements to the user's screen.
     */
    private void renderUIElements() {
        //Draw status message
        if (!statusMessage.equals("")) {
            font.getData().setScale(1);
            font.draw(batch, statusMessage, 720, 35);
        }
        if (hand == null) return;
        if (thisPlayer == null) { //Gets this instance's player object if we don't have it saved
            if (isServer) {
                thisPlayer = alivePlayerList.get(0);
            } else {
                thisPlayer = (Player) board.getObjectMap().get(hand.getPlayerShortName());
            }
        }
        Sprite drawPlayer = spriteMap.get(thisPlayer.getShortName()); //Get player as sprite
        drawPlayer.setX(177);
        drawPlayer.setY(647);
        drawPlayer.setSize(68, 68);
        drawPlayer.draw(batch); //Draw player object in the GUI

        // Put correct hp/pc/score sprites onto board
        Sprite hpSprite = spriteMap.get(String.valueOf(thisPlayer.getHp()));
        hpSprite.setPosition(432, 650);
        hpSprite.setSize(60,60);
        hpSprite.draw(batch);

        Sprite pcSprite = spriteMap.get(String.valueOf(thisPlayer.getPc()));
        pcSprite.setPosition(563, 650);
        pcSprite.setSize(60,60);
        pcSprite.draw(batch);

        Sprite scoreSprite = spriteMap.get(String.valueOf(thisPlayer.getScore()));
        scoreSprite.setPosition(965, 68);
        scoreSprite.setSize(60,60);
        scoreSprite.draw(batch);

        //sprite for infobutton
        showInfo.setX(1213);
        showInfo.setY(690);
        showInfo.setSize(30,30);
        showInfo.draw(batch);

        //sprite for mutebutton
        showMute.setX(1245);
        showMute.setY(690);
        showMute.setSize(30,30);
        showMute.draw(batch);

        powerDownSprite.draw(batch);
    }

    /**
     * Function for displaying all and selected cards in GUI.
     */
    private void renderCards() {
        if(!powerDown){
            int allOffsetX = 0;   //Keep track of x offset of all cards
            int allOffsetY = 0; //Keep track of y offset of all cards
            int selectedOffsetX = 0;  //Keep track of x offset of selected cards
            if (hand == null) return;
            ArrayList<Card> allCards = hand.getAllCards();
            ArrayList<Card> selectedCards = hand.getSelectedCards();
            font.getData().setScale(1); //Reset font size to 1
            for (Card c : allCards) { //Loop through all cards and print the ones not selected
                Sprite cSprite = spriteMap.get(c.getType()); //Get sprite for current card
                Button button = buttonMap.get(c);
                if (!selectedCards.contains(c)) { //If card is selected draw it in the selectedCards box
                    if (allOffsetX > 500) {
                        allOffsetX = 0;
                        allOffsetY = 150;
                    }
                    cSprite.setScale(1f);
                    cSprite.setX(645 + allOffsetX);
                    cSprite.setY(530 - allOffsetY);
                    cSprite.draw(batch); //Draw card
                    button.setPosition(645 + allOffsetX, 530 - allOffsetY);
                    button.setSize(99, 153);
                    font.setColor(Color.WHITE);
                    font.draw(batch, "" + (allCards.indexOf(c) + 1), (690 + allOffsetX), (549 - allOffsetY)); //Draw number used to select card
                    //Draw priority:
                    font.setColor(Color.YELLOW);
                    font.draw(batch, "" + c.getPriority(), (701 + allOffsetX), (666 - allOffsetY));
                    font.setColor(Color.WHITE);
                    allOffsetX += 100;
                }
            }
            boolean addedSpaceLockedCards = false;
            for (Card c : selectedCards) { //Loop through selected cards and draw them
                if (lockedCards && !addedSpaceLockedCards && Objects.requireNonNull(getLockedCards()).contains(c)) {
                    selectedOffsetX += 100 * (5 - selectedCards.size());
                    addedSpaceLockedCards = true;
                }
                Sprite cSprite = spriteMap.get(c.getType()); //Get sprite for current card
                Button button = buttonMap.get(c);
                int indexOfCard = allCards.indexOf(c);
                cSprite.setScale(1f);
                cSprite.setX(645 + selectedOffsetX);
                cSprite.setY(160);
                button.setPosition(645 + selectedOffsetX, 160);
                button.setSize(99, 153);
                cSprite.draw(batch);
                font.draw(batch, "" + (indexOfCard + 1), (690 + selectedOffsetX), 179); //Draw number used to select card
                //Draw priority:
                font.setColor(Color.YELLOW);
                font.draw(batch, "" + c.getPriority(), (701 + selectedOffsetX), 296);
                font.setColor(Color.WHITE);
                selectedOffsetX += 100;
            }
        }

    }

    /**
     * Function for path tracing from a laser to an object that can be hit by it, damages players if
     * the object is a player as opposed to a wall.
     */
    public void fireLasers(ArrayList<Laser> laserList) {
        for (Laser laser : laserList) {
            Pair dir = dirMap.get(laser.getDirection());
            Pair currentPos = laser.getCoordinates();
            while (true) {
                if (currentPos.getX() + dir.getX() < 0 || currentPos.getX() + dir.getX() > boardSize - 1) {
                    break;
                } else if (currentPos.getY() + dir.getY() < 0 || currentPos.getY() + dir.getY() > boardSize - 1) {
                    break;
                }
                AbstractGameObject object = board.getPosition(currentPos.getX() + dir.getX(), currentPos.getY() + dir.getY());
                if (object.getShortName().equals("w")) {
                    break;
                } else if (object.getShortName().matches("p\\d+")) {
                    Player player = (Player) object;
                    player.damage();
                    break;
                }
                currentPos = new Pair(currentPos.getX() + dir.getX(), currentPos.getY() + dir.getY());
                //Add an else here for updating texture and adding a pause between
            }
        }
    }

    public ArrayList<Laser> getPlayerLasers() {
        ArrayList<Laser> playerLasers = new ArrayList<>();

        for (Player player : alivePlayerList) {
            Laser playerLaser = new Laser(player.getCoordinates().getX(), player.getCoordinates().getY(), player.getDirection(), player.getShortName());
            playerLasers.add(playerLaser);
        }
        return playerLasers;
    }

    /**
     * Function for selecting which cards you want to use to move.
     *
     */
    private void selectMove() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            hand.selectCard(0);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            hand.selectCard(1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            hand.selectCard(2);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            hand.selectCard(3);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            hand.selectCard(4);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
            hand.selectCard(5);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
            hand.selectCard(6);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) {
            hand.selectCard(7);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) {
            hand.selectCard(8);
        }
    }

    /**
     * @param playerObject the player object
     * @param playerSprite the player object as sprite
     * @param move         the move as a string, i.e. "move2"
     *                     Takes a moveString and performs the corresponding move.
     *                     Will change the needed objects to change the representation on the board
     */
    private void move(Player playerObject, Sprite playerSprite, String move) {
        if(!move.equals("powerdown"))
            statusMessage = playerObject.getShortName() + " moved - " + move;
        else
            statusMessage = playerObject.getShortName() + " is in PowerDown";

        switch (move) {
            case "powerdown":  //Powerdown for the player
                if(heal) {
                    playerObject.setPc(9);
                    heal = false;
                }
                endTurn();
                break;
            case "move1":  //Move in the direction the player is facing
                Direction dir = playerObject.getDirection();
                Pair pair = dirMap.get(dir);
                if (collision(playerObject)) {
                    playerObject.move(pair.getX(), pair.getY());
                }
                endTurn();
                break;
            case "move2":  //Move in the direction the player is facing
                dir = playerObject.getDirection();
                pair = dirMap.get(dir);
                for (int i = 0; i < 2; i++) {
                    if (playerObject.isDead()) {
                        break;
                    }
                    if (collision(playerObject)) {
                        playerObject.move(pair.getX(), pair.getY());
                    }
                }
                endTurn();
                break;
            case "move3":  //Move in the direction the player is facing
                dir = playerObject.getDirection();
                pair = dirMap.get(dir);
                for (int i = 0; i < 3; i++) {
                    if (playerObject.isDead()) {
                        break;
                    }
                    if (collision(playerObject)) {
                        playerObject.move(pair.getX(), pair.getY());
                    }
                }
                endTurn();
                break;
            case "backUp":  //Move in the direction the player is facing
                dir = playerObject.getDirection();
                pair = dirMap.get(dir);
                Pair reversedDirection = pair.getReverseDirection();
                if (collision(playerObject, reversedDirection)) {
                    playerObject.move(pair.getX(), pair.getY());
                }
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
                for (int i = 0; i < 2; i++) {
                    playerSprite.rotate90(false);
                    playerObject.setDirection(getNewDirection(playerObject.getDirection(), false));
                }
                endTurn();
                break;
        }
        if (playerObject.getScore() >= 3) {   //If win condition
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
        if(player.isDead()){
            return;
        }
        Sprite playerSprite = spriteMap.get(shortName); //Get player as Sprite
        move(player, playerSprite, move);   //Move player
    }

    /**
     * Function for checking if a player collides with another player, moves the second
     *
     * @param player that is moving
     * @return true if player should move/collide with something
     */
    public boolean collision(Player player) {
        Pair dir = dirMap.get(player.getDirection());
        Pair playerPos = player.getCoordinates();
        int newX = playerPos.getX() + dir.getX();
        int newY = playerPos.getY() + dir.getY();
        if ((newX > board.getSize() - 1 || newX < 0) || (newY > board.getSize() - 1 || newY < 0)) { //If moving out of the board
            return true; //Return true as this is a legal move/push. Dying/resetting player is handled by Player.move
        }
        if (board.getPosition(newX, newY).getShortName().matches("p\\d+")) { //If there is a player where we are moving
            Player pushedPlayer = (Player) board.getPosition(newX, newY); //Get the player
            if (collision(pushedPlayer, dir)) { //Check if pushedPlayer is allowed to move, and if he also collides handle that collision recursively
                pushedPlayer.move(dir.getX(), dir.getY()); // Move the pushed player to correct tile
                statusMessage = ("Pushed " + pushedPlayer.getShortName());
            } else { // If the pushed player can not move then this player is not allowed to either.
                return false;
            }
        } else return !board.getPosition(newX, newY).getShortName().matches("w"); //If player hits wall return false
        return true;
    }

    public boolean collision(Player player, Pair dir) { // Overload method for recursive calls where the direction is the same as the original player's
        Pair playerPos = player.getCoordinates();
        int newX = playerPos.getX() + dir.getX();
        int newY = playerPos.getY() + dir.getY();
        if ((newX > board.getSize() - 1 || newX < 0) || (newY > board.getSize() - 1 || newY < 0)) { //If moving out of the board
            return true; //Return true as this is a legal move/push. Dying/resetting player is handled by Player.move
        }
        if (board.getPosition(newX, newY).getShortName().matches("p\\d+")) { //If there is a player where we are moving
            Player pushedPlayer = (Player) board.getPosition(newX, newY); //Get the player
            if (collision(pushedPlayer, dir)) { //Check if pushedPlayer is allowed to move, and if he also collides handle that collision recursively
                pushedPlayer.move(dir.getX(), dir.getY()); // Move the pushed player to correct tile
                statusMessage = ("Pushed " + pushedPlayer.getShortName());
            } else { // If the pushed player can not move then this player is not allowed to either.
                return false;
            }
        } else return !board.getPosition(newX, newY).getShortName().matches("w"); //If player hits wall return false
        return true;
    }

    /**
     * Sends a list of all moves ordered by when the move should be performed to clients so that
     * they can update the board correctly.
     * This change from sending each move to list of moves was made to prevent de-synchronization between moves.
     */
    private void sendListOfAllMoves() {
        ArrayList<Card> listMoves = new ArrayList<>(); //A list to hold all moves.
        while (allMoves.size() > 0) {
            //Loop through all the moves in this turn, and select the one with highest priority in position 0 to be the move to be executed.
            Hand handToMove = allMoves.get(0); //Get an initial hand
            for (Hand hand : allMoves) { //Loop through all moves
                if (hand.getNumberOfCardsSelected() > handToMove.getNumberOfCardsSelected()) { //If the current hand has more cards than selected hand (so it is one move behind)
                    handToMove = hand; //Select the new hand.
                    continue;
                }
                if (hand.getNumberOfCardsSelected() == handToMove.getNumberOfCardsSelected() && hand.getSelectedCards().size() > 0 && hand.getFirstCard().getPriority() > handToMove.getFirstCard().getPriority()) { //If they are on equal amounts of cards && If the new hand has higher priority on first card
                    handToMove = hand; //Select the new hand.
                }
            }
            Card card = handToMove.getSelectedCards().remove(0);    //Get the move Card
            String shortName = handToMove.getPlayerShortName(); //Get name of player to move
            card.setShortName(shortName);
            listMoves.add(card);

            if (handToMove.getSelectedCards().size() == 0) {
                allMoves.remove(handToMove);
            }
        }
        playerMoves = new PlayerMoves();
        playerMoves.setMoves(listMoves);

        //Send moves to all clients:
        for (Connection connection : gameServerListener.getPlayers()) {
            connection.sendTCP(playerMoves);
        }
    }

    /**
     * Function for sending a hand of cards to all players, from the same deck.
     */
    private void dealCardsToAllPlayers() {
        Deck deck = new Deck();
        //Check if any players have taken enough damage to have locked cards, if they do they will hold onto some of their cards for this turn
        // These cards should therefore be removed from the deck as there should only be one copy of each card
        for (Player player : alivePlayerList) {
            if (player.getPc() < 5) {
                int playerDamage = player.getPc();
                ArrayList<Card> playerCards = playerCardsHashMap.get(player); // Get the cards player had last turn
                int index = 4;
                while (playerDamage < 5 && index >= 0) {
                    Card c = playerCards.get(index);
                    if (c.getType().equals("powerdown")){
                        continue;
                    }
                    deck.removeCardFromDeck(c); // Removes card from deck starting with last card in hand which is locked first.
                    playerDamage++;
                    index--;
                }
            }
        }
        // Loop over players again to deal cards
        for (Player player : alivePlayerList) { //For all alive players
            //If player is server, simply set the hand.
            if (player.getShortName().equals("p1")) {
                hand = new Hand();
                hand.create(deck.deal(player.getPc()), player.getShortName());
                if (hand.getAllCards().size() < 5) {
                    lockedCards = true;
                    amountLockedCards = 5 - hand.getAllCards().size();
                    hand.setLockedCards(getLockedCards());
                }
            }
            //If the player is client, create hand and send to client.
            else {
                Hand currentHand = new Hand();
                currentHand.create(deck.deal(player.getPc()), player.getShortName());
                Connection connectedClient = gameServerListener.getPlayer(player.getPlayerNum() - 2);
                if (connectedClient != null) { // Checks that player is connected
                    connectedClient.sendTCP(currentHand); // Sends the requestObject to the retrieved client
                }
            }
        }
    }

    /**
     * Function for rotating a player from one direction to another.
     *
     * @param dir       initial direction
     * @param clockwise If true then clockwise, otherwise counter-clockwise
     * @return The new direction
     */
    private Direction getNewDirection(Direction dir, boolean clockwise) {
        if (clockwise) {
            if (dir.equals(Direction.NORTH)) {
                return Direction.EAST;
            }
            if (dir.equals(Direction.EAST)) {
                return Direction.SOUTH;
            }
            if (dir.equals(Direction.SOUTH)) {
                return Direction.WEST;
            }
            if (dir.equals(Direction.WEST)) {
                return Direction.NORTH;
            }
        } else {
            if (dir.equals(Direction.NORTH)) {
                return Direction.WEST;
            }
            if (dir.equals(Direction.EAST)) {
                return Direction.NORTH;
            }
            if (dir.equals(Direction.SOUTH)) {
                return Direction.EAST;
            }
            if (dir.equals(Direction.WEST)) {
                return Direction.SOUTH;
            }
        }
        return Direction.NORTH; //will never reach this
    }

    /**
     * Called after each move, adds a small pause between turns to allow user to see every move, instead of just being
     * able to see the final position of the player. (There is probably a cleaner way to do this than sleep the thread.)
     */
    private void endTurn() {
        ArrayList<Player> toBeRemoved = new ArrayList<>(); //List of players that died this round
        for (Player player : alivePlayerList) {
            if (player.isDead() || player.getPlayerNum() > numPlayers) {    //If player died
                toBeRemoved.add(player); //Add to list over dead players
                int playerX = player.getCoordinates().getX();
                int playerY = player.getCoordinates().getY();
                if (board.getOriginalPosition(playerX, playerY).getShortName().equals(player.getShortName())) {
                    board.updateCoordinate("g", playerX, playerY);
                } else {
                    board.updateCoordinate(board.getOriginalPosition(playerX, playerY).getShortName(), playerX, playerY);
                }
                statusMessage = (player.getName() + " died!");
            }
        }
        alivePlayerList.removeAll(toBeRemoved); //Remove dead players from list of alive players

        //Call all functions supposed to run after every player has played one card
        moveCounter += 1;
        if(moveCounter >= alivePlayerList.size()){ //If we have moved enough time for all players to move
            moveCounter = 0;
            runExpressConveyorBelt();
            runConveyorBelt();
            runGear(false);
            fireLasers(board.getLaserList());
            fireLasers(getPlayerLasers());
            for ( Player player : alivePlayerList) {
                player.pickupFlag();
            }

        }

        if (playerMoves.getMoves().size() == 0) {
            if(powerDownNextRound) {
                powerDown = true;
                submittedCards = false;
                powerDownNextRound = false;
                powerDownSprite.setTexture(powerDownOff);
            }
            else if(powerDown) {
                powerDown = false;
                hand = new Hand();
                hand.create(new ArrayList<>(), thisPlayer.getShortName());
            }
        }

        if (alivePlayerList.size() == 1) { //If there is only one player left, the player wins.
            winner = alivePlayerList.get(0);
            phase = Phase.FINISHED;
        }
        else if (isServer) {
            if (playerMoves.getMoves().size() == 0) {
                phase = Phase.DEAL_CARDS;
                moveCounter = 0;
            }
        }

        //Pause between moves so the user can see whats happening.
        try {
            Thread.sleep(2);
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


    public void createAlivePlayerList() {
        alivePlayerList = new ArrayList<>();
        alivePlayerList.addAll(board.getPlayerList());
    }


    /**
     * Runs all the conveyorbelts (including express) and moves players on them one tile in the direction of the belt
     */
    public void runConveyorBelt() {
        for (Player player : alivePlayerList) {
            Pair cbDir;
            Pair playerCords = player.getCoordinates().getCopy();
            if (board.getOriginalPosition(playerCords.getX(), playerCords.getY()).getName().matches("ConveyorBelt")) {
                ConveyorBelt conveyorBelt = (ConveyorBelt) board.getOriginalPosition(playerCords.getX(), playerCords.getY());
                cbDir = dirMap.get(conveyorBelt.getDir()).getCopy();
            }
            else if (board.getOriginalPosition(playerCords.getX(), playerCords.getY()).getName().matches("ExpressConveyorBelt")) {
                ExpressConveyorBelt ExpressConveyorBelt = (ExpressConveyorBelt) board.getOriginalPosition(playerCords.getX(), playerCords.getY());
                cbDir = dirMap.get(ExpressConveyorBelt.getDir()).getCopy();
            }
            else {
                continue;
            }
            int newX = playerCords.getX() + cbDir.getX();
            int newY = playerCords.getY() + cbDir.getY();
            if (board.getPosition(newX, newY).getName().matches("ConveyorBelt")) {
                //If moving to  new conveyorBelt position should not need to think about collision
                // If theres a player there they will also be moved by the conveyorbelt so no collision should occur!
                player.move(cbDir.getX(), cbDir.getY());
            }
           else{
                if (collision(player, cbDir)) {
                    player.move(cbDir.getX(), cbDir.getY());
                    }
           }
        }
    }


    /**
     * Runs all the expressconveyorbelts and moves player on them one tile in the direction of the belt
     */
    public void runExpressConveyorBelt() {
        for (Player player : alivePlayerList) {
            Pair playerCords = player.getCoordinates().getCopy();
            if (board.getOriginalPosition(playerCords.getX(), playerCords.getY()).getName().matches("ExpressConveyorBelt")) {
                ExpressConveyorBelt ExpressConveyorBelt = (ExpressConveyorBelt) board.getOriginalPosition(playerCords.getX(), playerCords.getY());
                Pair cbDir = dirMap.get(ExpressConveyorBelt.getDir()).getCopy();
                int newX = playerCords.getX() + cbDir.getX();
                int newY = playerCords.getY() + cbDir.getY();
                if (board.getOriginalPosition(newX, newY).getName().matches("ExpressConveyorBelt")) {
                    //If moving to  new expressconveyorBelt position should not need to think about collision
                    // If theres a player there they will also be moved by the expressconveyorbelt so no collision should occur!
                    player.move(cbDir.getX(), cbDir.getY());
                }
                else{
                    if (collision(player, cbDir)) {
                        player.move(cbDir.getX(), cbDir.getY());
                    }
                }
            }
        }
    }

    /**
     * Runs all the gears on the board and turns the players on them accordingly
     * Also turns the sprites for the board visualization
     */
    public void runGear(boolean test) {
        for (Player player : alivePlayerList) {
            if (board.getOriginalPosition(player.getCoordinates().getX(), player.getCoordinates().getY()).getName().matches("Gear")) {
                Gear gear = (Gear) board.getOriginalPosition(player.getCoordinates().getX(), player.getCoordinates().getY());
                player.setDirection(getNewDirection(player.getDirection(), gear.isClockwise()));
                if (!test) {
                    Sprite playerSprite = spriteMap.get(player.getShortName());
                    playerSprite.rotate90(gear.isClockwise());
                }
            }
        }
    }
}
