package com.kag.common.data;

import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.entities.parts.TileMapPart;

/**
 * @author Sofie Jørgensen
 */
public class GameMap {

	private Entity tileEntity;
	private Node[][] pathNodes;
	private int playerX, playerY;

	public GameMap(int width, int height, int tileWidth, int tileHeight) {
		tileEntity = new Entity();

		TileMapPart tileMapPart = new TileMapPart(width, height, tileWidth, tileHeight);
		tileEntity.addPart(tileMapPart);
	}

	public Entity getTileEntity() {
		return tileEntity;
	}

	public Tile getTile(int x, int y) {
		return tileEntity.getPart(TileMapPart.class).getTile(x, y);
	}

	public Node[][] getPathNodes() {
		return pathNodes;
	}

	public void setPathNodes(Node[][] pathNodes) {
		this.pathNodes = pathNodes;
	}

	public void setSpriteSheet(AssetPart spriteSheet) {
		tileEntity.addPart(spriteSheet);
	}

	public void recalculatePathNodeMap() {

	}

	public int getWidth() {
		return tileEntity.getPart(TileMapPart.class).getWidth();
	}

	public int getHeight() {
		return tileEntity.getPart(TileMapPart.class).getHeight();
	}

	public int getTileWidth() {
		return tileEntity.getPart(TileMapPart.class).getTileWidth();
	}

	public int getTileHeight() {
		return tileEntity.getPart(TileMapPart.class).getTileHeight();
	}
	
	public int getPlayerX() {
		return playerX;
	}

	public void setPlayerX(int playerX) {
		this.playerX = playerX;
	}

	public int getPlayerY() {
		return playerY;
	}

	public void setPlayerY(int playerY) {
		this.playerY = playerY;
	}
}
