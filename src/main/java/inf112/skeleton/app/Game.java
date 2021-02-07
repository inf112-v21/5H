package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;

/**
 * Class for handling GUI and game setup/logic
 */
public class Game implements ApplicationListener {
    private SpriteBatch batch;
    private Camera camera;
    private boolean isFinished;
    private Board board;
    private int boardSize;
    private ArrayList<Player> playerList;
    private ArrayList<Flag> flagList;
    private int turn;
    private Player winner;
    private boolean pause;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        board = new Board(1);
        playerList = board.getPlayerList();
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
                //HashMap<String, Sprite> spriteMap = board.getSpriteMap();
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
            if(Gdx.input.isKeyPressed(Input.Keys.R)){ //Debug win mode
                dispose();
                create();
            }
            return;
        }
        Player playerTurn = playerList.get(turn-1);
        if(Gdx.input.isKeyPressed(Input.Keys.F)){ //Debug win mode
            playerTurn.addScore(3);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.R)){ //Debug restart
            dispose();
            create();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            playerTurn.setTexture(new Texture("src\\main\\tex\\"+playerTurn.getName()+"up.png"));
            if(playerTurn.move(0, 1, board)){
                return;
            }
            endTurn();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            playerTurn.setTexture(new Texture("src\\main\\tex\\" + playerTurn.getName() +"right.png"));
            if(playerTurn.move(1, 0, board)){
                return;
            }
            endTurn();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            playerTurn.setTexture(new Texture("src\\main\\tex\\" + playerTurn.getName() +"left.png"));
            if(playerTurn.move(-1, 0, board)){
                return;
            }
            endTurn();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            playerTurn.setTexture(new Texture("src\\main\\tex\\" + playerTurn.getName() + "down.png"));
            if(playerTurn.move(0, -1, board)){
                return;
            }
            endTurn();
        }
        if(playerTurn.getScore() >= 3){
            isFinished = true;
            winner = playerTurn;
        }

    }

    /**
     * Called after each move, adds a small pause between turns to prevent accidental moves.
     */
    private void endTurn(){
        System.out.println("Turn: " + turn + " finished");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        turn++;
        if(turn > 4){
            turn = 1;
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
