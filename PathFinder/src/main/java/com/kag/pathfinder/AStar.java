package com.kag.pathfinder;

import com.kag.common.data.Node;
import com.kag.common.data.Tile;
import com.kag.common.data.World;

import java.util.*;

public class AStar {

	PriorityQueue<EvaluatedNode> queue;

	public Node findPath(int startX, int startY, int endX, int endY, World world) {
		EvaluatedNode goalNode = new EvaluatedNode(world.getGameMap().getTile(startX, startY));

		Map<EvaluatedNode, Integer> costMap = new HashMap<>();

		queue = new PriorityQueue<>();
		EvaluatedNode endNode = new EvaluatedNode(world.getGameMap().getTile(endX, endY));
		endNode.setfValue(0);
		endNode.setgValue(0);
		queue.add(endNode);

		while (!queue.isEmpty()) {
			EvaluatedNode current = queue.poll();

			if (current.equals(goalNode)) {
				return current;
			}

			for (EvaluatedNode neighbor : getNeighborsFromTile(current, world)) {
				int newCost = current.getgValue() + 1;
				neighbor.setgValue(newCost);

				if (!costMap.containsKey(neighbor) || newCost < costMap.get(neighbor)) {
					costMap.put(neighbor, newCost);
					int fValue = newCost + heuristic(goalNode, neighbor);
					neighbor.setfValue(fValue);
					queue.add(neighbor);
					neighbor.setNext(current);
				}
			}
		}

		return null;
	}

	private boolean validateCoordinates(World world, int x, int y) {
		if (x < 0 || x >= world.getGameMap().getWidth() || y < 0 || y >= world.getGameMap().getHeight()) {
			return false;
		}

		return world.isWalkable(x, y);
	}

	private Collection<EvaluatedNode> getNeighborsFromTile(EvaluatedNode current, World world) {
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

	private int heuristic(EvaluatedNode goal, EvaluatedNode current) {
		return Math.abs(goal.getTile().getX() - current.getTile().getX()) + Math.abs(goal.getTile().getY() - current.getTile().getY());
	}

}
