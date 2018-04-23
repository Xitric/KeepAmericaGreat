package com.kag.common.spinterfaces;

import com.kag.common.data.GameData;
import com.kag.common.map.World;

/**
 * A system for performing some logic once every iteration of the game loop. Use
 * this interface when performing logic that is not entity specific.
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
	 * @param dt       the time in milliseconds since the last iteration of the
	 *                 game loop. This is the time span that must be simulated by
	 *                 this method invocation
	 * @param world    the game world
	 * @param gameData container for input devices and window information
	 */
	void update(float dt, World world, GameData gameData);
}
