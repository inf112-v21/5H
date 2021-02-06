package inf112.skeleton.app;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends Sprite {
    private Pair coordinates;
    private final String name;
    private final String shortName;
    private int points;
    private int hp;

    /**
     * @param x X spawn location
     * @param y Y spawn location
     * @param number the player number (i.e. 1 for player1, 2 for player2...)
     */
    public Player(int x, int y, Texture tex, int number){
        super(tex);
        shortName = "p"+number;
        coordinates = new Pair(x, y);
        this.name = "player"+number;
        points = 0;
        hp = 7;
    }

    public void setCoordinates(int x, int y){
        coordinates = new Pair(x, y);
    }

    public Pair getCoordinates(){
        return coordinates;
    }

    public String getName(){
        return name;
    }


    public boolean move(int x, int y, Board board) {
        int currentX = coordinates.getKey();
        int currentY = coordinates.getValue();
        int updatedX = currentX + x;
        int updatedY = currentY + y;
        if(updatedX > board.getSize()-1 || updatedX < 0){
            System.err.println("Out of bounds");
            return false;
        }
        else if(updatedY > board.getSize()-1 || updatedY < 0){
            System.err.println("Out of bounds");
            return false;
        }
        else if(board.info(updatedX, updatedY).equals("h")){
            System.err.println("HP LOST");
            damage();
            return false;
        }
        else if(board.info(updatedX, updatedY).equals("f")){
            updateScore(1);
            System.out.println("+1 point, " + points + " total.");
        }
        setCoordinates(updatedX, updatedY); //Ignore this warning, its inverted in Board so therefore in this class it looks wrong
        board.updateCoordinate(shortName, updatedY, updatedX);
        board.updateCoordinate("g", currentY, currentX); //Ignore this warning, its inverted in Board so therefore in this class it looks wrong
        return true;
    }

    public void damage(){
        hp --;
    }
    public int getHp(){
        return hp;
    }
    public void updateScore(int score){
        points += score;
    }
    public int getScore(){
        return points;
    }
}
