package inf112.skeleton.app;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class showCardsTest {

    public Viewport viewport;

    public void create() {
        Camera camera = new OrthographicCamera();
        viewport = new FitViewport(80, 48, camera);
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }



}
