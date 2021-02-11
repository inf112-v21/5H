package inf112.skeleton.app.Sprites;
import inf112.skeleton.app.Pair;

/**
 * Class that holds all the identical functions from board Sprites.
 */
public abstract class AbstractGameObject implements IGameObject {
    private String name;
    private String shortName;
    private final String texturePath;
    private Pair coordinates;

    public AbstractGameObject(String texturePath){
        this.texturePath = texturePath;
    }

    public void setCoordinates(int x, int y){
        coordinates = new Pair(x,y);
    }

    public Pair getCoordinates(){
        return coordinates;
    }

    public String getName(){
        return name;
    }

    public String getTexturePath(){
        return texturePath;
    }

    public String getShortName(){
        return shortName;
    }

    public void setShortName(String sName){
        shortName = sName;
    }

    public void setName(String name){
        this.name = name;
    }
}
