package inf112.skeleton.app.Sprites;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import inf112.skeleton.app.Pair;

/**
 * Class that holds all the identical functions from board Sprites.
 */
public abstract class AbstractSprite extends Sprite implements ISprite {
    private String name;
    private String shortName;
    private Pair coordinates;

    public AbstractSprite(Texture tex){
        super(tex);
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
