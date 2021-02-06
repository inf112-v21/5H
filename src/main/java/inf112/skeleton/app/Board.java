package inf112.skeleton.app;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class for the Board, updates the board and provides necessary info for Game.java
 */
public class Board {
    private final String[][] boardInfo;
    private final HashMap<String, Sprite> spriteMap;
    private final int boardSize = 12;
    private final ArrayList<Player> playerList;

    public Board(ArrayList<Player> playerList){
        Sprite ground = new Sprite(new Texture("src\\main\\tex\\ground.png"));
        Sprite hole = new Sprite(new Texture("src\\main\\tex\\hole.png"));
        Sprite flag = new Sprite(new Texture("src\\main\\tex\\flag.png"));
        spriteMap = new HashMap<>();
        spriteMap.put("g", ground);
        spriteMap.put("h", hole);
        spriteMap.put("f", flag);
        spriteMap.put("p1", playerList.get(0));
        spriteMap.put("p2", playerList.get(1));
        spriteMap.put("p3", playerList.get(2));
        spriteMap.put("p4", playerList.get(3));
        this.playerList = playerList;
        boardInfo = new String[boardSize][boardSize];
    }

    /**
     * Function for reading board layout from .txt, formats the data properly and puts it in field variable boardInfo.
     * This exists so creating new boards is as simple as editing a .txt. During runtime boardInfo will be edited as opposed to the
     * .txt to preserve the original board layout.
     * @param boardNum The board to play on, calling this with 1 will play Board1.txt, 2 -> Board2.txt and so forth.
     *
     * @author Skjalg
     */
    public void readBoard(int boardNum) {
        try{
            File file = new File("src\\main\\boards\\Board"+boardNum+".txt");
            Scanner scanner = new Scanner(file);
            int k = 0;
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] items = line.split(" ");
                for(int i=0; i<items.length; i++){
                    if(items[i].equals("p1")){
                        playerList.get(0).setCoordinates(k, i);
                    }
                    if(items[i].equals("p2")){
                        playerList.get(1).setCoordinates(k, i);
                    }
                    if(items[i].equals("p3")){
                        playerList.get(2).setCoordinates(k, i);
                    }
                    if(items[i].equals("p4")){
                        playerList.get(3).setCoordinates(k, i);
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
    public void updateCoordinate(String sprite, int x, int y){
        if(!spriteMap.containsKey(sprite)) {
            System.err.println("Illegal object");
            return;
        }
        boardInfo[y][x] = sprite;
    }

    public int getSize() {
        return boardSize;
    }

    public HashMap<String, Sprite> getSpriteMap() {
        return spriteMap;
    }

    public String getPosition(int x, int y) {
        return boardInfo[x][y];
    }

    public Object info(int updatedX, int updatedY) {
        return boardInfo[updatedX][updatedY];
    }

}
