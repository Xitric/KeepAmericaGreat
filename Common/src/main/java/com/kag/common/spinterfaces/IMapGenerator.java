/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.spinterfaces;

import com.kag.common.data.GameMap;

/**
 * Interface describing a service for generating world maps.
 *
 * @author andre
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
