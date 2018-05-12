package com.kag.pathfinder;


import com.kag.common.map.Node;
import com.kag.common.map.World;
import java.util.*;

public class AStar extends AbstractPathFinder {

	private PriorityQueue<EvaluatedNode> queue;

	public Node findPath(int startX, int startY, int endX, int endY, World world) {
		EvaluatedNode goalNode = new EvaluatedNode(world.getGameMap().getTile(startX, startY));

		Map<EvaluatedNode, Integer> costMap = new HashMap<>();

		queue = new PriorityQueue<>();
		EvaluatedNode endNode = new EvaluatedNode(world.getGameMap().getTile(endX, endY));
		endNode.setfValue(0);
		endNode.setgValue(0);
		queue.add(endNode);
		//TODO: Should end node be added to costMap

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

	private int heuristic(EvaluatedNode goal, EvaluatedNode current) {
		return Math.abs(goal.getTile().getX() - current.getTile().getX()) + Math.abs(goal.getTile().getY() - current.getTile().getY());
	}

}
