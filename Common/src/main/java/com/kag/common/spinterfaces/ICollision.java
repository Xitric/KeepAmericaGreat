package com.kag.common.spinterfaces;

import com.kag.common.entities.Entity;

/**
 * Interface describing a service for testing collisions between entities. Implementations are free to determine how
 * precisely to check collisions, ranging from simple axis aligned bounding box testing to uses of the separating axis
 * theorem and even more complex techniques. The degree of precision can potentially have an impact on the game play, so
 * the user must be sure to install the collision checking service provider that is most appropriate for the type of
 * game being played.
 */
public interface ICollision {

	/**
	 * Test if a collision has occurred between the specified entities. A
	 * collision occurs when any point is within both of the areas covered by
	 * the entities. A point aon the edge of an entity is not considered to be
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
