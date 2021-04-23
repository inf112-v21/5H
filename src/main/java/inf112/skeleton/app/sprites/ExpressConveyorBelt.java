package inf112.skeleton.app.sprites;


public class ExpressConveyorBelt extends AbstractGameObject {

    public static String texturePath = "src/main/resources/tex/expressconveyorbelt.png";
    private Direction dir;


    public ExpressConveyorBelt(String texturePath, String dir) {
        super(texturePath);
        setName("ExpressConveyorBelt" + dir);
        setShortName("eb"+dir);
        setDirection(dir);
    }

    public Direction getDir() {
        return dir;
    }

    private void setDirection(String dir) {
        switch (dir) {
            case "n":
                this.dir = Direction.NORTH;
                break;
            case "e":
                this.dir = Direction.EAST;
                break;
            case "s":
                this.dir = Direction.SOUTH;
                break;
            case "w":
                this.dir = Direction.WEST;
                break;
            default:
                this.dir = Direction.NORTH; // temp
                break;

        }
    }
}
