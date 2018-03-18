/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.spinterfaces;

import com.kag.common.entities.Entity;

/**
 * Interface describing a service for testing collisions between entities.
 *
 * @author andre
 */
public interface ICollision {

	/**
	 * Test if a collision has occurred between the specified entities. A
	 * collision occurs when any point is within both of the areas covered by
	 * the entities. A point at the edge of an entity is also considered to be
	 * inside this area.
	 * <ul>
	 * <li>Pre-conditions: The entities must be present in the game world.
	 * Furthermore, both entities must have at least a
	 * {@link com.kag.common.entities.parts.PositionPart} and a
	 * {@link com.kag.common.entities.parts.BoundingBoxPart}.</li>
	 * <li>Post-conditions: None</li>
	 * </ul>
	 *
	 * @param a the first entity to test for
	 * @param b the second entity to test for
	 * @return true if the entities collide, false otherwise
	 */
	boolean doesCollide(Entity a, Entity b);
}
