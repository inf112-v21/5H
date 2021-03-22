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


    public void setUpBoard(int boardNum) {
        networkSettings = new NetworkSettings("Server", "localhost", 1, 1);
        game = new Game(networkSettings, 4);
        board = new Board();
        board.readBoard(boardNum);
        game.board = board;
        laserList = board.getLaserList();
        game.setLaserList(laserList);
        player1 = board.getPlayerList().get(0);
        player2 = board.getPlayerList().get(1);
        player3 = board.getPlayerList().get(2);
        player4 = board.getPlayerList().get(3);

    }

    @Test
    public void player1damagesPlayer2NoOtherDamage() {
        setUpBoard(101); //Testboard for this problem
        for (Player player : board.getPlayerList()) {
            System.out.println(player.getPc());
        }
        game.fireLasers();
        System.out.println("------------");
        for (Player player : board.getPlayerList()) {
            System.out.println(player.getPc());
        }

    }


}