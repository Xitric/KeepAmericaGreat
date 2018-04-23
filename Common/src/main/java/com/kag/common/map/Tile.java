package com.kag.common.map;

/**
 * A class used to represent the contents of a cell in a grid based map. A tile consists of a number of layers, each of
 * which can contain a reference to an index in a sprite sheet that is used to render the tile to the screen. A tile
 * also stores information regarding the ability for entities to walk on it.
 */
public class Tile {

	private final int x;
	private final int y;
	private final int[] layers;
	private boolean walkable;
	private boolean occupied;

	/**
	 * Constructs a new tile with the specified position in tile coordinates. This position must correspond to the
	 * position of the tile in the game map.
	 *
	 * @param x the horizontal position of the tile, in tile coordinates
	 * @param y the vertical position of the tile, in tile coordinates
	 */
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
		this.layers = new int[]{-1, -1};
		walkable = true;
		occupied = false;
	}

	/**
	 * Get the horizontal position of this tile, in tile coordinates.
	 *
	 * @return the horizontal position of this tile
	 */
	public int getX() {
		return x;
	}

	/**
	 * Get the vertical position of this tile, in tile coordinates.
	 *
	 * @return the vertical position of this tile
	 */
	public int getY() {
		return y;
	}

	/**
	 * Get the sprite index for the specified layer. This index corresponds to a position in the sprite sheet used to
	 * render this tile to the screen.
	 *
	 * @param layer the layer to extract information about
	 * @return the sprite index for the specified layer
	 * @throws IndexOutOfBoundsException if the tile has no such layer
	 */
	public int getLayer(int layer) {
		if (layer < 0 || layer >= layers.length) {
			throw new IndexOutOfBoundsException("The given layer was out of bounds. Layer: " + layer + ", and length was: " + layers.length);
		}

		return layers[layer];
	}

	/**
	 * Set the sprite index for the specified layer. This index must correspond to a position in the sprite sheet used
	 * to render this tile to the screen.
	 *
	 * @param layer the layer to set the index for
	 * @param value the index into the sprite sheet
	 * @throws IndexOutOfBoundsException if the tile has no such layer
	 */
	public void setLayer(int layer, int value) {
		if (layer < 0 || layer >= layers.length) {
			throw new IndexOutOfBoundsException("The given layer was out of bounds. Layer: " + layer + ", and length was: " + layers.length);
		}

		layers[layer] = value;
	}

	/**
	 * Test if this tile is walkable. If a tile is walkable, it means that entities that walk on the ground are allowed
	 * to move over it. For instance, a tile is not walkable if it contains obstacles such as water or rocks.
	 *
	 * @return true if entities can walk on this tile, false otherwise
	 */
	public boolean isWalkable() {
		return walkable;
	}

	/**
	 * Set whether this tile is walkable. If a tile is walkable, it means that entities that walk on the ground are
	 * allowed to move over it. When an obstacle is placed on a tile it should be set as not walkable, and when an
	 * obstacle is removed, the tile should be set to walkable.
	 *
	 * @param walkable true if entities should be able to walk on this tile, false otherwise
	 */
	public void setWalkable(boolean walkable) {
		this.walkable = walkable;
	}

	/**
	 * Test whether this tile is occupied. If a tile is occupied, it means that entities that require an entire tile for
	 * themselves cannot be placed on the tile. An occupied tile, however, does not necessarily prohibit entities from
	 * walking on it, as long as they can share the space with other entities.
	 * <p>
	 * This state can be set explicitly on a tile to simulate that it is constantly occupied by entities, however
	 * {@link World#isOccupied(int, int)} also considers the presence of entities when determining if a tile is
	 * occupied, so this value does not need to be maintained manually. Thus, if this method returns false, the tile is
	 * still considered to be occupied if it has entities on top of it that occupy their space as described in
	 * {@link World}.
	 *
	 * @return true if the tile is occupied, false otherwise
	 */
	public boolean isOccupied() { return occupied; }

	/**
	 * Set whether this tile is occupied. See {@link Tile#isOccupied()} for a more detailed explanation.
	 *
	 * @param occupied true if the tile is occupied, false otherwise
	 */
	public void setOccupied(boolean occupied) { this.occupied = occupied; }
}
