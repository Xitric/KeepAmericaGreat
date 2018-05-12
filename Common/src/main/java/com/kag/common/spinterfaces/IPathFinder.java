package com.kag.common.spinterfaces;

import com.kag.common.map.Node;
import com.kag.common.map.World;

/**
 * Interface describing a service for performing path finding within the world map. Service providers must allow for
 * generating both a single path between two locations in the world map, as well as paths from all locations in the
 * world map towards a single goal. Service providers are free to determine what path finding algorithms to use,
 * although the algorithms must be both complete and optimal.
 */
public interface IPathFinder {

	/**
	 * Generate a path from the specified start coordinates to the end
	 * coordinates in the world map. This method is ideal for efficiently
	 * generating a single path. If path finding must be performed for a large
	 * number of starting positions but with a single goal, consider
	 * {@link IPathFinder#getPath(int, int, World)} instead.
	 * <ul>
	 * <li>Pre-conditions: The world must be generated and populated with valid
	 * terrain.</li>
	 * <li>Post-conditions: None</li>
	 * </ul>
	 *
	 * @param startX the horizontal start position of the path in tile
	 *               coordinates
	 * @param startY the vertical start position of the path in tile coordinates
	 * @param endX   the horizontal end position of the path in tile coordinates
	 * @param endY   the vertical end position of the path in tile coordinates
	 * @param world  the game world
	 * @return the first node in the generated path. Each node contains
	 * information about the next node in the path
	 */
	Node getPath(int startX, int startY, int endX, int endY, World world);

	/**
	 * Generate all possible paths in the game world to the specified end
	 * coordinates. This method is ideal for efficiently generating multiple
	 * paths to the same goal. If only a small number of paths or paths with
	 * different goals are needed, consider
	 * {@link IPathFinder#getPath(int, int, int, int, World)}
	 * instead.
	 * <ul>
	 * <li>Pre-conditions: The world must be generated and populated with valid
	 * terrain.</li>
	 * <li>Post-conditions: None</li>
	 * </ul>
	 *
	 * @param endX  the horizontal end position of the path in tile coordinates
	 * @param endY  the vertical end position of the path in tile coordinates
	 * @param world the game world
	 * @return a two dimensional array of nodes, where each node is the first
	 * node in the path from that position to the goal
	 */
	Node[][] getPath(int endX, int endY, World world);
}
