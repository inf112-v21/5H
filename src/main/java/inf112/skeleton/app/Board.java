package inf112.skeleton.app;

import com.badlogic.gdx.graphics.Texture;
import inf112.skeleton.app.Sprites.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class for the Board, updates the board and provides necessary info for Game.java
 */
public class Board {
    private final ArrayList<ArrayList<AbstractSprite>> boardInfo;
    private final ArrayList<ArrayList<AbstractSprite>> originalBoard;
    private final HashMap<String, AbstractSprite> spriteMap;
    private int playerNum;
    private int flagNum;
    private final ArrayList<Player> playerList;
    private final ArrayList<Flag> flagList;

    public Board(){
        //Only need one instance of each of these classes per map, rest of items initialized in readBoard()
        Ground ground = new Ground(new Texture("src\\main\\tex\\ground.png"));
        Hole hole = new Hole(new Texture("src\\main\\tex\\hole.png"));
        Wall wall = new Wall(new Texture("src\\main\\tex\\wall.png"));
        spriteMap = new HashMap<>();
        spriteMap.put("g", ground);
        spriteMap.put("h", hole);
        spriteMap.put("w", wall);

        playerList = new ArrayList<>(); //List of players
        flagList = new ArrayList<>(); //List of flags
        boardInfo = new ArrayList<>(); //List of list of objects on board
        originalBoard = new ArrayList<>(); //List for saving the original state of the board.
    }

    /**
     * Function for reading board layout from .txt, formats the data properly and puts it in field variable boardInfo.
     * This exists so creating new boards is as simple as editing a .txt. During runtime boardInfo will be edited as opposed to the
     * .txt to preserve the original board layout.
     * @param boardNum The board to play on, calling this with 1 will play Board1.txt, 2 -> Board2.txt and so forth.
     */
    public void readBoard(int boardNum) {
        try{
            File file = new File("src\\main\\boards\\Board"+boardNum+".txt");
            Scanner scanner = new Scanner(file);
            int k = 0;
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] items = line.split(" ");
                ArrayList<AbstractSprite> lineSprites = new ArrayList<>();
                for(int i=0; i<items.length; i++){
                    if(items[i].matches("p\\d+")) {
                        playerNum += 1;
                        Player player = new Player(k, i, new Texture("src\\main\\tex\\player" + playerNum + "up.png"), playerNum);
                        player.setBoard(this);
                        spriteMap.put(player.getShortName(), player);
                        playerList.add(player);
                        lineSprites.add(player);
                    }
                    else if(items[i].matches("f\\d+")) {
                        flagNum += 1;
                        int num = Integer.parseInt(items[i].substring(1));
                        Flag flag = new Flag(k, i, new Texture("src\\main\\tex\\flag" + num +".png"), num);
                        spriteMap.put(items[i], flag);
                        flagList.add(flag);
                        lineSprites.add(flag);
                    }
                    else{
                        lineSprites.add(spriteMap.get(items[i]));
                    }
                }
                k ++;
                boardInfo.add(lineSprites);
            }
            //Make a safe clone of board
            for( ArrayList<AbstractSprite> sublist : boardInfo) {
                originalBoard.add(new ArrayList<>(sublist));
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
     * @param sprite the short name of the sprite that should be placed in (x,y), example p1 => (AbstractSprite) Player1
     */
    public void updateCoordinate(String sprite, int x, int y){
        if(!spriteMap.containsKey(sprite)) {
            System.err.println("Illegal object");
            return;
        }
        AbstractSprite theSprite = spriteMap.get(sprite);
        boardInfo.get(x).set(y, theSprite);
    }

    public int getSize() {
        return 12;
    }

    public HashMap<String, AbstractSprite> getSpriteMap() {
        return spriteMap;
    }

    public AbstractSprite getPosition(int x, int y) {
        return boardInfo.get(x).get(y);
    }

    public AbstractSprite getOriginalPosition(int x, int y) {
        return originalBoard.get(x).get(y);
    }

    public AbstractSprite info(int updatedX, int updatedY) {
        return boardInfo.get(updatedX).get(updatedY);
    }

    public ArrayList<Player> getPlayerList(){
        return playerList;
    }

    public ArrayList<Flag> getFlagList(){
        return flagList;
    }

}
