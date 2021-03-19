package inf112.skeleton.app.sprites;

import inf112.skeleton.app.Pair;

import java.util.LinkedList;

public class Flag extends AbstractGameObject {
    private final LinkedList<Player> visitedBy; //Keeps track of what players have visited this flag
    private Pair coordinates;
    private int number;

    public Flag(int x, int y, String texturePath, int number){
        super(texturePath);
        setName("Flag"+number);
        setShortName("f"+number);
        coordinates = new Pair(x, y);
        visitedBy = new LinkedList<>();
        this.number = number;
        System.out.println(number);
    }

    /**
     * If a player has picked up a flag once he should not be able to pick up the same flag again. Furthermore
     * the player should not be able to pick up flag 2 before flag 1, therefore
     * this functions returns true if the pickup was successful.
     * @param player Player standing on same coordinates as flag, trying to pick it up
     * @return true if player is allowed to pick up flag, false otherwise
     */
    public boolean pickUp(Player player){
        if(visitedBy.contains(player)){
            return false;
        }
        if(number == 1){
            visitedBy.add(player);
            player.addFlag(getShortName());
            return true;
        }
        else if(player.getVisitedFlags().contains("f"+(number-1))){
            visitedBy.add(player);
            player.addFlag(getShortName());
            return true;
        }
        else{
            return false;
        }
    }
}
