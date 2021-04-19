package inf112.skeleton.app;
import inf112.skeleton.app.net.NetworkSettings;
import inf112.skeleton.app.sprites.ConveyorBelt;
import inf112.skeleton.app.sprites.Direction;
import inf112.skeleton.app.sprites.ExpressConveyorBelt;
import inf112.skeleton.app.sprites.Player;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ConveyorTest {

    private Player player1;
    private Player player2;
    private Game game;
    private HashMap<Direction, Pair> dirMap;
    private Board board;


    public void setUpBoard(int boardNum) {
        NetworkSettings networkSettings = new NetworkSettings("test", "localhost", 3, 3);
        game = new Game(networkSettings, 4, boardNum);
        board = new Board();
        board.readBoard(boardNum);
        game.board = board;
        game.setBoardSize(board.getSize());
        player1 = board.getPlayerList().get(0);
        player2 = board.getPlayerList().get(1);
        game.createAlivePlayerList();
        dirMap = game.getDirMap();

    }


    @Test
    public void conveyorBeltMovesPlayer () {
        setUpBoard(130);
        player1.move(0,1); //Moves to conveyorbelt
        // The way our boards work players can't start on board objects like gears, conveyorbelts etc.
        Pair player1coords = player1.getCoordinates().getCopy();
        game.runConveyorBelt(); //Run conveyorbelt for all players
        assertNotEquals(player1coords, player1.getCoordinates(), "Player was not moved by conveyor belts"); //Check that the coordinates have changed since conveyorbelt was ran

    }

    @Test
    public void conveyorBeltMovesPlayerInCorrectDirection() {
        setUpBoard(130);
        player1.move(0,1);
        Pair player1coords = player1.getCoordinates(); //get player1 current coords
        ConveyorBelt conveyorBelt = (ConveyorBelt) board.getOriginalPosition(player1coords.getX(), player1coords.getY()); //get conveyorbelt object from original board
        Direction conveyorDirection = conveyorBelt.getDir(); //Get the direction the conveyorbelt pushes in
        Pair dir = dirMap.get(conveyorDirection);
        Pair expectedPositionPlayer1 = player1coords.getCopy();
        expectedPositionPlayer1.setX(player1coords.getX() + dir.getX()); // Calculate position player 1 is expected to be moved to by conveyorbelts
        expectedPositionPlayer1.setY(player1coords.getY() + dir.getY());
        game.runConveyorBelt();
        assertEquals(expectedPositionPlayer1, player1.getCoordinates(), "Player was not moved in correct direction, may not have been moved at all");
    }

    @Test
    public void expressConveyorMovesTwice() {
        setUpBoard(130);
        player2.move(0,1);
        Pair player2coords = player2.getCoordinates();
        ExpressConveyorBelt expressConveyorBelt = (ExpressConveyorBelt) board.getOriginalPosition(player2coords.getX(), player2coords.getY());
        Direction conveyorDirection = expressConveyorBelt.getDir();
        Pair dir = dirMap.get(conveyorDirection);
        Pair expectedPositionPlayer2 = player2coords.getCopy();
        expectedPositionPlayer2.setX(player2coords.getX() + dir.getX());
        expectedPositionPlayer2.setY(player2coords.getY() + dir.getY());
        game.runExpressConveyorBelt(); // Run only express conveyorbelts once
        assertEquals(expectedPositionPlayer2, player2.getCoordinates(), "Express conveyor did not do express run");
        //Player should now land on another expressconveyorbelt that should run this time as well.
        player2coords = player2.getCoordinates();
        expressConveyorBelt = (ExpressConveyorBelt) board.getOriginalPosition(player2coords.getX(), player2coords.getY());
        conveyorDirection = expressConveyorBelt.getDir();
        dir = dirMap.get(conveyorDirection);
        expectedPositionPlayer2 = player2coords.getCopy();
        expectedPositionPlayer2.setX(player2coords.getX() + dir.getX());
        expectedPositionPlayer2.setY(player2coords.getY() + dir.getY());
        game.runConveyorBelt(); // Run all conveyorbelts (normal & express) once
        assertEquals(expectedPositionPlayer2, player2.getCoordinates(), "Express conveyor did not run when regular conveyors were called");
    }

    @Test
    public void regularConveyorDoesNotRunOnExpressCall() {
        setUpBoard(130);
        player1.move(0,1); // On regular conveyorBelt
        Pair player1coords = player1.getCoordinates().getCopy();
        game.runExpressConveyorBelt();
        assertEquals(player1coords, player1.getCoordinates(), "The regular conveyorbelt moved the player when the express belt was called, this should not occur");

    }
}
