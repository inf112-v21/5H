package inf112.skeleton.app;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Main {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("TestGrid");
        cfg.setWindowedMode(900, 900);
        cfg.setResizable(true);
        cfg.useVsync(true);
        cfg.setIdleFPS(60);

        new Lwjgl3Application(new Game(), cfg);
    }
}