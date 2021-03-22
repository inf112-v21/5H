package inf112.skeleton.app;

import com.badlogic.gdx.Net;
import inf112.skeleton.app.net.NetworkSettings;
import inf112.skeleton.app.sprites.Flag;
import inf112.skeleton.app.sprites.Laser;
import inf112.skeleton.app.sprites.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class LaserTest {
    private Board board;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Game game;
    private NetworkSettings networkSettings;
    private ArrayList<Laser> laserList;
    HashMap<Player, Integer> pcBefore;


    public void setUpBoard(int boardNum) {
        networkSettings = new NetworkSettings("server", "localhost", 1, 1);
        game = new Game(networkSettings, 4);
        board = new Board();
        board.readBoard(boardNum);
        game.board = board;
        game.setBoardSize(board.getSize());
        laserList = board.getLaserList();
        game.setLaserList(laserList);
        player1 = board.getPlayerList().get(0);
        player2 = board.getPlayerList().get(1);
        player3 = board.getPlayerList().get(2);
        player4 = board.getPlayerList().get(3);
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
    public void player1TakesDamageOtherPlayersDoesNot() {
        setUpBoard(101); //Testboard for this problem
        game.fireLasers();
        //Make a hashmap with expected pc values based on pc values before
        HashMap<Player, Integer> excectedPc = pcBefore;
        // Player 1 is expected to take 1 damage so subtract 1 from player1s pc value
        excectedPc.put(player1, excectedPc.get(player1)-1);
        // Make new hasmap
        HashMap<Player, Integer> pcAfter = makePlayerPcHashMap();
        assertEquals(excectedPc, pcAfter);


        }

    }
