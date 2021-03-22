package inf112.skeleton.app;

import inf112.skeleton.app.net.NetworkSettings;
import inf112.skeleton.app.sprites.Direction;
import inf112.skeleton.app.sprites.Player;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class CollisionsTest {
    private Board board;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Game game;
    HashMap<Direction, Pair> dirMap;


    public void setUpBoard(int boardNum) {
        NetworkSettings networkSettings = new NetworkSettings("server", "localhost", 2, 2);
        game = new Game(networkSettings, 4);
        board = new Board();
        board.readBoard(boardNum);
        game.board = board;
        game.setBoardSize(board.getSize());
        player1 = board.getPlayerList().get(0);
        player2 = board.getPlayerList().get(1);
        player3 = board.getPlayerList().get(2);
        player4 = board.getPlayerList().get(3);
        dirMap = game.getDirMap();
    }

    @Test
    public void player1StoppedByWall() { //Note this tests that the collision method stops wallcollision, a regular move handles this as well.
        setUpBoard(110);
        //Direction dir = player1.getDirection();
        //Pair pair = dirMap.get(dir);
        //player1.move(pair.getX(), pair.getY());
        assertFalse(game.collision(player1));
    }

    @Test
    public void player2PushesPlayer3() {
        setUpBoard(110);
        Direction dir = player2.getDirection();
        Pair pair = dirMap.get(dir);
        Pair player3ExpectedCoordinate = player3.getCoordinates();
        assertTrue(game.collision(player2), "Player2 was not allowed to move");
        Pair player3EndCoordinate = player3.getCoordinates();
        //Modifying the start coordinate to be what we expect
        player3ExpectedCoordinate.setX(player3ExpectedCoordinate.getX() + pair.getX()); //Using player 2s direction as it is player2 that decides where you are pushed.
        player3ExpectedCoordinate.setY(player3ExpectedCoordinate.getY() + pair.getY());
        assertEquals(player3ExpectedCoordinate, player3EndCoordinate, "Player 3 didn't move (where expected or at all)");
        player2.move(pair.getX(), pair.getY()); // We can now move as the tile is unoccupied.
        Pair player2EndCoordinate = player2.getCoordinates();
        assertEquals(player3ExpectedCoordinate, player2EndCoordinate, "Player 2 didn't move into player 3s tile");

    }

}