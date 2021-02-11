package inf112.skeleton.app;

import inf112.skeleton.app.Sprites.Direction;
import inf112.skeleton.app.Sprites.Flag;
import inf112.skeleton.app.Sprites.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FlagTest {
    private Board board;
    private Player player;
    private Flag flag;

    @BeforeEach
    public void setUp(){
        board = new Board();
        board.readBoard(1);
        player = board.getPlayerList().get(0);
        flag = board.getFlagList().get(0);
    }

    @Test
    public void flagRegistersVisitedPlayers(){
        assertTrue(flag.pickUp(player), "Player should pick up flag first time, but didn't");
        assertFalse(flag.pickUp(player), "Player shouldn't pick up flag the second time, but it did.");
    }

}
