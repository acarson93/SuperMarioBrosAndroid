package com.sc.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Created by AC on 12/19/2016.
 */
public class Mario extends Sprite {

    public enum State {STANDING, FALLING, JUMPING, RUNNING, GROW, HIT, DEAD};

    public State current;
    public State previous;

    public World world;
    public Body body;
    private TextureRegion stand;

    private Animation run;
    private Animation jump;

    public boolean isjump = false;

    public boolean runRight = false;
    private float statetimer;

    private boolean hitbyenemy;
    private boolean dead;
    private PScreen screen;

    private static TiledMapTileSet tileSet;
    private TextureRegion bigMarioS;
    private TextureRegion bigMarioR;
    private TextureRegion bigMarioJ;
    private TextureRegion bigMarioF;
    private TextureRegion marioD;

    private Animation bigMarioRun;
    private Animation bigMarioJump;
    private Animation marioGrow;

    private boolean isRedefine;

    private float invulnerableperiod;
    private float currenttime;



    private boolean isDead;


    private Array <Goomba> goombas;
    private final int BLANK_COIN = 28;

    int keepscore = HUD.getScore();

    public static boolean hG;
    private boolean rGA;
    private boolean isBig;
    private boolean justHit;

    private Music sound;




    public Mario(World world, PScreen screen){
        super(screen.getTextureA().findRegion("little_mario"));
        tileSet = screen.tiledm.getTileSets().getTileSet("supermariotiles");
        this.world = world;
        this.screen = screen;
        current = State.STANDING;
        previous = State.STANDING;
        statetimer = 0;
        runRight = true;
        hitbyenemy = false;
        dead = false;
        hG = false;
        justHit = false;
        isRedefine = false;
        isBig = false;
        isDead = false;


        goombas = new Array<Goomba>();

        Array <TextureRegion> frames = new Array<TextureRegion>();

        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(getTexture(), i * 16, 0 , 16, 16));
        }

        run = new Animation (0.1f, frames);
        frames.clear();

        for(int i = 4; i<6; i++){
            frames.add(new TextureRegion(getTexture(), i * 16, 0 ,16, 16));
        }

        jump = new Animation(0.1f,frames);
        frames.clear();

        for(int i = 1; i<4; i++){
            frames.add(new TextureRegion(screen.getTextureA().findRegion("big_mario"), i * 16, 0 ,16, 32));
        }

        bigMarioRun = new Animation(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(screen.getTextureA().findRegion("big_mario"),240, 0 , 16, 32));
        frames.add(new TextureRegion(screen.getTextureA().findRegion("big_mario"),0, 0 , 16, 32));
        frames.add(new TextureRegion(screen.getTextureA().findRegion("big_mario"),240, 0 , 16, 32));
        frames.add(new TextureRegion(screen.getTextureA().findRegion("big_mario"),0, 0 , 16, 32));
        frames.add(new TextureRegion(screen.getTextureA().findRegion("big_mario"),240, 0 , 16, 32));
        frames.add(new TextureRegion(screen.getTextureA().findRegion("big_mario"),0, 0 , 16, 32));
        frames.add(new TextureRegion(screen.getTextureA().findRegion("big_mario"),240, 0 , 16, 32));
        frames.add(new TextureRegion(screen.getTextureA().findRegion("big_mario"),0, 0 , 16, 32));

        marioGrow = new Animation(0.3f,frames);

        bigMarioS = new TextureRegion(screen.getTextureA().findRegion("big_mario"),0,0,16,32);

        bigMarioJ = new TextureRegion(screen.getTextureA().findRegion("big_mario"),80,0,16,32);
        marioD = new TextureRegion(screen.getTextureA().findRegion("little_mario"),96,0,16,16);

        BodyDef bd = new BodyDef();
        bd.position.set(32 / MarioBros.pp,32 / MarioBros.pp);
        bd.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bd);

        FixtureDef fd = new FixtureDef();
        CircleShape circle = new CircleShape();
        circle.setRadius(5 / MarioBros.pp);

        fd.filter.categoryBits = MarioBros.MARIO_BIT;
        fd.filter.maskBits = MarioBros.BRICK_BIT | MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT | MarioBros.ENEMY_HEAD_BIT;

        fd.shape = circle;

        body.createFixture(fd).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/MarioBros.pp, 6 / MarioBros.pp), new Vector2(2/ MarioBros.pp, 6 /MarioBros.pp));
        fd.shape = head;
        fd.isSensor = true;


        body.createFixture(fd).setUserData("head");
        stand = new TextureRegion(getTexture(),0,0,16,16);
        setBounds(0,0,16/MarioBros.pp, 16 / MarioBros.pp);
        setRegion(stand);


        BodyDef bdef = new BodyDef();
        PolygonShape poly = new PolygonShape();
        FixtureDef fix = new FixtureDef();
        Body body;

        for(MapObject object: screen.tiledm.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle r = ((RectangleMapObject)object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((r.getX() + r.getWidth()/2)/ MarioBros.pp, (r.getY() + r.getHeight()/2)/MarioBros.pp);

            body = world.createBody(bdef);

            poly.setAsBox(r.getWidth()/2 / MarioBros.pp ,r.getHeight()/2 / MarioBros.pp);
            fix.shape = poly;
            body.createFixture(fix);
        }

        for(MapObject object: screen.tiledm.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle r = ((RectangleMapObject)object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((r.getX() + r.getWidth()/2)/ MarioBros.pp, (r.getY() + r.getHeight()/2)/MarioBros.pp);

            body = world.createBody(bdef);

            poly.setAsBox(r.getWidth()/2 / MarioBros.pp ,r.getHeight()/2 / MarioBros.pp);
            fix.shape = poly;
            fd.filter.categoryBits = MarioBros.OBJECT_BIT;
            body.createFixture(fix);
        }

        for(MapObject object: screen.tiledm.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){

            new Coin(screen, object);
        }

        for(MapObject object: screen.tiledm.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
           new Brick(screen,object);
        }

        for(MapObject object: screen.tiledm.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle r = ((RectangleMapObject)object).getRectangle();

            goombas.add(new Goomba(screen, r.getX()/ MarioBros.pp, r.getY() / MarioBros.pp));
        }



    }

    public static boolean getHG(){
        return hG;
    }

    public boolean getisDead() {
        return isDead;
    }

    public void gotHit(){


        if(hG==true){
            hG = false;
            setBounds(getX(), getY(), getWidth(), getHeight() / 2);
            justHit = true;
            rGA = true;
            sound = MarioBros.manager.get("smb_pipe.wav", Music.class);
            sound.play();
            invulnerableperiod = currenttime + 0.5f;


        }



        else {

            if (currenttime > invulnerableperiod) {
                System.out.println("got hit mario died");
                isDead = true;
                sound = MarioBros.manager.get("smb_mariodie.wav", Music.class);
                sound.play();
                screen.getMusic().pause();
                Filter filter = new Filter();
                filter.maskBits = MarioBros.NOTHING_BIT;
                invulnerableperiod = statetimer + 10;

                for (Fixture fixture : body.getFixtureList()) {
                    fixture.setFilterData(filter);
                    body.applyLinearImpulse(new Vector2(0, 2f), body.getWorldCenter(), true);
                }

            }
        }






    }



    public void grow(){
        rGA = true;
        hG = true;
        sound = MarioBros.manager.get("smb_powerup.wav", Music.class);
        sound.play();

        setBounds(getX(), getY(), getWidth(), getHeight() * 2);
    }

    public void update(float d){
        statetimer += d;
        currenttime += d;
        //System.out.println("current time is " + currenttime);


        if(hG==false) {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 3);
            //setRegion(getFrame(d));
        }

        else{
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 4);
            //setRegion(getFrame(d));
        }

        setRegion(getFrame(d));



    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }

    public TextureRegion getFrame(float dt){
        //System.out.println("in here");

        TextureRegion region;
        current = getState();

        //System.out.println("curent is " + current.name());

        if(current!=State.JUMPING || current!=State.FALLING){
            isjump = false;
        }

        switch(current){
            case DEAD:

                region = (TextureRegion)marioD;
                break;

            case GROW :

                region = (TextureRegion)marioGrow.getKeyFrame(statetimer);
                if(marioGrow.isAnimationFinished(statetimer)){
                    rGA = false;
                }
                break;
            case HIT:
                //System.out.println("just got hit");
                region = (TextureRegion)marioGrow.getKeyFrame(statetimer);
                if(marioGrow.isAnimationFinished(statetimer)){
                    rGA = false;
                }
                justHit = false;
                break;
            case JUMPING :
                //region = (TextureRegion)jump.getKeyFrame(statetimer);
                if(hG==true){
                    region = (TextureRegion)bigMarioJ;
                }
                else{
                    region = (TextureRegion)jump.getKeyFrame(statetimer);
                }
                isjump = true;
                break;
            case RUNNING:

                if(hG==true){
                    region = (TextureRegion)bigMarioRun.getKeyFrame(statetimer, true);
                }
                else {
                    region = (TextureRegion) run.getKeyFrame(statetimer, true);
                }
                break;
            case FALLING:
                isjump = true;
            case STANDING:
            default:
                if(hG==true) {
                    region = bigMarioS;
                }
                else{
                    region = (TextureRegion) stand;
                }
                isjump = false;
                break;

        }


        if((body.getLinearVelocity().x < 0 || !runRight) && !region.isFlipX()){
            region.flip(true,false);
            runRight = false;
        }

        if((body.getLinearVelocity().x > 0 || runRight) && region.isFlipX()){
            region.flip(true,false);
            runRight = true;
        }

        if(current==previous){
            statetimer = statetimer + dt;
        }
        else{
            statetimer = 0;
        }

        previous = current;
        //System.out.println("region is " + region.getClass().getName();
        return region;
    }

    public State getState(){

        if(isDead){

            return State.DEAD;
        }

        if(rGA){

            return State.GROW;
        }

        else if(justHit){
            return State.HIT;
        }

        else if(body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && previous == State.JUMPING)){
            return State.JUMPING;
        }

        else if(body.getLinearVelocity().y<0){
            return State.FALLING;
        }

        else if(body.getLinearVelocity().x != 0){


            return State.RUNNING;
        }

        else{
            return State.STANDING;
        }


    }






}
