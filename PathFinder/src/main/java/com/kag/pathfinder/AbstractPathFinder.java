package com.kag.pathfinder;

import com.kag.common.map.Tile;
import com.kag.common.map.World;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractPathFinder {

	private boolean validateCoordinates(World world, int x, int y) {
		/*
		if (x < 0 || x >= world.getGameMap().getWidth() || y < 0 || y >= world.getGameMap().getHeight()) {
			return false;
		}

		return world.isWalkable(x, y);
		 */
		return !(x < 0 || x >= world.getGameMap().getWidth() || y < 0 || y >= world.getGameMap().getHeight()) && world.isWalkable(x, y);
	}

	Collection<EvaluatedNode> getNeighborsFromTile(EvaluatedNode current, World world) {
		List<EvaluatedNode> nodes = new ArrayList<>();

		Tile currentTile = current.getTile();

		// Left
		if (validateCoordinates(world, currentTile.getX() - 1, currentTile.getY())) {
			nodes.add(new EvaluatedNode(world.getGameMap().getTile(currentTile.getX() - 1, currentTile.getY())));
		}

		// Right
		if (validateCoordinates(world, currentTile.getX() + 1, currentTile.getY())) {
			nodes.add(new EvaluatedNode(world.getGameMap().getTile(currentTile.getX() + 1, currentTile.getY())));
		}

		// Up
		if (validateCoordinates(world, currentTile.getX(), currentTile.getY() - 1)) {
			nodes.add(new EvaluatedNode(world.getGameMap().getTile(currentTile.getX(), currentTile.getY() - 1)));
		}

		// Down
		if (validateCoordinates(world, currentTile.getX(), currentTile.getY() + 1)) {
			nodes.add(new EvaluatedNode(world.getGameMap().getTile(currentTile.getX(), currentTile.getY() + 1)));
		}

		return nodes;
	}
}
