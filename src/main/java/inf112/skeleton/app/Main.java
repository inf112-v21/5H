package inf112.skeleton.app;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import inf112.skeleton.app.net.NetworkSettings;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        HashMap<String, String> setupData = GameSetup.setup(); //Set up all game settings
        NetworkSettings settings = new NetworkSettings(setupData.get("state"), setupData.get("ip"), Integer.parseInt(setupData.get("tcp")), Integer.parseInt(setupData.get("udp")));
        int playerCount;
        int boardNum;
        if(setupData.get("state").equals("client")){
            playerCount = 0;
            boardNum = 0;
        }
        else{
            playerCount = Integer.parseInt(setupData.get("playerCount"));
            boardNum = Integer.parseInt(setupData.get("board"));
        }

        //Application setup
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("RoboRally - " + settings.getState().toUpperCase());
        cfg.setWindowedMode(1280, 720);
        cfg.setResizable(false);
        cfg.useVsync(true);
        cfg.setIdleFPS(60);
        new Lwjgl3Application(new Game(settings, playerCount, boardNum), cfg);
        System.exit(0);
    }
}