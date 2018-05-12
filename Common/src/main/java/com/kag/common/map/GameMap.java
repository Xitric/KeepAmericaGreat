package com.kag.common.map;

import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.entities.parts.TileMapPart;
import com.kag.common.spinterfaces.ICollision;
import org.openide.util.Lookup;

/**
 * A class used to represent the map that is currently being played in the game. This class contains information
 * relevant for path finding, information about the size of the map and its contents, and information about the player
 * position.
 */
public class GameMap {

	private final Entity tileEntity;
	private Node[][] pathNodes;
	private int playerX, playerY;

	/**
	 * Constructs a new, empty game map with the specified width and height in tiles, and with each tile having the
	 * specified dimensions, in pixels.
	 *
	 * @param width      the number of tiles in the map along the horizontal axis
	 * @param height     the number of tiles in the map along the vertical axis
	 * @param tileWidth  the width of a tile, in pixels
	 * @param tileHeight the height of a tile, in pixels
	 */
	public GameMap(int width, int height, int tileWidth, int tileHeight) {
		tileEntity = new Entity();

		TileMapPart tileMapPart = new TileMapPart(width, height, tileWidth, tileHeight);
		tileEntity.addPart(tileMapPart);

		pathNodes = new Node[height][width];
	}

	/**
	 * Get the entity representing this game map. This entity is mainly used to inject the rendering of the game map
	 * into the entity framework system.
	 *
	 * @return the entity representing this game map
	 */
	public Entity getTileEntity() {
		return tileEntity;
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
		return tileEntity.getPart(TileMapPart.class).getTile(x, y);
	}

	/**
	 * Get a two dimensional array of {@link Node}s used for finding a path to the player position from an arbitrary
	 * position on the game map. There is a 1 to 1 correspondence between the indices in this two dimensional array and
	 * the tiles in the game world. To get the Node for the tile at position (x,y), call:
	 * <p>
	 * {@code getPathNodes()[y][x]}
	 * <p>
	 * If there is no valid path from a certain tile, the corresponding Node will be null.
	 *
	 * @return a two dimensional array of Nodes used for path finding
	 */
	public Node[][] getPathNodes() {
		return pathNodes;
	}

	/**
	 * Set the two dimensional array of Nodes to use for finding a path to the player position from an arbitrary
	 * position on the game map.
	 *
	 * @param pathNodes the two dimensional array of Nodes to use for path finding
	 */
	public void setPathNodes(Node[][] pathNodes) {
		this.pathNodes = pathNodes;
	}

	/**
	 * Get the number of tiles along the horizontal axis.
	 *
	 * @return the number of tiles along the horizontal axis
	 */
	public int getWidth() {
		return tileEntity.getPart(TileMapPart.class).getWidth();
	}

	/**
	 * Get the number of tiles along the vertical axis.
	 *
	 * @return the number of tiles along the vertical axis
	 */
	public int getHeight() {
		return tileEntity.getPart(TileMapPart.class).getHeight();
	}

	/**
	 * Get the width of a tile, in pixels, in this tile map.
	 *
	 * @return the width of a tile, in pixels
	 */
	public int getTileWidth() {
		return tileEntity.getPart(TileMapPart.class).getTileWidth();
	}

	/**
	 * Get the height of a tile, in pixels, in this tile map.
	 *
	 * @return the height of a tile, in pixels
	 */
	public int getTileHeight() {
		return tileEntity.getPart(TileMapPart.class).getTileHeight();
	}

	/**
	 * Test if the specified entity overlaps the specified tile. The entity must have both a {@link PositionPart} and a
	 * {@link BoundingBoxPart} for this test to succeed. This method also requires that an implementation of the
	 * {@link ICollision} interface is installed.
	 *
	 * @param tile   the tile to test against
	 * @param entity the entity to test collision for
	 * @return true if the entity overlaps the specified tile, false otherwise
	 */
	public boolean doesCollideWithTile(Tile tile, Entity entity) {
		ICollision collision = Lookup.getDefault().lookup(ICollision.class);
		if (collision == null) return false;

		Entity tileEnt = new Entity();
		tileEnt.addPart(new PositionPart(tile.getX() * getTileWidth() + getTileWidth() / 2,
				tile.getY() * getTileHeight() + getTileHeight() / 2));
		tileEnt.addPart(new BoundingBoxPart(getTileWidth(), getTileHeight()));

		return collision.doesCollide(entity, tileEnt);
	}

	/**
	 * Get the horizontal position of the player tower in the game map, in tile coordinates.
	 *
	 * @return the horizontal position of the player tower
	 */
	public int getPlayerX() {
		return playerX;
	}

	/**
	 * Set the horizontal position of the player tower in the game map, in tile coordinates.
	 *
	 * @param playerX the horizontal position of the player tower
	 */
	public void setPlayerX(int playerX) {
		this.playerX = playerX;
	}

	/**
	 * Get the vertical position of the player tower in the game map, in tile coordinates.
	 *
	 * @return the vertical position of the player tower
	 */
	public int getPlayerY() {
		return playerY;
	}

	/**
	 * Set the vertical position of the player tower in the game map, in tile coordinates.
	 *
	 * @param playerY the vertical position of the player tower
	 */
	public void setPlayerY(int playerY) {
		this.playerY = playerY;
	}
}
