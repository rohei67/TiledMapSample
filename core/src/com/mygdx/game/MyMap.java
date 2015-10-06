package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;

public class MyMap {
	TiledMap _tiledMap;
	OrthogonalTiledMapRenderer _tiledMapRenderer;
	private int _mapPixelWidth;
	private int _mapPixelHeight;

	private static final int[] BACKGROUND = new int[]{0};
	private static final int[] FOREGROUND = new int[]{2};

	public MyMap() {
		_tiledMap = new TmxMapLoader().load("MyCrappyMap.tmx");
		_tiledMapRenderer = new OrthogonalTiledMapRenderer(_tiledMap);

		calcMapPixel();

		// get the water frame tiles
		Array<StaticTiledMapTile> frameTiles = readAnimationTileSet();
		// create the animated tiles
		createAnimatedTiles(frameTiles);
	}

	private void calcMapPixel() {
		MapProperties prop = _tiledMap.getProperties();
		int mapWidth = prop.get("width", Integer.class);
		int mapHeight = prop.get("height", Integer.class);
		int tilePixelWidth = prop.get("tilewidth", Integer.class);
		int tilePixelHeight = prop.get("tileheight", Integer.class);

		_mapPixelWidth = mapWidth * tilePixelWidth;
		_mapPixelHeight = mapHeight * tilePixelHeight;
	}

	private boolean searchAnimationTile(TiledMapTile tile, String tileName) {
		return tile.getProperties().containsKey("animation")
				&& tile.getProperties().get("animation", String.class).equals(tileName);
	}

	private Array<StaticTiledMapTile> readAnimationTileSet() {
		Array<StaticTiledMapTile> frameTiles = new Array<StaticTiledMapTile>();

		for (TiledMapTile tile : _tiledMap.getTileSets().getTileSet("Water")) {
			if (searchAnimationTile(tile, "water")) {
				frameTiles.add((StaticTiledMapTile) tile);
			}
		}
		return frameTiles;
	}

	private void createAnimatedTiles(Array<StaticTiledMapTile> frameTiles) {
		AnimatedTiledMapTile animatedTile = new AnimatedTiledMapTile(1 / 3f, frameTiles);

		TiledMapTileLayer layer = (TiledMapTileLayer) _tiledMap.getLayers().get("Background");
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				TiledMapTileLayer.Cell cell = layer.getCell(x, y);

				if (searchAnimationTile(cell.getTile(), "water"))
					cell.setTile(animatedTile);
			}
		}
		for (TiledMapTile tile : frameTiles) {
			animatedTile.getProperties().putAll(tile.getProperties());
		}
	}

	public void renderBackground(OrthographicCamera camera) {
		renderMapLayer(camera, BACKGROUND);
	}

	public void renderForeground(OrthographicCamera camera) {
		renderMapLayer(camera, FOREGROUND);
	}

	public void renderMapLayer(OrthographicCamera camera, int[] layerNum) {
		_tiledMapRenderer.setView(camera);
		_tiledMapRenderer.render(layerNum);
	}

	public void switchVisibleLayer(int layerNum) {
		MapLayer layer = getLayer(layerNum);
		layer.setVisible(!layer.isVisible());
	}

	public MapLayer getLayer(int n) {
		return _tiledMap.getLayers().get(n);
	}

	public int getWidth() {
		return _mapPixelWidth;
	}

	public int getHeight() {
		return _mapPixelHeight;
	}

	public void dispose() {
		_tiledMap.dispose();
		_tiledMapRenderer.dispose();
	}
}
