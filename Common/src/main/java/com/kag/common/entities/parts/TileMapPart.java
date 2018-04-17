package com.kag.common.entities.parts;

import com.kag.common.data.Tile;
import com.kag.common.entities.IPart;

/**
 * @author Kasper
 */
public class TileMapPart implements IPart {

	private final Tile[][] tiles;
	private int tileWidth, tileHeight;

	public TileMapPart(int width, int height, int tileWidth, int tileHeight) {
		this.tiles = new Tile[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tiles[y][x] = new Tile(x, y);
			}
		}

		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}

	public Tile getTile(int x, int y) {
		return tiles[y][x];
	}

	public int getHeight() {
		return tiles.length;
	}

	public int getWidth() {
		return tiles.length == 0 ? 0 : tiles[0].length;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}
}
