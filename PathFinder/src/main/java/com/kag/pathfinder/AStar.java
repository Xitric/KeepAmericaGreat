package com.kag.pathfinder;

import com.kag.common.data.Node;
import com.kag.common.data.World;

import java.util.PriorityQueue;

public class AStar {
    PriorityQueue<EvaluatedNode> queue;

    public Node findPath(int startX, int startY, int endX, int endY, World world) {
        EvaluatedNode goalNode = new EvaluatedNode(world.getGameMap().getTile(startX, startY));

        queue = new PriorityQueue<>();
        EvaluatedNode endNode = new EvaluatedNode(world.getGameMap().getTile(endX, endY));
        endNode.setfValue(0);
        endNode.setgValue(0);
        queue.add(endNode);

        while (!queue.isEmpty()){
            EvaluatedNode current = queue.poll();

            if (current.equals(goalNode)){
                break;
            }

            for(Node neighbor : getNeighborsFromTile(current, world)){

            }
        }

        return null;
    }

    private Node[] getNeighborsFromTile(EvaluatedNode current, World world) {
        
        return new Node[0];
    }

}
