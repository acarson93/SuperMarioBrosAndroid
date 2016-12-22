package com.sc.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AC on 12/20/2016.
 */
public class Goomba extends Enemies {

    public float statetime;


   // private Body body;
    private Animation animation;
    private Array<TextureRegion> frames;


    private boolean sDestroy;
    private boolean dead;


    public Goomba(PScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();


        for(int i=0;i<2;i++){
            frames.add(new TextureRegion(screen.getTextureA().findRegion("goomba"), i * 16, 0 ,16, 16));


        }



        animation = new Animation(0.3f, frames);
        statetime = 0;
        sDestroy = false;
        dead = false;
        setBounds(getX(), getY(), 16 / MarioBros.pp, 16 / MarioBros.pp);
    }


    @Override
    protected void defineEnemy() {

        BodyDef bd = new BodyDef();
        bd.position.set(getX(), getY());
        bd.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bd);



        FixtureDef fd = new FixtureDef();
        CircleShape circle = new CircleShape();
        circle.setRadius(5 / MarioBros.pp);

        fd.filter.categoryBits = MarioBros.ENEMY_BIT;
        fd.filter.maskBits = MarioBros.BRICK_BIT | MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT | MarioBros.MARIO_BIT;

        fd.shape = circle;
        body.createFixture(fd).setUserData(this);

        //Create the Head here:
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-1, 10).scl(1 / MarioBros.pp);
        vertice[1] = new Vector2(1, 10).scl(1 / MarioBros.pp);
        vertice[2] = new Vector2(-1, 7).scl(1 / MarioBros.pp);
        vertice[3] = new Vector2(1, 7).scl(1 / MarioBros.pp);
        head.set(vertice);

        fd.shape = head;
        fd.restitution = 0.5f;
        fd.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;

        body.createFixture(fd).setUserData(this);

    }

    @Override
    protected void headJump() {

        HUD.addScore(40);
        sDestroy = true;


    }

    public void update(float dt){

        statetime += dt;


        if(sDestroy && !dead){
            world.destroyBody(body);
            dead = true;
            setRegion(new TextureRegion(screen.getTextureA().findRegion("goomba"), 32, 0, 16, 16));
            statetime = 0;

        }

        else if(!dead) {
            body.setLinearVelocity(velocity);
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) animation.getKeyFrame(statetime, true));
        }

    }

    public void draw(Batch batch){
        if(!dead || statetime < 1)
            super.draw(batch);
    }


}
