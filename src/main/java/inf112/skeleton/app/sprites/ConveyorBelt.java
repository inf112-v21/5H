package inf112.skeleton.app.sprites;


public class ConveyorBelt extends AbstractGameObject {

    public static String texturePath;

    private final Direction dir;


    public ConveyorBelt(String texturePath, Direction dir) {
        super(texturePath);
        setName("ConveyorBelt");
        setShortName("cb");
        this.dir = dir;
    }

    public Direction getDir() {
        return dir;
    }
}
