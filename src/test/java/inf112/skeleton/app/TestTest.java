package inf112.skeleton.app;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import inf112.skeleton.app.Sprites.Player;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestTest {
    @Test
    public void test(){
        //worksIfThisIsCalled();
        Board board = new Board();
        board.readBoard(1);
        Player player = board.getPlayerList().get(0); //Just get a player
        int startHp = player.getHp();
        player.die();
        assertNotEquals(startHp, player.getHp());
    }

    /**
     * If this is called, that is if you create a Lwjgl3Application, test() will work as expected
     * If you dont call this then you will get nullpointerexception in Texture.java.
     *
     * The problem is that this opens a window that needs closing for the test to finish
     */
    public void worksIfThisIsCalled(){
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        Game game = new Game();
        new Lwjgl3Application(game, cfg);
    }
}
