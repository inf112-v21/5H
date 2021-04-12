package inf112.skeleton.app.sprites;

public class Gear extends AbstractGameObject {

    public static String texturePath;

    public boolean isClockwise() {
        return clockwise;
    }

    private boolean clockwise;


    public Gear(String direction) {
        super(texturePath);
        setName("Gear");
        if (direction.equals("left")) {
            setShortName("gl");
            clockwise = false;

        }
        else if (direction.equals("right")) {
            setShortName("gr");
            clockwise  = true;
        }

    }


}
