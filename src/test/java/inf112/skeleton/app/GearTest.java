package inf112.skeleton.app;
import inf112.skeleton.app.net.NetworkSettings;
import inf112.skeleton.app.sprites.Direction;
import inf112.skeleton.app.sprites.Player;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class GearTest {

    private Player player1;
    private Player player2;
    private Game game;


    public void setUpBoard(int boardNum) {
        NetworkSettings networkSettings = new NetworkSettings("test", "localhost", 3, 3);
        game = new Game(networkSettings, 4, boardNum);
        Board board = new Board();
        board.readBoard(boardNum);
        game.board = board;
        game.setBoardSize(board.getSize());
        player1 = board.getPlayerList().get(0);
        player2 = board.getPlayerList().get(1);
        game.createAlivePlayerList();

    }

    @Test
    public void gearRotatesPlayer() {
        setUpBoard(140);
        player1.move(0,1); //move to  the gear on the board
        // The way our boards work players can't start on board objects like gears, conveyorbelts etc.
        Direction initialPlayer1Direction = player1.getDirection();
        game.runGear(true); // Had to make a overload specifically for tests, not pretty but can't deal with sprites in automatic tests and the original method rotates player sprites
        assertNotEquals(initialPlayer1Direction, player1.getDirection(), "Gear did not rotate the player");
    }

    @Test
    public void gearRotateCorrectDirection() {
        //Player 1 initially faces north and moves to a right gear and should end up facing east
        //Player 2 initially faces north and moves to a left gear and should end up facing west
        setUpBoard(140);
        player1.move(0,1);
        player2.move(0,1);
        game.runGear(true);
        assertEquals(Direction.EAST, player1.getDirection(), "Player 1 was not turned to correct orientation");
        assertEquals(Direction.WEST, player2.getDirection(), "Player 2 was not turned to correct orientation");
    }


}
