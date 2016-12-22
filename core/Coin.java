package com.sc.game;

/**
 * Created by AC on 12/20/2016.
 */


        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.audio.Music;
        import com.badlogic.gdx.audio.Sound;
        import com.badlogic.gdx.maps.MapObject;
        import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
        import com.badlogic.gdx.math.Rectangle;
        import com.badlogic.gdx.math.Vector2;

public class Coin extends InteractiveTileObjects {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;
    private Music sound;
    boolean hit = false;

    public Coin(PScreen screen, MapObject object){
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("supermariotiles");
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.COIN_BIT);
    }

    @Override
    public void onHeadHit() {

        if(hit == false) {
            sound = MarioBros.manager.get("coin.wav", Music.class);
            sound.play();
            HUD.addScore(50);
            getCell().setTile(tileSet.getTile(BLANK_COIN));
            hit = true;
        }

    }
}