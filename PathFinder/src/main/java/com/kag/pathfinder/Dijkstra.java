package com.kag.pathfinder;

import com.kag.common.data.Node;
import com.kag.common.data.Tile;
import com.kag.common.data.World;

import java.util.*;

/**
 * @author Kasper
 */
public class Dijkstra {

	/**
	 * Perform a modified version of Dijkstra's algorithm (inspired by Uniform Cost Search) from the specified goal
	 * coordinate to all other tiles in the specified map. The result will be a two-dimensional array of linked
	 * {@link Node} elements providing the shortest path to the goal from any location.
	 *
	 * @param endX the x coordinate of the goal tile
	 * @param endY the y coordinate of the goal tile
	 * @return a two-dimensional array of linked Node elements
	 */
	public Node[][] constructNodeMap(int endX, int endY, World world) {
		Node[][] nodeMap = new Node[world.getGameMap().getHeight()][world.getGameMap().getWidth()];

		Queue<EvaluatedNode> open = new PriorityQueue<>(Comparator.comparingInt(EvaluatedNode::getgValue));
		Map<EvaluatedNode, Integer> costMap = new HashMap<>();

		EvaluatedNode endNode = new EvaluatedNode(world.getGameMap().getTile(endX, endY));
		endNode.setgValue(0);
		open.add(endNode);
		costMap.put(endNode, endNode.getgValue());

		while (!open.isEmpty()) {
			EvaluatedNode current = open.poll();

			for (EvaluatedNode neighbor : getNeighborsFromTile(current, world)) {
				neighbor.setgValue(current.getgValue() + 1);

				if (neighbor.getgValue() < costMap.getOrDefault(neighbor, Integer.MAX_VALUE)) {
					costMap.put(neighbor, neighbor.getgValue());
					neighbor.setNext(current);
					open.add(neighbor);

					nodeMap[neighbor.getTile().getY()][neighbor.getTile().getX()] = neighbor;
				}
			}
		}

		return nodeMap;
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
}
