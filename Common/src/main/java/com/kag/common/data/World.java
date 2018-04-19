package com.kag.common.data;

import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.BlockingPart;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.PositionPart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sofie Jørgensen
 */
public class World {

	private final Collection<Entity> entities;
	private final GameMap gameMap;

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

		if (gameMap.getTile(x, y).isOccupied()) {
			return true;
		}

		for (Entity entity : entities) {
			if (entity.hasPart(BlockingPart.class) && entity.hasPart(PositionPart.class) && entity.hasPart(BoundingBoxPart.class)) {
				if(gameMap.doesCollideWithTile(gameMap.getTile(x,y), entity)) {
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
			int	eH = eBox.getHeight();

			if(worldX > eX-eW/2 && worldX < eX + eW/2 && worldY > eY-eH/2 && worldY < eY + eH/2){
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
	 * @param x the horizontal coordinate of the position to test against
	 * @param y the vertical coordinate of the position to test against
	 * @return true if the entity is at the specified position
	 */
	public boolean isEntityAt(Entity entity, float x, float y){
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

	public Tile getTileAt(float worldX, float worldY) {
		int tX = (int)worldX / 64;
		int tY = (int)worldY / 64;

		if(tY > gameMap.getHeight() - 1 || tX > gameMap.getWidth() - 1) {
			return null;
		}
		return gameMap.getTile((int)worldX / 64, (int)worldY / 64);
	}

	public List<Entity> getEntitiesByFamily(Family family) {
		return getAllEntities().stream().filter(entity -> family.matches(entity.getBits())).collect(Collectors.toList());
	}

	public GameMap getGameMap() {
		return gameMap;
	}
}
