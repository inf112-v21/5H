package inf112.skeleton.app;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends Sprite {
    private Pair coordinates;
    private String name;
    private Texture texture;

    public Player(int x, int y, String name){
        coordinates = new Pair(x, y);
        this.name = name;
        texture = new Texture("src\\main\\tex\\"+name+".png");
        this.setTexture(texture);
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

    public Texture getTexture(){
        return texture;
    }
}
