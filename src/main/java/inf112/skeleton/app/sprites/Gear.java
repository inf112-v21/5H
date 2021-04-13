package inf112.skeleton.app.sprites;

public class Gear extends AbstractGameObject {

    public static String texturePath = "src/main/resources/tex/gear";

    private boolean clockwise;

    public boolean isClockwise() {
        return clockwise;
    }



    public Gear(String direction) {
        super(texturePath + direction + ".png");
        setName("Gear");
        if (direction.equals("l")) {
            setShortName("ol");
            clockwise = false;
        }
        else if (direction.equals("r")) {
            setShortName("or");
            clockwise  = true;
        }
    }


}
