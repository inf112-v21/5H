package inf112.skeleton.app.sprites;


public class ConveyorBelt extends AbstractGameObject {

    public static String texturePath = "src/main/resources/tex/conveyorbelt.png";

    private Direction dir;


    public ConveyorBelt(String texturePath, String dir) {
        super(texturePath);
        setName("ConveyorBelt");
        setShortName("cb" + dir);
        setDirection(dir);
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
                this.dir = Direction.NORTH;
                break;

            }
        }

    public Direction getDir() {
        return dir;
    }
}
