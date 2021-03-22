package inf112.skeleton.app;

import inf112.skeleton.app.net.NetworkSettings;
import inf112.skeleton.app.sprites.Laser;
import inf112.skeleton.app.sprites.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class LaserTest {
    private Board board;
    private Player player1;
    private int player1PcBefore;
    private Player player2;
    private int player2PcBefore;
    private Player player3;
    private int player3PcBefore;
    private int player4PcBefore;
    private Player player4;
    private Game game;
    private NetworkSettings networkSettings;
    private ArrayList<Laser> laserList;
    HashMap<Player, Integer> pcBefore;


    public void setUpBoard(int boardNum) {
        networkSettings = new NetworkSettings("server", "localhost", 2, 2);
        game = new Game(networkSettings, 4);
        board = new Board();
        board.readBoard(boardNum);
        game.board = board;
        game.setBoardSize(board.getSize());
        laserList = board.getLaserList();
        game.setLaserList(laserList);
        player1 = board.getPlayerList().get(0);
        player1PcBefore = player1.getPc();
        player2 = board.getPlayerList().get(1);
        player2PcBefore = player2.getPc();
        player3 = board.getPlayerList().get(2);
        player4 = board.getPlayerList().get(3);
        player4PcBefore = player4.getPc();
        pcBefore = makePlayerPcHashMap();

    }

    public HashMap<Player, Integer> makePlayerPcHashMap() {
        HashMap<Player, Integer> hashMap = new HashMap<Player, Integer>();
        for (Player player : board.getPlayerList()) {
            hashMap.put(player, player.getPc());
        }
        return hashMap;
    }

    @Test
    public void noPlayerTakesDamage() {
        setUpBoard(100);
        game.fireLasers();
        assertEquals(pcBefore, makePlayerPcHashMap());
    }

    @Test
    public void player1TakesDamageOtherPlayersDoesNot() {
        setUpBoard(101); //Testboard for this problem
        game.fireLasers();
        //Make a hashmap with expected pc values based on pc values before
        HashMap<Player, Integer> expectedPc = pcBefore;
        // Player 1 is expected to take 1 damage so subtract 1 from player1s pc value
        expectedPc.put(player1, expectedPc.get(player1)-1);
        // Make new hashmap with current pc values
        HashMap<Player, Integer> pcAfter = makePlayerPcHashMap();
        assertEquals(expectedPc, pcAfter, "Some player does not have the pc that was expected after firing lasers on this board");
        }

    @Test
    public void player1DoesNotTakeDamageThroughPlayer2() {
        setUpBoard(102);
        game.fireLasers();
        //Player 2 will take damage and stop the laser so it shouldn't hit player 1
        assertEquals(player2PcBefore-1, player2.getPc(), "Laser did not hit or did too much damage to player2");
        assertEquals(player1PcBefore, player1.getPc(), "Player 1 took damage somehow when they shouldn't");
    }
    @Test
    public void wallPreventsDamage() {
        setUpBoard(103);
        game.fireLasers();
        assertEquals(player1PcBefore, player1.getPc(), "Player 1 took damage through the wall!");

    }
    @Test
    public void moreThanOneLaserFiresInOneMethodCall() {
        setUpBoard(104);
        game.fireLasers();
        assertEquals(player1PcBefore -1, player1.getPc(),"Laser 1 didn't fire"); //Note it will break here and you don't know if laser 2 fired
        assertEquals(player4PcBefore -1, player4.getPc(), "Laser 2 didn't fire");
    }

    }
