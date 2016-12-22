package com.sc.game;

/**
 * Created by AC on 12/19/2016.
 */
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;



public abstract class InteractiveTileObjects {
    protected World world;
    protected TiledMap map;
    protected Rectangle bounds;
    protected Body body;
    protected PScreen screen;
    protected MapObject object;

    protected Fixture fixture;

    public InteractiveTileObjects(PScreen screen, MapObject object){


        this.object = object;
        this.screen = screen;
        this.world = screen.world;
        this.map = screen.tiledm;
        this.bounds = ((RectangleMapObject) object).getRectangle();

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / MarioBros.pp, (bounds.getY() + bounds.getHeight() / 2) / MarioBros.pp);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / MarioBros.pp, bounds.getHeight() / 2 / MarioBros.pp);
        fdef.shape = shape;
        //fdef.isSensor = true;
        //body.createFixture(fdef).setUserData("uu");
        fixture = body.createFixture(fdef);

    }

    public abstract void onHeadHit();

    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);


    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x * MarioBros.pp / 16),
                (int)(body.getPosition().y * MarioBros.pp / 16));
    }

}
