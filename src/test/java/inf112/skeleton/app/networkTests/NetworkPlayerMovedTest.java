package inf112.skeleton.app.networkTests;

import inf112.skeleton.app.net.PlayerMoved;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class NetworkPlayerMovedTest {

    private PlayerMoved playerMoved;
    private String shortName;
    private String move;

    @BeforeEach
    public void setUp(){
        playerMoved = new PlayerMoved();
        shortName = "p1";
        move = "NoMove";
    }

    @Test
    public void shortNameSetGetTest(){
        assertNotEquals(shortName,playerMoved.getShortName());
        playerMoved.setShortName(shortName);
        assertEquals(shortName, playerMoved.getShortName());
    }

    @Test
    public void moveSetGetTest(){
        assertNotEquals(move,playerMoved.getMove());
        playerMoved.setMove(move);
        assertEquals(move,playerMoved.getMove());
    }
}
