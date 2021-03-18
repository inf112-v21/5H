package inf112.skeleton.app.sprites;

import inf112.skeleton.app.Pair;

public class Laser extends AbstractGameObject{
    
    private Direction direction;
    private Pair coordinates;
    
    public Laser (String texturePath){
        super(texturePath);
        setName("Laser");
        setShortName("l");

    }


    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
