package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;


public class TiledMapSample extends ApplicationAdapter implements InputProcessor {
	MyMap map;
	OrthographicCamera camera;
	KeyInput key;
	private BitmapFont font;
	private SpriteBatch batch;

	final int GAME_WIDTH = 800;
	final int GAME_HEIGHT = 480;

	Pika pika = null;

	@Override
	public void create() {
		createCamera();
		map = new MyMap();
		Gdx.input.setInputProcessor(this);
		key = new KeyInput();

		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.RED);

		pika = new Pika(400, 240);
	}

	private void createCamera() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
		camera.update();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		ScrollMap(key);
		camera.update();

		map.renderBackground(camera);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		pika.draw(batch);
		renderFont();
		batch.end();

		map.renderForeground(camera);
	}

	private void renderFont() {
		font.draw(batch, "Pika... x." + pika.getX() + ", y:" + pika.getY(), camera.position.x + 50, camera.position.y + 50);
		font.draw(batch, "Camera... x." + camera.position.x + ", y:" + camera.position.y, camera.position.x + 50, camera.position.y + 100);
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
		font.dispose();
		map.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		key.keyPressed(keycode);

		if (keycode == Input.Keys.NUM_1)
			map.switchVisibleLayer(0);
		if (keycode == Input.Keys.NUM_2)
			map.switchVisibleLayer(1);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		key.keyReleased(keycode);
		return false;
	}

	private void ScrollMap(KeyInput key) {
		final int MAP_SCROLL_LENGTH = 10;
		final int centerX = GAME_WIDTH/2;
		final int centerY = GAME_HEIGHT/2;

		if (key.isPressing(Input.Keys.LEFT)) {
			camera.translate(-MAP_SCROLL_LENGTH, 0);
			if (camera.position.x < centerX)
				camera.position.x = centerX;
		}
		if (key.isPressing(Input.Keys.RIGHT)) {
			camera.translate(MAP_SCROLL_LENGTH, 0);
			if (camera.position.x > map.getWidth() - centerX)
				camera.position.x = map.getWidth() - centerX;
		}
		if (key.isPressing(Input.Keys.UP)) {
			camera.translate(0, MAP_SCROLL_LENGTH);
			if (camera.position.y > map.getHeight() - centerY)
				camera.position.y = map.getHeight() - centerY;
		}
		if (key.isPressing(Input.Keys.DOWN)) {
			camera.translate(0, -MAP_SCROLL_LENGTH);
			if (camera.position.y < centerY)
				camera.position.y = centerY;
		}
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0));
		pika.setPosition(position.x, position.y);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector3 position = camera.unproject(new Vector3(screenX, screenY, 0));
		pika.setPosition(position.x, position.y);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}