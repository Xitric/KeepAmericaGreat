package com.kag.common.spinterfaces;

import com.kag.common.map.GameMap;


/**
 * Interface describing a service for generating world maps.
 *
 */
public interface IMapGenerator {

	/**
	 * Generate a random world map with the specified width and height in tiles.
	 * This method has no side effects and thus no pre- or post-conditions.
	 * Implementations of this method are entirely free to define the topography
	 * of the generated maps.
	 * <ul>
	 * <li>Pre-conditions: None</li>
	 * <li>Post-conditions: None</li>
	 * </ul>
	 *
	 * @param width  the width of the game map in tiles
	 * @param height the height of the game map in tiles
	 * @return the generated world map
	 */
	GameMap generateMap(int width, int height);
}
