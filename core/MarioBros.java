package com.sc.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class MarioBros extends Game {
	public SpriteBatch batch;
	Texture img;

	public static AssetManager manager;
	public static final int height = 210;
	public static final int width = 260;
	public static final float pp = 100;

	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short MARIO_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 130;
	public static final short ITEM_BIT = 256;
	public static final short MARIO_HEAD_BIT = 512;
	public static final short FIREBALL_BIT = 1024;

	public ArrayList <String> listofmusic;

	
	@Override
	public void create () {
		listofmusic = new ArrayList<String>();
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("SuperMarioBros.ogg", Music.class);
		listofmusic.add("SuperMarioBros.ogg");
		manager.load("FFVII_Main_Theme.ogg", Music.class);
		listofmusic.add("FFVII_Main_Theme.ogg");
		manager.load("Overworld.ogg", Music.class);
		listofmusic.add("Overworld.ogg");
		manager.load("coin.wav", Music.class);
		manager.load("brick.wav", Music.class);
		manager.load("jump_small.wav", Music.class);
		manager.load("smb_powerup.wav", Music.class);
		manager.load("smb_pipe.wav",Music.class);
		manager.load("smb_mariodie.wav", Music.class);
		manager.finishLoading();
		setScreen(new PScreen(this));

		//img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		manager.dispose();

		//img.dispose();
	}
}
