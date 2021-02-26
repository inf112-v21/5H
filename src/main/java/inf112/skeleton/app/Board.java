package inf112.skeleton.app;

import inf112.skeleton.app.sprites.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class for the Board, updates the board and provides necessary info for Game.java
 */
public class Board {
    private final ArrayList<ArrayList<AbstractGameObject>> boardInfo;
    private final ArrayList<ArrayList<AbstractGameObject>> originalBoard;
    private final HashMap<String, AbstractGameObject> objectMap;    //Map for getting the Game Object from a string, i.e. p1 => (Player) player1
    private int playerNum;
    private int flagNum;
    private final ArrayList<Player> playerList;
    private final ArrayList<Flag> flagList;

    public Board(){
        //Only need one instance of each of these classes per map, rest of items initialized in readBoard()
        Ground ground = new Ground("src\\main\\resources\\tex\\ground.png");
        Hole hole = new Hole("src\\main\\resources\\tex\\hole.png");
        Wall wall = new Wall("src\\main\\resources\\tex\\wall.png");
        objectMap = new HashMap<>();
        objectMap.put("g", ground);
        objectMap.put("h", hole);
        objectMap.put("w", wall);

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
            File file = new File("src\\main\\resources\\boards\\Board"+boardNum+".txt");
            Scanner scanner = new Scanner(file);
            int k = 0;
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] items = line.split(" ");
                ArrayList<AbstractGameObject> lineSprites = new ArrayList<>();
                for(int i=0; i<items.length; i++){
                    if(items[i].matches("p\\d+")) {
                        playerNum += 1;
                        Player player = new Player(k, i, "src\\main\\resources\\tex\\player" + playerNum + ".png", playerNum);
                        player.setBoard(this);
                        objectMap.put(player.getShortName(), player);
                        playerList.add(player);
                        lineSprites.add(player);
                    }
                    else if(items[i].matches("f\\d+")) {
                        flagNum += 1;
                        int num = Integer.parseInt(items[i].substring(1));
                        Flag flag = new Flag(k, i, "src\\main\\resources\\tex\\flag" + num +".png", num);
                        objectMap.put(items[i], flag);
                        flagList.add(flag);
                        lineSprites.add(flag);
                    }
                    else{
                        lineSprites.add(objectMap.get(items[i]));
                    }
                }
                k ++;
                boardInfo.add(lineSprites);
            }
            //Make a safe clone of board
            for( ArrayList<AbstractGameObject> sublist : boardInfo) {
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
     * @param object the short name of the sprite that should be placed in (x,y), example p1 => (AbstractSprite) Player1
     */
    public void updateCoordinate(String object, int x, int y){
        if(!objectMap.containsKey(object)) {
            System.err.println("Illegal object");
            return;
        }
        AbstractGameObject theObject = objectMap.get(object);
        boardInfo.get(x).set(y, theObject);
    }

    /**
     * @return size of board as int
     */
    public int getSize() {
        return 12;
    }

    /**
     * @return the objectMap, that is to say the map from shortName to Object. p1 => (Player) player1
     */
    public HashMap<String, AbstractGameObject> getObjectMap() {
        return objectMap;
    }

    /**
     * @param x coordinate
     * @param y coordinate
     * @return The object at coordinate (x,y)
     */
    public AbstractGameObject getPosition(int x, int y) {
        return boardInfo.get(x).get(y);
    }

    /**
     * @param x coordinate
     * @param y coordinate
     * @return The object that initially was placed at (x,y)
     */
    public AbstractGameObject getOriginalPosition(int x, int y) {
        return originalBoard.get(x).get(y);
    }

    /**
     * @return list of players
     */
    public ArrayList<Player> getPlayerList(){
        return playerList;
    }

    /**
     * @return list of flags
     */
    public ArrayList<Flag> getFlagList(){
        return flagList;
    }

}
