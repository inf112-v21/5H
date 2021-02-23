package inf112.skeleton.app.sprites;

import inf112.skeleton.app.Pair;

import java.util.LinkedList;

public class Flag extends AbstractGameObject {
    private final LinkedList<Player> visitedBy; //Keeps track of what players have visited this flag
    private Pair coordinates;

    public Flag(int x, int y, String texturePath, int number){
        super(texturePath);
        setName("Flag"+number);
        setShortName("f"+number);
        coordinates = new Pair(x, y);
        visitedBy = new LinkedList<>();
    }

    /**
     * If a player has picked up a flag once he should not be able to pick up the same flag again, therefore
     * this functions returns true if the pickup was successful.
     * @param player Player standing on same coordinates as flag, trying to pick it up
     * @return true if player is allowed to pick up flag, false otherwise
     */
    public boolean pickUp(Player player){
        if(visitedBy.contains(player)){
            return false;
        }
        else{
            visitedBy.add(player);
            player.addScore(1);
            return true;
        }
    }
}
