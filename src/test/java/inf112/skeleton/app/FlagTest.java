package inf112.skeleton.app;

import inf112.skeleton.app.sprites.Flag;
import inf112.skeleton.app.sprites.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FlagTest {
    private Player player;
    private Flag firstFlag;
    private Flag secondFlag;
    private Flag thirdFlag;

    @BeforeEach
    public void setUp(){
        Board board = new Board();
        board.readBoard(1);
        player = board.getPlayerList().get(0);
        // There are always exactly three flags so accessing them with indexes should be fine, note they are in the wrong order in the list
        firstFlag = board.getFlagList().get(2);
        secondFlag = board.getFlagList().get(1);
        thirdFlag = board.getFlagList().get(0);

    }

    @Test
    public void flagRegistersVisitedPlayers(){
        assertTrue(firstFlag.pickUp(player), "Player should pick up flag first time, but didn't");
        assertFalse(firstFlag.pickUp(player), "Player shouldn't pick up flag the second time, but it did.");
    }
    @Test
    public void canPickupFlagsInOrder() {
        boolean flagsInOrder;
        firstFlag.pickUp(player);
        secondFlag.pickUp(player);
        flagsInOrder = thirdFlag.pickUp(player);
        assertTrue(flagsInOrder, "Player could not pickup the flags in order");
    }

    @Test
    public void canNotPickupFlagsOutOfOrder() {
        boolean flagsOutOfOrder;
        firstFlag.pickUp(player);
        flagsOutOfOrder = thirdFlag.pickUp(player);
        assertFalse(flagsOutOfOrder, "Player could pickup flags out of order, but shouldn't be able to");
    }

    @Test
    public void canPickupFlagAfterFailingAPickup() {
        boolean pickUp;
        secondFlag.pickUp(player);
        firstFlag.pickUp(player);
        pickUp = secondFlag.pickUp(player);
        assertTrue(pickUp, "Could not pickup flags after failing picking it up earlier");
    }

}
