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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class for drawing the board to screen
 *
 * @author Skjalg
 */
public class Board implements ApplicationListener {
    private final int boardSize = 12;
    private SpriteBatch batch;
    private Sprite player;
    private Camera camera;
    private String[][] boardInfo;
    private HashMap<String, Sprite> spriteMap;
    private HashMap<Sprite, Pair> playerMap;

    @Override
    public void create() {
        batch = new SpriteBatch();
        Sprite floor = new Sprite(new Texture("src\\main\\tex\\floor.png"));
        Sprite hole = new Sprite(new Texture("src\\main\\tex\\hole.png"));
        player = new Sprite(new Texture("src\\main\\tex\\player1.png"));
        playerMap = new HashMap<>();
        spriteMap = new HashMap<>();
        spriteMap.put("f", floor);
        spriteMap.put("h", hole);
        spriteMap.put("p", player);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        boardInfo = new String[boardSize][boardSize];
        getBoard(1);
    }

    /**
     * Function for reading board layout from .txt, formats the data properly and puts it in field variable boardInfo.
     * This exists so creating new boards is as simple as editing a .txt. During runtime boardInfo will be edited as opposed to the
     * .txt to preserve the original board layout.
     * @param boardNum The board to play on, calling this with 1 will play Board1.txt, 2 -> Board2.txt and so forth.
     *
     * @author Skjalg
     */
    private void getBoard(int boardNum) {
       try{
           File file = new File("src\\main\\boards\\Board"+boardNum+".txt");
           Scanner scanner = new Scanner(file);
           int k = 0;
           while(scanner.hasNextLine()){
               String line = scanner.nextLine();
               String[] items = line.split(" ");
               for(int i=0; i<items.length; i++){
                   if(items[i].equals("p")){
                       playerMap.put(player, new Pair(i, k));
                   }
               }
               boardInfo[k] = items;
               k ++;
           }
           scanner.close();
       }
       catch(FileNotFoundException e){
           System.out.println("Board does not exist");
       }
    }

    /**
     * Function for updating the grid at position (x,y).
     * Note that x will be horizontal and y vertical for function calls, however in the code
     * they will be used opposite due to the boardInfo[][] layout.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param sprite the object that should be placed in (x,y)
     */
    private void updateCoordinate(String sprite, int x, int y){
        if(!spriteMap.containsKey(sprite)) {
            System.err.println("Illegal object");
            return;
        }
        boardInfo[y][x] = sprite;
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
        for(int x = 0; x < boardSize; x++){
            for(int y = 0; y < boardSize; y++){
                Sprite sprite = spriteMap.get(boardInfo[x][y]);
                sprite.setSize(camera.viewportWidth/boardSize, camera.viewportWidth/boardSize);
                sprite.setX(x*(camera.viewportWidth/boardSize));
                sprite.setY(y*(camera.viewportHeight/boardSize));
                sprite.draw(batch);
            }
        }
        batch.end();
    }

    private void checkInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            player.setTexture(new Texture("src\\main\\tex\\player1up.png"));
            move(player, 0, 1);
            pause();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            player.setTexture(new Texture("src\\main\\tex\\player1right.png"));
            move(player, 1, 0);
            pause();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            player.setTexture(new Texture("src\\main\\tex\\player1left.png"));
            move(player, -1, 0);
            pause();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            player.setTexture(new Texture("src\\main\\tex\\player1down.png"));
            move(player, 0, -1);
            pause();
        }
    }

    private void move(Sprite player, int x, int y) {
        int currentX = playerMap.get(player).getKey();
        int currentY = playerMap.get(player).getValue();
        int updatedX = currentX + y;
        int updatedY = currentY + x;
        if(updatedX > boardSize-1 || updatedX < 0){
            System.err.println("Out of bounds");
            return;
        }
        else if(updatedY > boardSize-1 || updatedY < 0){
            System.err.println("Out of bounds");
            return;
        }
        playerMap.put(player, new Pair(updatedX, updatedY));
        updateCoordinate("p", updatedX, updatedY);
        updateCoordinate("f", currentX, currentY);
    }

    @Override
    public void pause() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
