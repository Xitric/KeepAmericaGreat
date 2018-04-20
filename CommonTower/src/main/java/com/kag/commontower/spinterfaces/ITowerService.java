package com.kag.commontower.spinterfaces;

import com.kag.common.entities.Entity;

/**
 * Service provider interface describing a service for managing events and queries regarding towers. This interface
 * allows for subscribing to events raised when actions are performed on towers, as well as querying the currently
 * selected tower etc.
 *
 * @author Kasper
 */
public interface ITowerService {

	Entity getSelectedTower();

	void towerCreated(Entity tower);

	void towerRemoved(Entity tower);

	void towerSelected(Entity tower);

	void towerDeselected(Entity tower);

	/**
	 * A listener that can subscribe to events raised when towers are either added to or removed from the game world.
	 */
	interface ITowerCreationListener {

		void towerCreated(Entity tower);

		void towerRemoved(Entity tower);
	}

	/**
	 * A listener that can subscribe to events raised when a tower in the game world is either selected or deselected.
	 */
	interface ITowerSelectionListener {

		void towerSelected(Entity tower);

		void towerDeselected(Entity tower);
	}
}
