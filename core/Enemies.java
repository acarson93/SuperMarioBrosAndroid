package com.sc.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by AC on 12/20/2016.
 */
public abstract class Enemies extends Sprite {

    public World world;
    protected PScreen screen;
    public Body body;

    public Vector2 velocity;

    public Enemies(PScreen screen, float x, float y){
        this.world = screen.world;
        this.screen = screen;

        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(-1,0);
        body.setActive(false);


    }

    public void reverse(boolean x, boolean y){
        if(x){
            velocity.x = -velocity.x;
        }

        if(y){
            velocity.y = -velocity.y;
        }
    }
    protected abstract void defineEnemy();
    protected abstract void headJump();
    public abstract void update(float dt);





    }

