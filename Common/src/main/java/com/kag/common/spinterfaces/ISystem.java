/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.spinterfaces;

import com.kag.common.data.GameData;
import com.kag.common.data.World;

/**
 * A system for performing some logic once every iteration of the game loop. Use
 * this interface when perfoming logic that is not entity specific.
 *
 * @author andre
 */
public interface ISystem extends IPrioritizable {

	/**
	 * This method is called once every iteration of the game loop. The
	 * implementation is free to make any changes to the provided world object.
	 * <ul>
	 * <li>Pre-conditions: The game loop must not be paused, and the game must
	 * be initialized to an environment that allows for rendering graphics to
	 * the screen.</li>
	 * <li>Post-conditions: None</li>
	 * </ul>
	 *
	 * @param dt    the time in milliseconds since the last iteration of the
	 *              game loop. This is the time span that must be simulated by
	 *              this method invocation
	 * @param world the game world
	 */
	void update(float dt, World world, GameData gameData);
}
