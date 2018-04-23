package com.kag.towercontroller;

import com.kag.common.data.GameData;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.map.Node;
import com.kag.common.map.Tile;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IPathFinder;
import com.kag.commonplayer.entities.parts.PlayerPart;
import com.kag.commontd.entities.parts.MoneyPart;
import org.openide.util.Lookup;

public class TowerBuilder {

	private static final Family PLAYER_FAMILY = Family.forAll(PlayerPart.class, MoneyPart.class);

	public boolean tryPlaceTower(Entity towerPreview, World world, GameData gameData) {
		int tileX = (int) ((gameData.getCamera().getX() - gameData.getWidth() / 2 + gameData.getMouse().getX()) / world.getGameMap().getTileWidth());
		int tileY = (int) ((gameData.getCamera().getY() - gameData.getHeight() / 2 + gameData.getMouse().getY()) / world.getGameMap().getTileHeight());

		//Cannot place tower outside of the map
		if (tileX < 0 || tileX >= world.getGameMap().getWidth() ||
				tileY < 0 || tileY >= world.getGameMap().getHeight()) {
			return false;
		}

		//Cannot place tower on occupied tiles
		if (world.isOccupied(tileX, tileY)) {
			return false;
		}

		//Cannot place tower without sufficient funds
		Entity player = world.getEntitiesByFamily(PLAYER_FAMILY).stream().findFirst().orElse(null);
		Entity tower = towerPreview.getPart(TowerBuyMenuPart.class).getTowerFactory().create();
		if (player == null || !canAffordTower(player, tower)) {
			return false;
		}

		//At this point we take money from the player, regardless of whether the tower can be placed, as we do
		//additional checks with path finding
		buyTower(player, tower);
		placeTowerOnGameMap(world, tower, tileX, tileY);
		return true;
	}

	private boolean canAffordTower(Entity player, Entity tower) {
		int playerMoney = player.getPart(MoneyPart.class).getMoney();
		int cost = tower.getPart(MoneyPart.class).getMoney();

		return cost <= playerMoney;
	}

	private void buyTower(Entity player, Entity tower) {
		MoneyPart playerCurrencyPart = player.getPart(MoneyPart.class);

		int playerMoney = playerCurrencyPart.getMoney();
		int cost = tower.getPart(MoneyPart.class).getMoney();

		playerCurrencyPart.setMoney(playerMoney - cost);
	}

	private void placeTowerOnGameMap(World world, Entity tower, int tileX, int tileY) {
		Tile hoverTile = world.getGameMap().getTile(tileX, tileY);
		tower.getPart(PositionPart.class).setPos(tileX * world.getGameMap().getTileWidth() + world.getGameMap().getTileWidth() / 2,
				tileY * world.getGameMap().getTileHeight() + world.getGameMap().getTileHeight() / 2);

		// Running Dijkstra pf to get a count of nodes available for start pos, to know how many start positions are removed after tower placement
		Node[][] validPaths = getPaths(world);
		int validPathsCount = validPaths == null ? 0 : countValidPaths(validPaths);

		world.addEntity(tower);
		hoverTile.setWalkable(false);

		if (!isMapValid(world, validPathsCount, validPaths)) {
			world.removeEntity(tower);
			hoverTile.setWalkable(true);
		} else {
			Lookup.getDefault().lookup(TowerService.class).towerCreated(tower);
		}
	}

	private Node[][] getPaths(World world) {
		IPathFinder pathFinder = Lookup.getDefault().lookup(IPathFinder.class);
		if (pathFinder != null) {
			return pathFinder.getPath(world.getGameMap().getPlayerX(), world.getGameMap().getPlayerY(), world);
		}

		return null;
	}

	private int countValidPaths(Node[][] paths) {
		int count = 0;

		for (Node[] path : paths) {
			for (Node aPath : path) {
				if (aPath != null) {
					count++;
				}
			}
		}

		return count;
	}

	private boolean isMapValid(World world, int prevValidPathsCount, Node[][] prevValidPaths) {
    	/*
    	Run Dijkstra.
    	If more than one possible path to the goal is removed, an area is blocked off.
    	Validate that there are no enemies by checking the blocked off tiles for whether they are walkable and occupied,
    	if that's the case, there are enemies on the blocked off tiles and the map is invalid.
    	 */
		IPathFinder pathFinder = Lookup.getDefault().lookup(IPathFinder.class);
		if (pathFinder == null) return true;

		// Check whether there's a valid path from spawn to goal (the player)
		if (pathFinder.getPath(0, 0, world.getGameMap().getPlayerX(), world.getGameMap().getPlayerY(), world) == null) {
			return false;
		}

		Node[][] validPaths = getPaths(world);
		if (validPaths == null) return true;

		// Check whether more than one valid path was removed
		if (prevValidPathsCount - countValidPaths(validPaths) > 1) {
			for (int y = 0; y < validPaths.length; y++) {
				for (int x = 0; x < validPaths[y].length; x++) {
					if (prevValidPaths[y][x] != null && validPaths[y][x] == null && world.isWalkable(x, y) && world.isOccupied(x, y)) {
						return false;
					}
				}
			}
		}

		return true;
	}
}
