package com.sc.game;

/**
 * Created by AC on 12/19/2016.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;


public class Brick extends InteractiveTileObjects {

    boolean hit = false;
    private PScreen screen;

    public Brick(PScreen screen, MapObject obj){

        super(screen, obj);
        this.screen = screen;
        fixture.setUserData(this);


        setCategoryFilter(MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {

        if(hit==false ){
            Music sound = MarioBros.manager.get("brick.wav",Music.class);
            sound.play();
            setCategoryFilter(MarioBros.DESTROYED_BIT);
            HUD.addScore(20);
            getCell().setTile(null);
            hit = true;
        }




    }

}