package com.kag.pathfinder;

import com.kag.common.map.Node;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IPathFinder;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IPathFinder.class)
public class PathFinder implements IPathFinder {

    @Override
    public Node getPath(int startX, int startY, int endX, int endY, World world) {
        AStar aStar = new AStar();
        return aStar.findPath(startX, startY, endX, endY, world);
    }

    @Override
    public Node[][] getPath(int endX, int endY, World world) {
		Dijkstra dijkstra = new Dijkstra();
		return dijkstra.constructNodeMap(endX, endY, world);
    }
}
