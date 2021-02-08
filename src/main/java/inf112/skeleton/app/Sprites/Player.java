package inf112.skeleton.app.Sprites;

import com.badlogic.gdx.graphics.Texture;
import inf112.skeleton.app.Board;

public class Player extends AbstractSprite {
    private int points;
    private int hp;

    /**
     * @param x X spawn location
     * @param y Y spawn location
     * @param number the player number (i.e. 1 for player1, 2 for player2...)
     */
    public Player(int x, int y, Texture tex, int number){
        super(tex);
        setShortName("p"+number);
        setCoordinates(x,y);
        setName("Player"+number);
        points = 0;
        hp = 7;
    }

    /**
     * Function for moving a player on the board.
     * @param x Number of units to move in x direction
     * @param y Number of units to move in y direction
     * @param board the active board
     * @return true if move is valid, false otherwise.
     */
    public boolean move(int x, int y, Board board) {
        //Coordinates for current location of player
        int currentX = getCoordinates().getX();
        int currentY = getCoordinates().getY();
        //Coordinates where player wants to move
        int updatedX = currentX + x;
        int updatedY = currentY + y;
        if(updatedX > board.getSize()-1 || updatedX < 0){
            System.err.println("Out of bounds");
            return true;
        }
        else if(updatedY > board.getSize()-1 || updatedY < 0){
            System.err.println("Out of bounds");
            return true;
        }
        else if(board.info(updatedX, updatedY).getName().equals("Hole")){
            System.err.println("HP LOST");
            damage();
            if(hp == 0){
                System.err.println(getName() + " is dead");//placeholder
            }
            return true;
        }
        else if(board.info(updatedX, updatedY).getName().equals("Wall")){
            System.out.println("Hit a wall.");
            return true;
        }
        else if(board.info(updatedX, updatedY).getName().matches("Flag\\d+")){
            Flag flag = (Flag) board.getPosition(updatedX, updatedY);
            if(flag.pickUp(this)){
                addScore(1);
                System.out.println("+1 point, " + points + " total.");
            }
            else{
                System.out.println("Flag already picked up!");
            }
        }
        setCoordinates(updatedX, updatedY);
        board.updateCoordinate(getShortName(), updatedX, updatedY); //Ignore this warning, its inverted in Board so therefore in this class it looks wrong
        for(Flag flag : board.getFlagList()){
            if(flag.getCoordinates().getX() == currentX && flag.getCoordinates().getY() == currentY){
                board.updateCoordinate(flag.getShortName(), currentX, currentY);
                return false;
            }
        }
        board.updateCoordinate("g", currentX, currentY);
        return false;
    }

    /**
     * When a player is hit by laser or falls in hole, use this function to deal damage.
     */
    public void damage(){
        hp --;
    }

    /**
     * @return Player's hp
     */
    public int getHp(){
        return hp;
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
}
