package inf112.skeleton.app.NetworkTests;

import com.esotericsoftware.kryonet.Connection;
import inf112.skeleton.app.net.GameClientListener;
import inf112.skeleton.app.net.GameServerListener;
import inf112.skeleton.app.net.PlayerMoved;
import inf112.skeleton.app.net.RequestToClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameClientListenerTest {
    private boolean needMoveInput = false;

    private PlayerMoved playerMoved;
    private boolean playerHasMoved;
    GameClientListener gameClientListener;
    GameServerListener gameServerListener;

    @BeforeEach
    public void setUp(){
        gameClientListener = new GameClientListener();
        gameServerListener = new GameServerListener(8);
    }


    @Test
    public void receivedTest(){
        Connection con = gameServerListener.getPlayer(0); //Random connection for the gameClientListener.
        PlayerMoved playerMoved = new PlayerMoved(); //PlayerMoved class to be used as argument for the receive method.
        RequestToClient requestToClient = new RequestToClient();//RequestToClient class to be used as argument for the receive method.
        requestToClient.setRequestType("Move");

        assertFalse(gameClientListener.playerHasMoved());
        gameClientListener.received(con, playerMoved);
        assertTrue(gameClientListener.playerHasMoved());

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
        assertNull(gameClientListener.getPlayerMoved());
    }

    @Test
    public void playerHasMovedTest(){
        assertFalse(gameClientListener.playerHasMoved());
    }

    @Test
    public void resetPlayerHasMovedTest(){
        gameClientListener.resetPlayerHasMoved();
        assertFalse(gameClientListener.playerHasMoved());
    }
}
