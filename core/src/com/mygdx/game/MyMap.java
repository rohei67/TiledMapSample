package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.ArrayList;
import java.util.HashMap;

public class MyMap {
	TiledMap tiledMap;
	OrthogonalTiledMapRenderer tiledMapRenderer;
	int mapPixelWidth;
	int mapPixelHeight;

	java.util.Map<String, TiledMapTile> waterTiles;
	ArrayList<TiledMapTileLayer.Cell> waterCellList;
	float elapsedSinceAnimation = 0.0f;

	public MyMap() {
		tiledMap = new TmxMapLoader().load("MyCrappyMap.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

		MapProperties prop = tiledMap.getProperties();
		int mapWidth = prop.get("width", Integer.class);
		int mapHeight = prop.get("height", Integer.class);
		int tilePixelWidth = prop.get("tilewidth", Integer.class);
		int tilePixelHeight = prop.get("tileheight", Integer.class);

		mapPixelWidth = mapWidth * tilePixelWidth;
		mapPixelHeight = mapHeight * tilePixelHeight;

		// 水面タイルマップの読み込み
		readWaterTileSet();
		extractWaterMapCells();
	}

	private void readWaterTileSet() {
		waterTiles = new HashMap<String, TiledMapTile>();

		TiledMapTileSet tileset = tiledMap.getTileSets().getTileSet("Water");

		for (TiledMapTile tile : tileset) {
			Object property = tile.getProperties().get("WaterFrame");
			if (property != null)
				waterTiles.put((String) property, tile);
		}
	}

	private void extractWaterMapCells() {
		waterCellList = new ArrayList<TiledMapTileLayer.Cell>();
		TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);

		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				TiledMapTileLayer.Cell cell = layer.getCell(x, y);

				Object property = cell.getTile().getProperties().get("WaterFrame");
				if (property != null) {
					waterCellList.add(cell);
				}
			}
		}
	}

	public void render(OrthographicCamera camera) {
		waterMapTileAnimation();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
	}

	private void waterMapTileAnimation() {
		elapsedSinceAnimation += Gdx.graphics.getDeltaTime();
		if (elapsedSinceAnimation > 0.5f) {
			updateWaterAnimations();
			elapsedSinceAnimation = 0.0f;
		}
	}

	public void renderBackground(OrthographicCamera camera) {
		waterMapTileAnimation();
		renderMapLayer(camera, "Background");
	}

	public void renderForeground(OrthographicCamera camera) {
		renderMapLayer(camera, "Foreground");
	}

	public void renderMapLayer(OrthographicCamera camera, String layerName) {
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.getBatch().begin();
		TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(layerName);
		if (layer != null)
			tiledMapRenderer.renderTileLayer(layer);
		tiledMapRenderer.getBatch().end();
	}

	private void updateWaterAnimations() {
		for (TiledMapTileLayer.Cell cell : waterCellList) {
			String property = (String) cell.getTile().getProperties().get("WaterFrame");
			Integer currentAnimationFrame = Integer.parseInt(property);

			currentAnimationFrame++;

			if (currentAnimationFrame > waterTiles.size())
				currentAnimationFrame = 1;
			TiledMapTile newTile = waterTiles.get(currentAnimationFrame.toString());

			cell.setTile(newTile);
		}
	}

	public void switchVisibleLayer(int layerNum) {
		MapLayer layer = getLayer(layerNum);
		layer.setVisible(!layer.isVisible());
	}

	public MapLayer getLayer(int n) {
		return tiledMap.getLayers().get(n);
	}

	public int getWidth() {
		return mapPixelWidth;
	}

	public int getHeight() {
		return mapPixelHeight;
	}

	public void dispose() {
		tiledMap.dispose();
		tiledMapRenderer.dispose();
	}
}
