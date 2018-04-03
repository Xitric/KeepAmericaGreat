package com.kag.common.data;

import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.BlockingPart;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.spinterfaces.ICollision;
import org.openide.util.Lookup;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sofie Jørgensen
 */
public class World {

	private Collection<Entity> entities;
	private GameMap gameMap;

	public World(GameMap gameMap) {
		entities = new ArrayList<>();
		this.gameMap = gameMap;
		addEntity(gameMap.getTileEntity());
	}

	/**
	 * isWalkable considers only Tile.isWalkable()
	 *
	 * @param x The x-coordinate for the tile to check upon
	 * @param y The y-coordinate for the tile to check upon
	 * @return true if Tile.isWalkable is true, false otherwise
	 */
	public boolean isWalkable(int x, int y) {
		return gameMap.getTile(x, y).isWalkable();
	}

	/**
	 * isOccupied considers all entities with the BlockingPart and
	 * Tile.isWalkable()
	 *
	 * @param x The x-coordinate for the tile to check upon
	 * @param y The y-coordinate for the tile to check upon
	 * @return true if Tile.isWalkable is false or if there's an entity on x,y
	 * with a BlockingPart
	 */
	public boolean isOccupied(int x, int y) {
		if (!isWalkable(x, y)) {
			return true;
		}

		//Test if an entity is over the tile
		//Making a dummy entity for the tile allows us to use the collision module
		Entity tile = new Entity();
		tile.addPart(new PositionPart(x * gameMap.getTileWidth() + gameMap.getTileWidth() / 2,
				y * gameMap.getTileHeight() + gameMap.getTileHeight() / 2));
		tile.addPart(new BoundingBoxPart(gameMap.getTileWidth(), gameMap.getHeight()));

		ICollision collision = Lookup.getDefault().lookup(ICollision.class);

		for (Entity entity : entities) {
			if (entity.hasPart(BlockingPart.class) && entity.hasPart(PositionPart.class)) {
				if (collision.doesCollide(entity, tile)) {
					return true;
				}
			}
		}

		return false;
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}

	public Collection<Entity> getAllEntities() {
		return new ArrayList<>(entities);
	}

	public Entity getEntityAt(int screenX, int screenY) {
		return null;
	}

	public GameMap getGameMap() {
		return gameMap;
	}
}
