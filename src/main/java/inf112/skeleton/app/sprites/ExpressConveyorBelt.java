package inf112.skeleton.app.sprites;


public class ExpressConveyorBelt extends AbstractGameObject {

    public static String texturePath;

    private final Direction dir;


    public ExpressConveyorBelt(String texturePath, Direction dir) {
        super(texturePath);
        setName("ExpressConveyorBelt");
        setShortName("ecb");
        this.dir = dir;
    }

    public Direction getDir() {
        return dir;
    }
}
