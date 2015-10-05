package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Pika {
	private float x;
	private float y;
	private TextureRegion pikaTexture;

	public Pika(float x, float y) {
		this.x = x;
		this.y = y;
		pikaTexture = new TextureRegion(new Texture(Gdx.files.internal("pik.png")), 64, 64);
	}

	void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	public TextureRegion getTextureRegion() {
		return pikaTexture;
	}
	public void draw(SpriteBatch batch){
		batch.draw(getTextureRegion(), getX(), getY());
	}
}
