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
import java.util.HashMap;

/**
 * Class for handling GUI and game setup/logic
 * @author Skjalg
 */
public class Game implements ApplicationListener {
    private SpriteBatch batch;
    private Camera camera;
    private boolean isFinished;
    private Board board;
    private int boardSize;
    private ArrayList<Player> playerList;
    private int turn;
    private Player winner;
    private boolean pause;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        playerList = new ArrayList<>();
        for(int i=1; i<5;i++){
            Texture tex = new Texture("src\\main\\tex\\player"+i+".png");
            playerList.add(new Player(0,0, tex, i));
        }
        board = new Board(playerList);
        board.readBoard(1);
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
            Sprite text = new Sprite(new Texture("src\\main\\tex\\win.png"));
            text.setX(camera.viewportWidth/5);
            text.setY(camera.viewportHeight/5);
            text.draw(batch);
            winner.setX(camera.viewportWidth/2);
            winner.setY(camera.viewportHeight/3);
            winner.draw(batch);
            batch.end();
            return;
        }
        for(int x = 0; x < boardSize; x++){
            for(int y = 0; y < boardSize; y++){
                HashMap<String, Sprite> spriteMap = board.getSpriteMap();
                Sprite sprite = spriteMap.get(board.getPosition(x, y));
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
     * @author Skjalg
     */
    private void checkInput() {
        Player playerTurn = playerList.get(turn-1);
        if(Gdx.input.isKeyPressed(Input.Keys.F)){ //Debug win mode
            playerTurn.updateScore(3);
            if(!playerTurn.move(0, 1, board)){
                return;
            }
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.R)){ //Debug win mode
            dispose();
            create();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            playerTurn.setTexture(new Texture("src\\main\\tex\\"+playerTurn.getName()+"up.png"));
            if(!playerTurn.move(0,1,board)){
                return;
            }
            endTurn();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            playerTurn.setTexture(new Texture("src\\main\\tex\\" + playerTurn.getName() +"right.png"));
            if(!playerTurn.move(1,0,board)){
                return;
            }
            endTurn();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            playerTurn.setTexture(new Texture("src\\main\\tex\\" + playerTurn.getName() +"left.png"));
            if(!playerTurn.move(-1,0,board)){
                return;
            }
            endTurn();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            playerTurn.setTexture(new Texture("src\\main\\tex\\" + playerTurn.getName() + "down.png"));
            if(!playerTurn.move(0,-1,board)){
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
     * @author Skjalg
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
