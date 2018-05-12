package com.kag.common.entities.parts;

/**
 * A part describing an absolute position in screen space coordinates. This position is relative to the top left corner
 * of the game window, and it is not affected by the position of the game camera.
 */
public class AbsolutePositionPart extends PositionPart {

	/**
	 * Constructs a new absolute position part with an initial position.
	 *
	 * @param x the initial horizontal position, in pixels
	 * @param y the initial vertical position, in pixels
	 */
	public AbsolutePositionPart(float x, float y) {
		super(x, y);
	}

	/**
	 * Constructs a new absolute position part with an initial position and a rotation. This rotation is around the
	 * origin of the entity that owns this part and not around the origin of the game screen.
	 *
	 * @param x        the initial horizontal position, in pixels
	 * @param y        the initial vertical position, in pixels
	 * @param rotation the rotation i degrees around the origin of the owning entity
	 */
	public AbsolutePositionPart(float x, float y, int rotation) {
		super(x, y, rotation);
	}
}
