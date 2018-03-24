/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.spinterfaces;

import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;

/**
 * A system for processing entities that contain certain parts. An entity system
 * specifies the parts that it requires entities to have to be processed by the
 * system. Each entity system is called for every entity that satisfies the
 * requirements for every iteration of the game loop.
 *
 * @author andre
 */
public interface IEntitySystem extends IPrioritizable {

	/**
	 * Get the family describing the parts that entities must contain to be
	 * processed by this system. Entities must have at least these parts, but
	 * they are allowed to have more.
	 *
	 * @return family describing the parts that entities must contain to be
	 *         processed
	 */
	Family getFamily();

	/**
	 * Process an entity that matches the requirements for this system. This
	 * method is called for every entity that matches the system's family for
	 * every iteration of the game loop. This method is free to remove one or
	 * more of the parts that are part of the family, as the constraint on the
	 * parts only applies at the time of method invocation. This method is also
	 * free to remove the entity from the game world entirely.
	 * <ul>
	 * <li>Pre-conditions: The entity that is passed to this method must have at
	 * least the parts specified in the system's family, and the entity must be
	 * present in the game world. Furthermore, the game loop must not be paused,
	 * and the game must be initialized to an environment that allows for
	 * rendering graphics to the screen.</li>
	 * <li>Post-conditions: The entity has been processed.</li>
	 * </ul>
	 *
	 * @param delta  the time in milliseconds since the last iteration of the
	 *               game loop. This is the time span that must be simulated by
	 *               this method invocation
	 * @param entity the entity to process
	 * @param world  the game world
	 */
	void update(float delta, Entity entity, World world, GameData gameData);
}
