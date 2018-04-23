package com.kag.common.spinterfaces;

/**
 * Interface describing a service that can be used to change the state of the game. Plugins for this game can use this
 * interface to send notifications to the game loop, such as when the game must be restarted.
 */
public interface IGame {

	/**
	 * Notify the game loop that a new game must be started. This can happen in the middle of an update cycle. In this
	 * case, no systems are skipped. Instead, the remaining systems are updated in the new game state.
	 * <ul>
	 * <li>Pre-conditions: The game engine must be initialized.</li>
	 * <li>Post-conditions: The game state has been reset, meaning that a new map has been generated and all
	 * state specific entities have been removed from the game world. Entities that are not tied to a specific state
	 * remain. Furthermore, all implementations of {@link IGameStateListener} haven been informed that a new game has
	 * started.</li>
	 * </ul>
	 */
	void startNewGame();
}
