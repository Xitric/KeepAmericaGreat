package com.kag.common.entities.parts;

import com.kag.common.entities.IPart;
import com.kag.common.map.Tile;

/**
 * A part describing the contents of a two-dimensional tile map. Conceptually, a tile map is a two-dimensional array of
 * objects, each describing the contents of a cell (also known as a tile) in a grid based map.
 */
public class TileMapPart implements IPart {

	private final Tile[][] tiles;
	private int tileWidth, tileHeight;

	/**
	 * Constructs a new tile map part with the specified width and height in tiles, and with each tile having the
	 * specified dimensions, in pixels.
	 *
	 * @param width      the number of tiles in the map along the horizontal axis
	 * @param height     the number of tiles in the map along the vertical axis
	 * @param tileWidth  the width of a tile, in pixels
	 * @param tileHeight the height of a tile, in pixels
	 */
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

	/**
	 * Get the object representing a tile at the specified position. This position is the index of the tile along the
	 * horizontal and vertical axes, not its pixels coordinates.
	 *
	 * @param x the tile coordinate along the horizontal axis
	 * @param y the tile coordinate along the vertical axis
	 * @return the tile at the specified position
	 */
	public Tile getTile(int x, int y) {
		return tiles[y][x];
	}

	/**
	 * Get the number of tiles along the horizontal axis.
	 *
	 * @return the number of tiles along the horizontal axis
	 */
	public int getWidth() {
		return tiles.length == 0 ? 0 : tiles[0].length;
	}

	/**
	 * Get the number of tiles along the vertical axis.
	 *
	 * @return the number of tiles along the vertical axis
	 */
	public int getHeight() {
		return tiles.length;
	}

	/**
	 * Get the width of a tile, in pixels, in this tile map.
	 *
	 * @return the width of a tile, in pixels
	 */
	public int getTileWidth() {
		return tileWidth;
	}

	/**
	 * Set the width of all tiles, in pixels.
	 *
	 * @param tileWidth the new width of tiles
	 */
	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	/**
	 * Get the height of a tile, in pixels, in this tile map.
	 *
	 * @return the height of a tile, in pixels
	 */
	public int getTileHeight() {
		return tileHeight;
	}

	/**
	 * Set the height of all tiles, in pixels.
	 *
	 * @param tileHeight the new height of tiles
	 */
	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}
}
