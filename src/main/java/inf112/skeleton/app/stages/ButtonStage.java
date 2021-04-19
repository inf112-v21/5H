package inf112.skeleton.app.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class ButtonStage extends Stage {


    public ButtonStage(){
        Stage stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Button button1 = new Button();

        button1.setSize(99, 153);
        button1.setPosition(645, 530);
        stage.addActor(button1);
        button1.addListener(new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {
            System.out.println("Button 1 pressed");

            }
        });



    }




}

/*
Stage stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Button button = new Button();
        button.setSize(99, 153);
        button.setPosition(645, 530);
        stage.addActor(button);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                System.out.println("Button 1 pressed");
            }
        });
 */