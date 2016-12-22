package com.sc.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



/**
 * Created by AC on 12/18/2016.
 */
public class HUD implements Disposable {

   // public com.badlogic.gdx.scenes.scene2d.Stage stage;
    Viewport view;

    private int worldtime;
    private float time;
    static int score = 0;
    public Stage stage;


    Label countdown;
    static Label scoreL;
    Label timeL;
    Label level;
    Label world;
    Label mario;
    static int score2;

    public HUD(SpriteBatch sb){

        worldtime = 400;
        time = 0;
        score = 0;
        score2 = 0;

        view = new FitViewport(MarioBros.width, MarioBros.height, new OrthographicCamera());
        stage = new Stage(view,sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdown = new Label(String.format("%03d",worldtime), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreL = new Label(String.format("%05d",score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeL = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        level = new Label("1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        world = new Label("World", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        mario = new Label("Mario", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(mario).expandX().padTop(5);
        table.add(world).expandX().padTop(5);
        table.add(timeL).expandX().padTop(5);
        table.row();
        table.add(scoreL).expandX();
        table.add(level).expandX();
        table.add(countdown).expandX();

        stage.addActor(table);

    }

    public void update(float dt){

        time += dt;

        if(time >= 1){
            worldtime--;
            countdown.setText(String.format("%03d",worldtime));
            time = 0;
        }

    }

    public static void addScore(int value){



        score += value;
        score2 += value;

        scoreL.setText(String.format("%05d",score));

    }



    public static int getScore(){
        return score;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }


}
