package inf112.skeleton.app;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import inf112.skeleton.app.net.NetworkSettings;

import java.util.HashMap;

public class Main2 {
    public static void main(String[] args) {
        HashMap<String, String> setupData = GameSetup.setup(); //Set up all game settings
        NetworkSettings settings = new NetworkSettings(setupData.get("state"), setupData.get("ip"), Integer.parseInt(setupData.get("tcp")), Integer.parseInt(setupData.get("udp")));
        int playerCount;
        if(setupData.get("state").equals("client")){
            playerCount = 0;
        }
        else{
            playerCount = Integer.parseInt(setupData.get("playerCount"));
        }
        //check connection
        System.out.println(settings.getState());

        //Application setup
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("RoboRally - " + settings.getState().toUpperCase());
        cfg.setWindowedMode(900, 900);
        cfg.setResizable(true);
        cfg.useVsync(true);
        cfg.setIdleFPS(60);
        new Lwjgl3Application(new Game(settings, playerCount), cfg);
        System.exit(42069);
    }
}