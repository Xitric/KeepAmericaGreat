package com.kag.common.data;

/**
 *
 * @author Sofie Jørgensen
 */
public class GameMap {

	private IAsset spriteSheet;
	private Tile[][] tiles;
	private Node[][] pathNodes;

	public GameMap(int width, int height) {
		this.tiles = new Tile[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tiles[y][x] = new Tile(x, y);
			}
		}
	}

	public Tile getTile(int x, int y) {
		return tiles[y][x];
	}

	public Node[][] getPathNodes() {
		return pathNodes;
	}

	public void setPathNodes(Node[][] pathNodes) {
		this.pathNodes = pathNodes;
	}

	/**
	 * @return the spriteSheet
	 */
	public IAsset getSpriteSheet() {
		return spriteSheet;
	}

	/**
	 * @param spriteSheet the spriteSheet to set
	 */
	public void setSpriteSheet(IAsset spriteSheet) {
		this.spriteSheet = spriteSheet;
	}

	public void recalculatePathNodeMap() {

	}

	public int getHeight() {
		return tiles.length;
	}

	public int getWidth() {
		return tiles.length == 0 ? 0 : tiles[0].length;
	}
}
