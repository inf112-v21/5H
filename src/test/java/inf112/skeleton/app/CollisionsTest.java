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
    public void player1StoppedByWall() { //Note this tests that the collision method stops wall collision, a regular move handles this as well.
        setUpBoard(110);
        assertFalse(game.collision(player1));
    }

    @Test
    public void player2PushesPlayer3() {
        setUpBoard(110);
        Direction dir = player2.getDirection();
        Pair pair = dirMap.get(dir);
        //Retrieving a copy of Player 3s coordinates
        Pair player3StartCoordinates = player3.getCoordinates().getCopy();
        System.out.println(player3StartCoordinates);
        Pair player3ExpectedCoordinate = player3.getCoordinates().getCopy();
        assertTrue(game.collision(player2), "Player2 was not allowed to move"); //If this passes the method call should move player3
        Pair player3EndCoordinate = player3.getCoordinates();
        //Modifying the start coordinate to be what we expect
        player3ExpectedCoordinate.setX(player3ExpectedCoordinate.getX() + pair.getX()); //Using player 2s direction as it is player2 that decides where you are pushed.
        player3ExpectedCoordinate.setY(player3ExpectedCoordinate.getY() + pair.getY());
        assertEquals(player3ExpectedCoordinate, player3EndCoordinate, "Player 3 didn't move (where expected or at all)");
        player2.move(pair.getX(), pair.getY()); // We can now move as the tile is unoccupied.
        Pair player2EndCoordinate = player2.getCoordinates();
        assertEquals(player3StartCoordinates, player2EndCoordinate, "Player 2 didn't move into player 3s tile");

    }

    @Test
    public void player4AllowedToMoveOffBoard() { //Collision should allow players to move off the board so that players can push off other players
        setUpBoard(110);
        // Checks that collision return true when trying to move off the board
        assertTrue(game.collision(player4), "Player 4 was not allowed to move off the board");
    }

    @Test
    public void Player1CanPushPlayer2WhoPushesPlayer3() {
        setUpBoard(111);
    }

    @Test
    public void Player1CanPushPlayer2OffTheBoardAndKillThem() {
        setUpBoard(112);
    }

    @Test
    public void Player3NotAllowedToPushPlayer4ThroughWall() {
        setUpBoard(112);
    }

}