package com.sc.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.prism.image.ViewPort;

import javafx.stage.Screen;

/**
 * Created by AC on 12/18/2016.
 */
public class PScreen implements com.badlogic.gdx.Screen {

    private MarioBros game;
    private TextureAtlas atlas;
    //Texture texture;
    private OrthographicCamera cam;
    private Viewport gport;
    private HUD hud;
    private TmxMapLoader mapLoader;
    public TiledMap tiledm;
    private OrthogonalTiledMapRenderer renderer;
    public World world;
    private Box2DDebugRenderer debug;



    private Mario player;
    private boolean pressup = false;
    private Brick brick;




    private Music music;
    private Music sound;

    private int keepscore = HUD.getScore();



    int count = 0;

    public PScreen(MarioBros game){
        atlas = new TextureAtlas("MarioEnemies.pack");

        this.game = game;
        //texture = new Texture("badlogic.jpg");
        cam = new OrthographicCamera();
        gport = new FitViewport(MarioBros.width / MarioBros.pp ,MarioBros.height / MarioBros.pp ,cam );
       // gport.apply();
        hud = new HUD(game.batch);

        mapLoader = new TmxMapLoader();
        tiledm = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledm, 1 / MarioBros.pp);
        cam.position.set(gport.getWorldWidth()/2, gport.getWorldHeight()/2, 0);

        world = new World(new Vector2(0,-10),true);
        debug = new Box2DDebugRenderer();
        player = new Mario(world, this);


        music = MarioBros.manager.get("SuperMarioBros.ogg", Music.class);

        music.setLooping(true);
        music.play();






        world.setContactListener(new WorldContactListen());





    }

    public TextureAtlas getTextureA(){
        return atlas;
    }

    public Music getMusic() {
        return music;
    }

    public Mario getPlayer() {
        return player;
    }

    public void update(float d){



       // System.out.println("hud val is " + HUD.score2);
            //System.out.println("score 2 is " + HUD.score2);
        if(HUD.score2>=50){
            //System.out.println("im in here");
            player.grow();
            HUD.score2 -= 50;
            //HUD.score2-=100;
        }

        if(!player.getisDead()) {

            if (Gdx.input.isKeyJustPressed((Input.Keys.UP)) && player.isjump == false) {
                player.body.applyLinearImpulse(new Vector2(0, 4f), player.body.getWorldCenter(), true);
                sound = MarioBros.manager.get("jump_small.wav", Music.class);
                sound.play();
                player.isjump = true;
            }


            if (Gdx.input.isKeyPressed((Input.Keys.RIGHT)) && player.body.getLinearVelocity().x <= 2) {
                player.body.applyLinearImpulse(new Vector2(0.1f, 0), player.body.getWorldCenter(), true);
                //player.current = Mario.State.RUNNING;
                //player.runRight = true;
            }

            if (Gdx.input.isKeyPressed((Input.Keys.LEFT)) && player.body.getLinearVelocity().x >= -2) {
                player.body.applyLinearImpulse(new Vector2(-0.1f, 0), player.body.getWorldCenter(), true);
                //player.current = Mario.State.RUNNING;
                //player.runRight = false;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                music.stop();

                count++;

                if (count >= game.listofmusic.size()) {
                    count = 0;
                }


                music = MarioBros.manager.get(game.listofmusic.get(count), Music.class);
                music.setLooping(true);
                music.play();

            }
        }

        world.step(1/60f, 6, 2);
        player.update(d);

        for(Enemies enemy: player.getGoombas()){
            enemy.update(d);

            if(enemy.getX() < player.getX() + 224 / MarioBros.pp){
                enemy.body.setActive(true);
            }
        }
        hud.update(d);

        if(!player.getisDead()) {
            cam.position.x = player.body.getPosition().x;

        }
        cam.update();
        renderer.setView(cam);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        update(delta);


        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
        debug.render(world,cam.combined);

        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for(Enemies enemy: player.getGoombas()){
            enemy.draw(game.batch);
        }
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        /*game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.draw(texture,0,0);
        game.batch.end();*/


    }

    @Override
    public void resize(int width, int height) {

        gport.update(width,height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        tiledm.dispose();
        renderer.dispose();
        world.dispose();
        debug.dispose();
        hud.dispose();


    }
}
