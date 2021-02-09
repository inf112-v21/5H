package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf112.skeleton.app.Sprites.Flag;
import inf112.skeleton.app.Sprites.Player;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for handling GUI and game setup/logic
 */
public class Game implements ApplicationListener {
    private SpriteBatch batch;
    private Camera camera;
    private boolean isFinished;
    public Board board;
    private int boardSize;
    private ArrayList<Player> playerList;
    private ArrayList<Player> alivePlayerList;
    private ArrayList<Flag> flagList;
    private int turn;
    private Player winner;
    private boolean pause;
    private final HashMap<Direction, Pair> dirMap = new HashMap<>(){{
        put(Direction.NORTH, new Pair(0, 1));
        put(Direction.WEST, new Pair(-1, 0));
        put(Direction.EAST, new Pair(1, 0));
        put(Direction.SOUTH, new Pair(0, -1));
    }};

    //Enum to keep track of current game phase (card select, move, ...)
    private enum phase {
        CARD_SELECT,
        MOVE,
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        board = new Board();
        board.readBoard(1);
        playerList = board.getPlayerList();
        alivePlayerList = new ArrayList<>();
        alivePlayerList.addAll(playerList);
        flagList = board.getFlagList();
        boardSize = board.getSize();
        isFinished = false;
        turn = 1;
        pause = false;
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void render() {
        camera.update();
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        checkInput();
        batch.begin();
        if(isFinished){ //If the game is over
            winner.setSize(camera.viewportWidth, camera.viewportHeight);
            winner.setX(1);
            winner.setY(1);
            winner.draw(batch);
            Sprite text = new Sprite(new Texture("src\\main\\tex\\win.png"));
            text.setX(camera.viewportWidth/5);
            text.setY(camera.viewportHeight/5);
            text.draw(batch);
            batch.end();
            pause();
            return;
        }
        for(int x = 0; x < boardSize; x++){
            for(int y = 0; y < boardSize; y++){
                Sprite sprite = board.getPosition(x, y);
                sprite.setSize(camera.viewportWidth/boardSize, camera.viewportWidth/boardSize);
                sprite.setX(x*(camera.viewportWidth/boardSize));
                sprite.setY(y*(camera.viewportHeight/boardSize));
                sprite.draw(batch);
            }
        }
        batch.end();
    }

    /**
     * Handles input logic
     */
    private void checkInput() {
        if(pause){
            if(Gdx.input.isKeyPressed(Input.Keys.R)){ //Debug restart
                dispose();
                create();
            }
            return;
        }
        Player playerTurn = playerList.get(turn-1);
        if(playerTurn.isDead()){
            return;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.F)){ //Debug win mode
            playerTurn.addScore(3);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.R)){ //Debug restart
            dispose();
            create();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.D)){ //Debug restart
            playerTurn.damage();
            System.out.println("PC:" + playerTurn.getPc() + "| HP: " + playerTurn.getHp());
            if(playerTurn.isDead()){
                endTurn();
            }
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            Direction dir = playerTurn.getDirection();
            Pair pair = dirMap.get(dir);
            if(playerTurn.move(pair.getX(), pair.getY())){
                return;
            }
            endTurn();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            playerTurn.rotate90(true);
            playerTurn.setDirection(getNewDirection(playerTurn.getDirection(), true));
            endTurn();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            playerTurn.rotate90(false);
            playerTurn.setDirection(getNewDirection(playerTurn.getDirection(), false));
            endTurn();
        }
        if(playerTurn.getScore() >= 3){
            isFinished = true;
            winner = playerTurn;
        }

    }

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
        if(playerHasMoved.isDead()){
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
        boolean foundNext = false;
        while(!foundNext){
            turn++;
            if(turn > 4){   //This should be changed from hardcoded value to amount of players in the game
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
