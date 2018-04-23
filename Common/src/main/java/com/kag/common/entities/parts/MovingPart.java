package com.kag.common.entities.parts;

import com.kag.common.entities.IPart;

/**
 * A part describing an entity that can move in the world with a predefined speed. This part only describes the scalar
 * movement, and thus not the direction.
 */
public class MovingPart implements IPart {

	private float movementSpeed;

	/**
	 * Constructs a new movement speed part with the specified movement speed in pixels per second.
	 *
	 * @param movementSpeed the movement speed, in pixels per second
	 */
	public MovingPart(float movementSpeed) {
		this.movementSpeed = movementSpeed;
	}

	/**
	 * Get the movement speed in pixels per second.
	 *
	 * @return the movement speed, in pixels per second
	 */
	public float getMovementSpeed() {
		return movementSpeed;
	}

	/**
	 * Set the movement speed in pixels per second.
	 *
	 * @param movementSpeed the movement speed, in pixels per second
	 */
	public void setMovementSpeed(float movementSpeed) {
		this.movementSpeed = movementSpeed;
	}
}
