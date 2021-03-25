package inf112.skeleton.app.networktests;

import com.esotericsoftware.kryonet.Connection;
import inf112.skeleton.app.net.GameClientListener;
import inf112.skeleton.app.net.GameServerListener;
import inf112.skeleton.app.net.PlayerMoves;
import inf112.skeleton.app.net.RequestToClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class GameClientListenerTest {

    private GameClientListener gameClientListener;
    private GameServerListener gameServerListener;

    @BeforeEach
    public void setUp(){
        gameClientListener = new GameClientListener();
        gameServerListener = new GameServerListener(8);
    }


    @Test
    public void receivedTest(){
        Connection con = gameServerListener.getPlayer(0); //Random connection for the gameClientListener.
        PlayerMoves playerMoves = new PlayerMoves(); //PlayerMoved class to be used as argument for the receive method.
        RequestToClient requestToClient = new RequestToClient();//RequestToClient class to be used as argument for the receive method.
        requestToClient.setRequestType("Move");

        assertFalse(gameClientListener.hasReceivedAllMoves());
        gameClientListener.received(con, playerMoves);
        assertTrue(gameClientListener.hasReceivedAllMoves());

        assertFalse(gameClientListener.getNeedMoveInput());
        gameClientListener.received(con, requestToClient);
        assertTrue(gameClientListener.getNeedMoveInput());
    }

    @Test
    public void resetNeedMoveInputTest(){
        gameClientListener.resetNeedMoveInput();
        assertFalse(gameClientListener.getNeedMoveInput());
    }

    @Test
    public void getNeedMoveInputTest(){
        assertFalse(gameClientListener.getNeedMoveInput());
    }

    @Test
    public void getPlayerMovedTest(){
        assertNull(gameClientListener.getAllPlayerMoves());
    }

    @Test
    public void playerHasMovedTest(){
        assertFalse(gameClientListener.hasReceivedAllMoves());
    }

    @Test
    public void resetPlayerHasMovedTest(){
        gameClientListener.resetHasReceivedAllMoves();
        assertFalse(gameClientListener.hasReceivedAllMoves());
    }
}
