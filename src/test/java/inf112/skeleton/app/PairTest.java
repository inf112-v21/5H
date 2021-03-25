package inf112.skeleton.app;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class PairTest {
    private Pair pair;

    @Test
    public void pairGetsCreated(){
        assertNull(pair, "Pair should be null but was not.");
        pair = new Pair(1,1);
        assertNotNull(pair, "Pair should exist but was null");
    }

    @Test
    public void pairGetTest(){
        pair = new Pair(1,1);
        assertEquals(1, pair.getX(), "x value should be equal to the initialized value but was not.");
        assertEquals(1, pair.getY(), "y value should be equal to the initialized value but was not.");
    }

    @Test
    public void pairSetTest(){
        pair = new Pair(1,1);
        for(int x=2; x<20; x++){
            int y = x*x;
            pair.setX(x);
            pair.setY(y);
            assertEquals(x, pair.getX(), "x value should be equal to the set value but was not.");
            assertNotEquals(x, pair.getY(),"x value should not be equal to the set value it was." );
            assertEquals(y, pair.getY(), "y value should be equal to the set value but was not.");
            assertNotEquals(y, pair.getX(),"y value should not be equal to the set value it was." );
        }
    }
}
