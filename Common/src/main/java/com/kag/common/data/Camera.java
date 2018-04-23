package com.kag.common.data;

/**
 * A representation of the player's view of the game world. The player can view various parts of the game world by
 * moving a camera around. The camera does not actually exists, but it is an abstraction of moving around the game world
 * to create the illusion of a camera.
 * <p>
 * The position of the camera maps to the center of the game window. Thus, if the camera's position is (0,0), all
 * entities with a world space coordinate of (0,0) will be rendered in the center of the game window. If the camera's
 * position is (100, 50), all entities with a world space coordinate of (0,0) will be rendered 100 pixels to the left
 * and 50 pixels above the center of the game window.
 */
public class Camera {

	private float x;
	private float y;

	/**
	 * Get the horizontal position of this camera.
	 *
	 * @return the horizontal position of this camera
	 */
	public float getX() {
		return x;
	}

	/**
	 * Set the horizontal position of this camera.
	 *
	 * @param x the new horizontal position of this camera
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Get the vertical position of this camera.
	 *
	 * @return the vertical position of this camera
	 */
	public float getY() {
		return y;
	}

	/**
	 * Set the vertical position of this camera.
	 *
	 * @param y the new vertical position of this camera
	 */
	public void setY(float y) {
		this.y = y;
	}
}
