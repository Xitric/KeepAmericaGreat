package com.kag.common.entities.parts;

import com.kag.common.entities.IPart;

/**
 * A part describing a position in world space coordinates. This position is relative to the top left corner of the game
 * world, and it will be affected by the game camera when it is moved around.
 */
public class PositionPart implements IPart {

	private float x;
	private float y;

	/**
	 * The orientation in degrees from 0 to 360. A rotation of 0 indicates that the entity "looks" to the right.
	 */
	private float rotation;

	/**
	 * Constructs a new position part with an initial position.
	 *
	 * @param x the initial horizontal position, in pixels
	 * @param y the initial vertical position, in pixels
	 */
	public PositionPart(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructs a new position part with an initial position and a rotation. This rotation is around the origin of the
	 * entity that owns this part and not around the origin of the game world.
	 *
	 * @param x        the initial horizontal position, in pixels
	 * @param y        the initial vertical position, in pixels
	 * @param rotation the rotation i degrees around the origin of the owning entity
	 */
	public PositionPart(float x, float y, int rotation) {
		this(x, y);
		this.rotation = rotation % 360;
	}

	/**
	 * Get the horizontal position in pixels.
	 *
	 * @return the horizontal position, in pixels
	 */
	public float getX() {
		return x;
	}

	/**
	 * Get the vertical position in pixels.
	 *
	 * @return the vertical position, in pixels
	 */
	public float getY() {
		return y;
	}

	/**
	 * Set the position of this part.
	 *
	 * @param x the horizontal position, in pixels
	 * @param y the vertical position, in pixels
	 */
	public final void setPos(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Get the rotation of this part in degrees. The rotation will always be normalized to the range [0; 360[.
	 *
	 * @return the rotation, in degrees
	 */
	public float getRotation() {
		return rotation;
	}

	/**
	 * Set the rotation of this part in degrees.
	 *
	 * @param rotation the rotation, in degrees
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation % 360;
	}
}
