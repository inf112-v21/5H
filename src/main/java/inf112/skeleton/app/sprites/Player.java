package inf112.skeleton.app.sprites;

import inf112.skeleton.app.Board;
import inf112.skeleton.app.Pair;

import java.util.ArrayList;

public class Player extends AbstractGameObject {
    private int points;
    private int hp;
    private int pc;
    private final Pair savePoint;   //Last registered savepoint coordinates
    private Board board;
    private boolean dead;
    private Direction dir;  //Facing direction
    private int playerNum; //The number for this player (1 for player1, 2 for player2...)
    private ArrayList<String> visitedFlags; //List over flags (as shortname) that player has visited



    /**
     * @param x X spawn location
     * @param y Y spawn location
     * @param number the player number (i.e. 1 for player1, 2 for player2...)
     */
    public Player(int x, int y, String texturePath, int number){
        super(texturePath);
        setShortName("p"+number);
        setCoordinates(x,y);
        setName("Player "+number);
        setPlayerNum(number);
        points = 0;
        hp = 3;
        pc = 9;
        dead = false;
        savePoint = new Pair(x, y); //Initialize save point
        dir = Direction.NORTH; //Set direction on spawn
        visitedFlags = new ArrayList<>();
        setPlayerNum(number);
    }

    public void setBoard(Board board){
        this.board = board;
    }

    /**
     * Function for moving a player on the board.
     * @param x Number of units to move in x direction
     * @param y Number of units to move in y direction
     */
    public void move(int x, int y) {
        //Coordinates for current location of player
        int currentX = getCoordinates().getX();
        int currentY = getCoordinates().getY();
        //Coordinates where player wants to move
        int updatedX = currentX + x;
        int updatedY = currentY + y;
        if(updatedX > board.getSize()-1 || updatedX < 0){
            resetTile(currentX, currentY);
            die();
            System.out.println("X out of bounds");
            return;
        }
        else if(updatedY > board.getSize()-1 || updatedY < 0){
            resetTile(currentX, currentY);
            die();
            System.out.println("Y out of bounds");
            return;
        }
        else if(board.getPosition(updatedX, updatedY).getName().equals("Hole")){
            setCoordinates(updatedX, updatedY);
            resetTile(currentX, currentY);
            die();
            System.out.println("Fell into hole");
            return;
        }
        else if(board.getPosition(updatedX, updatedY).getName().equals("Wall")){
            System.out.println("Hit a wall.");
            return;
        }
        else if(board.getPosition(updatedX, updatedY).getName().matches("Flag\\d+")){
            Flag flag = (Flag) board.getPosition(updatedX, updatedY);
            if(flag.pickUp(this)){
                addScore(1);
                System.out.println("+1 point, " + points + " total.");
            }
            else{
                System.out.println("Flag already picked up! / Previous flag not picked up");
            }
        }
        setCoordinates(updatedX, updatedY);
        board.updateCoordinate(getShortName(), updatedX, updatedY);
        resetTile(currentX, currentY);
    }

    /**
     * Resets the tile in position (x,y) to its original state
     * @param x coordinate
     * @param y coordinate
     * @return false (why?)
     */
    private boolean resetTile(int x, int y) {
        AbstractGameObject tile = board.getOriginalPosition(x,y);
        if(tile.getShortName().matches("p\\d+")){
            board.updateCoordinate("g", x, y);
            return false;
        }
        board.updateCoordinate(tile.getShortName(), x, y);
        return false;
    }

    /**
     * When a player is hit by laser, use this function to deal damage.
     */
    public void damage() {
        if (pc == 0) {
            die();
        }
        else{
            pc --;
        }
    }

    /**
     * Updates player hp, resets pc, and moves player to save point.
     * Called when going OOB or falling in hole or when PC reaches 0.
     */
    public void die() {
        hp --;
        if(hp == 0){
            //If player is out of hp it makes sure the player is not spawned again.
            resetTile(getCoordinates().getX(), getCoordinates().getY());
            dead = true;
            return;
        }
        resetPosition();
        pc = 9;
        System.out.println(getName() + " died and now has: " + hp + " HP");
    }

    /**
     * Sets the played back to the last save point, called when player dies.
     */
    private void resetPosition() {
        resetTile(getCoordinates().getX(), getCoordinates().getY());
        setCoordinates(savePoint.getX(), savePoint.getY());
        board.updateCoordinate(getShortName(), savePoint.getX(), savePoint.getY());
    }

    /**
     * @return Player's hp
     */
    public int getHp(){
        return hp;
    }

    /**
     * @return Player's pc - pc being the amount of program cards the person can use
     */
    public int getPc(){
        return pc;
    }

    /**
     * Function for updating the score of the player.
     * @param points Amount of points to add to score
     */
    public void addScore(int points){
        this.points += points;
    }


    /**
     * @return Player's score
     */
    public int getScore(){
        return points;
    }

    /**
     *  Set player's savepoint
     */
    public void setSavepoint(int x, int y){
        savePoint.setX(x);
        savePoint.setY(y);
    }

    /**
     *  Set player's savepoint
     */
    public Pair getSavepoint(){
        return savePoint;
    }

    /**
     * @return the direction the player is facing
     */
    public Direction getDirection(){
        return dir;
    }

    /**
     * @param newDir the direction player should now be facing
     */
    public void setDirection(Direction newDir){
        dir = newDir;
    }

    public boolean isDead(){
        return dead;
    }

    public void setPlayerNum(int num){
        playerNum = num;
    }
    public int getPlayerNum(){
        return playerNum;
    }

    public ArrayList<String> getVisitedFlags() {
        return visitedFlags;
    }

    public void addFlag(String shortName){
        visitedFlags.add(shortName);
    }
}
