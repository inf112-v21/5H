package inf112.skeleton.app;

import inf112.skeleton.app.Sprites.Direction;
import inf112.skeleton.app.Sprites.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Board board;
    private Player player;

    @BeforeEach
    public void setUp(){
        board = new Board();
        board.readBoard(1);
        player = board.getPlayerList().get(0);
    }

    @Test
    public void playerLosesHpTest(){
        int hp = player.getHp();
        player.die();
        int newHp = player.getHp();
        assertNotEquals(hp,newHp);
    }

    @Test
    public void playerTakesOneDamageTest(){
        int pc = player.getPc();
        player.damage();
        int newPc = player.getPc();
        assertNotEquals(pc,newPc);
    }

    @Test
    public void playerLosesHpIfPcIs0Test(){
        int hp = player.getHp();

        for(int i = 0; i<10; i++){
            player.damage();
        }

        int newHp = player.getHp();
        assertNotEquals(hp,newHp);
    }

    @Test
    public void playerScoreIncreasesTest(){
        int score = player.getScore();
        player.addScore(1);
        int newScore = player.getScore();
        assertNotEquals(score,newScore);

    }

    @Test
    public void playerSetSavePointTest(){
        int x = 1;
        int y = 2;
        player.setSavepoint(x,y);
        Pair p = player.getSavepoint();
        int newX = p.getX();
        int newY = p.getY();
        assertEquals(x,newX);
        assertEquals(y,newY);
    }
    @Test
    public void playerSetDirectionTest(){
        player.setDirection(Direction.NORTH);
        Direction dir = player.getDirection();
        player.setDirection(Direction.EAST);
        Direction newDir = player.getDirection();
        assertNotEquals(dir,newDir);
    }

    @Test
    public void playerIsDeadTest(){
        assertFalse(player.isDead());
        player.die();
        player.die();
        player.die();
        assertTrue(player.isDead());
    }

    @Test
    public void playerLosesHpIfOOB(){
        int hp = player.getHp();
        player.setCoordinates(0,0);
        player.move(-1,0);
        assertNotEquals(hp, player.getHp());
    }

    @Test
    public void playerLosesHpIfMovesInHole(){
        board.updateCoordinate("h", 1,1);
        player.setCoordinates(1,0);
        int hp = player.getHp();
        player.move(0,1);
        assertNotEquals(hp, player.getHp());
    }

    @Test
    public void playerDoesNotMoveIfWall(){
        board.updateCoordinate("w", 1,1);
        player.setCoordinates(1,0);
        Pair pos = player.getCoordinates();
        player.move(0,1);
        assertEquals(pos, player.getCoordinates(), "Expected position not to change but it did.");
    }

    @Test
    public void playerScoreIncreasesWhenItGoesOnAFlagTest(){
        board.updateCoordinate("f1", 1,1);
        player.setCoordinates(1,0);
        int score = player.getScore();
        player.move(0,1);
        assertNotEquals(score, player.getScore());
    }
}
