package com.kag.pathfinder;

import com.kag.common.map.Node;
import com.kag.common.map.World;

import java.util.*;

public class Dijkstra extends AbstractPathFinder {

	/**
	 * Perform a modified version of Dijkstra's algorithm (inspired by Uniform Cost Search) from the specified goal
	 * coordinate to all other tiles in the specified map. The result will be a two-dimensional array of linked
	 * {@link Node} elements providing the shortest path to the goal from any location.
	 *
	 * @param endX  the x coordinate of the goal tile
	 * @param endY  the y coordinate of the goal tile
	 * @param world the game world to perform path finding in
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
}
