package inf112.skeleton.app.sprites;

import inf112.skeleton.app.Pair;

public class Laser extends AbstractGameObject{
    
    private Direction direction;
    private Pair coordinates;
    private int number;
    static String texturePath = "src/main/resources/tex/laser1.png";
    
    public Laser (int x, int y, String texturePath, int number){ // For creating board lasers
        super(texturePath);
        setName("Laser"+number);
        setShortName("l"+number);
        this.direction = Direction.WEST; //Testvvalue
        this.number = number;
        coordinates = new Pair(x,y);
    }
    public Laser (int x, int y, Direction dir, String playerShortName){ // For creating player lasers
        super(Laser.texturePath);
        setName("Laser"+playerShortName);
        setShortName("l"+playerShortName);
        this.direction = dir;
        coordinates = new Pair(x,y);
    }



    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Pair getCoordinates() {
        return coordinates;
    }

    public int getNumber() {
        return number;
    }
}
