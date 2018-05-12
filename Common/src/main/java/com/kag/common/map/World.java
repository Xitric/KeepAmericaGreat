package com.kag.common.map;

import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.BlockingPart;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.PositionPart;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class used to represent the state of the game, including the current game map as well as all active entities. All
 * entities in this world, an only those, will be used for updating the game each iteration of the game loop.
 */
public class World {

	private final Collection<Entity> entities;
	private GameMap gameMap;

	/**
	 * Constructs a new game world with the specified game map. Initially, this game world will contain only one entity,
	 * which is used to render the specified game map.
	 *
	 * @param gameMap the initial map to play in the game
	 */
	public World(GameMap gameMap) {
		entities = new ArrayList<>();
		this.gameMap = gameMap;
		addEntity(gameMap.getTileEntity());
	}

	/**
	 * Set the map to be played in the game. This will replace the currently active game map.
	 *
	 * @param gameMap the new map to play
	 */
	public void setMap(GameMap gameMap) {
		removeEntity(this.gameMap.getTileEntity());
		this.gameMap = gameMap;
		addEntity(gameMap.getTileEntity());
	}

	/**
	 * Test if the specified tile is walkable. See {@link Tile#isWalkable()} for a more detailed explanation.
	 *
	 * @param x the horizontal position of the tile to check, in tile coordinates
	 * @param y the vertical position of the tile to check, in tile coordinates
	 * @return true if entities can walk on the specified tile, false otherwise
	 */
	public boolean isWalkable(int x, int y) {
		return gameMap.getTile(x, y).isWalkable();
	}

	/**
	 * Test if the specified tile is occupied. If a tile is not walkable, it is considered to be occupied. If the tile
	 * has explicitly been marked as occupied as per {@link Tile#setOccupied(boolean)}, this method will return true.
	 * Otherwise it will consider all entities currently overlapping the tile. If any of those entities has a
	 * {@link BlockingPart}, the tile is also considered to be occupied. Otherwise this method will return false.
	 *
	 * @param x the horizontal position of the tile to check, in tile coordinates
	 * @param y the vertical position of the tile to check, in tile coordinates
	 * @return true if the tile is occupied, false otherwise
	 */
	public boolean isOccupied(int x, int y) {
		if (!isWalkable(x, y)) {
			return true;
		}

		if (gameMap.getTile(x, y).isOccupied()) {
			return true;
		}

		for (Entity entity : entities) {
			if (entity.hasPart(BlockingPart.class) && entity.hasPart(PositionPart.class) && entity.hasPart(BoundingBoxPart.class)) {
				if (gameMap.doesCollideWithTile(gameMap.getTile(x, y), entity)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Add an entity to this world. After the entity has been added, it will be used when updating the state of the game
	 * every iteration of the game loop.
	 *
	 * @param entity the entity to add
	 */
	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	/**
	 * Remove an entity from this world. After the entity has been removed, it will no longer be used when updating the
	 * state of the game.
	 *
	 * @param entity the entity to remove
	 */
	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}

	/**
	 * Get all entities in this world. This will return a copy of the internal representation, so changing this
	 * collection has no effect on the game world. Also, the returned collection is not updated when entities are added
	 * to or removed from the game world.
	 *
	 * @return a collection of all entities in the game world
	 */
	public Collection<Entity> getAllEntities() {
		return new ArrayList<>(entities);
	}

	/**
	 * Get all entities that match the specified family, as described in {@link Family#matches(BitSet)}.
	 *
	 * @param family the family to use for filtering entities
	 * @return all entities in the game world that match the specified family
	 */
	public List<Entity> getEntitiesByFamily(Family family) {
		return getAllEntities().stream().filter(entity -> family.matches(entity.getBits())).collect(Collectors.toList());
	}

	/**
	 * Get the first entity that is discovered at the specified position, in world space pixel coordinates. If there are
	 * multiple entities at the specified position, an arbitrary entity is returned. This method can only consider
	 * entities that have both a {@link BoundingBoxPart} and either a {@link PositionPart} or an
	 * {@link AbsolutePositionPart}.
	 *
	 * @param worldX the horizontal position to check, in pixels
	 * @param worldY the vertical position to check, in pixels
	 * @return the first entity that was discovered at the specified position
	 */
	public Entity getEntityAt(float worldX, float worldY) {
		Family targetFamily = Family.forAll(BoundingBoxPart.class).includingAny(PositionPart.class, AbsolutePositionPart.class);
		for (Entity entity : getEntitiesByFamily(targetFamily)) {
			PositionPart ePos;
			if (entity.hasPart(PositionPart.class)) {
				ePos = entity.getPart(PositionPart.class);
			} else {
				ePos = entity.getPart(AbsolutePositionPart.class);
			}
			BoundingBoxPart eBox = entity.getPart(BoundingBoxPart.class);

			float eX = ePos.getX();
			float eY = ePos.getY();
			int eW = eBox.getWidth();
			int eH = eBox.getHeight();

			if (worldX > eX - eW / 2 && worldX < eX + eW / 2 && worldY > eY - eH / 2 && worldY < eY + eH / 2) {
				return entity;
			}
		}
		return null;
	}

	/**
	 * Test if the specified entity is at the specified position. The supplied position may be in either world or screen
	 * space coordinates, so it is up to the caller of this method to ensure that the positioning of the entity is in
	 * the same space. This method takes the bounding box of the entity into consideration, so an entity may be spanning
	 * multiple point locations at once.
	 *
	 * @param entity the entity to test for
	 * @param x      the horizontal coordinate of the position to test against
	 * @param y      the vertical coordinate of the position to test against
	 * @return true if the entity is at the specified position
	 */
	public boolean isEntityAt(Entity entity, float x, float y) {
		BoundingBoxPart bbox = entity.getPart(BoundingBoxPart.class);
		PositionPart positionPart;
		if (entity.hasPart(PositionPart.class)) {
			positionPart = entity.getPart(PositionPart.class);
		} else {
			positionPart = entity.getPart(AbsolutePositionPart.class);
		}

		float left = positionPart.getX() - bbox.getWidth() / 2;
		float right = positionPart.getX() + bbox.getWidth() / 2;
		float top = positionPart.getY() - bbox.getHeight() / 2;
		float bottom = positionPart.getY() + bbox.getHeight() / 2;

		return x >= left && x <= right && y >= top && y <= bottom;
	}

	/**
	 * Get the tile at the specified position, in world space pixel coordinates. This method is useful when pixel
	 * coordinates must be converted into tile coordinates.
	 *
	 * @param worldX the horizontal position to check, in pixels
	 * @param worldY the vertical position to check, in pixels
	 * @return the tile at the specified pixel position, or null if the position was outside the bounds of the game map
	 */
	public Tile getTileAt(float worldX, float worldY) {
		int tX = (int) worldX / 64;
		int tY = (int) worldY / 64;

		if (tY > gameMap.getHeight() - 1 || tX > gameMap.getWidth() - 1) {
			return null;
		}
		return gameMap.getTile((int) worldX / 64, (int) worldY / 64);
	}

	/**
	 * Get the game map currently being played in this game.
	 *
	 * @return the current game map
	 */
	public GameMap getGameMap() {
		return gameMap;
	}
}
