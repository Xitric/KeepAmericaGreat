package com.kag.common.spinterfaces;

import com.kag.common.map.World;

/**
 * Interface describing a service that must be informed when a new game is started. This interface can be implemented by
 * services that must remove entities from the game world on a restart.
 */
public interface IGameStateListener {

	/**
	 * Called when the game engine is about to start a new game. This method is called before a new game map has been
	 * generated, and the provided world instance represents the current state of the game, before the restart. This
	 * method is not called the first time the game is started, but only when the game is restarted while running.
	 * <ul>
	 * <li>Pre-conditions: The game engine and game world must be initialized.</li>
	 * <li>Post-conditions: The service provider has removed all entities from the world that are tied to a specific
	 * state and which are managed by this service.</li>
	 * </ul>
	 *
	 * @param world the state of the game world before the restart
	 */
	void newGame(World world);
}
