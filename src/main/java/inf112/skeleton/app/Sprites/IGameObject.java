package inf112.skeleton.app.Sprites;

import inf112.skeleton.app.Pair;

/**
 * Interface for sprites
 */
public interface IGameObject {
    /**
     * @param x The x coordinate you want to update
     * @param y The y coordinate you want to update
     */
    void setCoordinates(int x, int y);

    /**
     * @return coordinates as a Pair
     */
    Pair getCoordinates();

    /**
     * @return Full name, for example "Player 1" for the first player
     */
    String getName();

    /**
     * @return Short name, for example "p1" for the first player
     */
    String getShortName();

    /**
     * @param sName Sets the item name to the String sName
     */
    void setShortName(String sName);

    /**
     * @param name Sets the name to the String name
     */
    void setName(String name);
}
