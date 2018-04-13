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

	public boolean isEntityLeftPressed(GameData gameData, Entity entity){
		Mouse mouse = gameData.getMouse();
		int mouseX = mouse.getX();
		int mouseY = mouse.getY();

		float btnX = entity.getPart(AbsolutePositionPart.class).getX();
		float btnY = entity.getPart(AbsolutePositionPart.class).getY();

		float btnW = entity.getPart(BoundingBoxPart.class).getWidth();
		float btnH = entity.getPart(BoundingBoxPart.class).getHeight();

		return mouse.isButtonPressed(Mouse.BUTTON_LEFT) && mouseX > btnX && mouseX < btnX + btnW && mouseY > btnY && mouseY < btnY + btnH;
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
