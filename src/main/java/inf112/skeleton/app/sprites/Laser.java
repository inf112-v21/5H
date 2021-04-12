package inf112.skeleton.app.sprites;

import inf112.skeleton.app.Pair;

public class Laser extends AbstractGameObject{
    
    private Direction direction;
    private final Pair coordinates;
    static String texturePath = "src/main/resources/tex/laser1.png";
    
    public Laser (int x, int y, String texturePath, String dir){ // For creating board lasers
        super(texturePath);
        setName("Laser"+dir);
        setShortName("l"+dir);
        setDirection(dir);
        coordinates = new Pair(x,y);
    }
    public Laser (int x, int y, Direction dir, String playerShortName){ // For creating player lasers
        super(Laser.texturePath);
        setName("Laser"+playerShortName);
        setShortName("l"+playerShortName);
        this.direction = dir;
        coordinates = new Pair(x,y);
    }

    private void setDirection(String dir) {
        switch (dir) {
            case "n":
                this.direction = Direction.NORTH;
                break;
            case "e":
                this.direction = Direction.EAST;
                break;
            case "s":
                this.direction = Direction.SOUTH;
                break;
            case "w":
                this.direction = Direction.WEST;
                break;
            default:
                this.direction = Direction.NORTH; // temp

        }
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

}
