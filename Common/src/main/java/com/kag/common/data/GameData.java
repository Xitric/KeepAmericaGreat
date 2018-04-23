package com.kag.common.data;

import com.kag.common.input.Keyboard;
import com.kag.common.input.Mouse;

/**
 * A class used to represent containers of information and object references related to the current game instance.
 * Instances of this class can be used to acquire references to the input mechanisms {@link Keyboard} and {@link Mouse},
 * as well as the game {@link Camera}. The class also stores information about the dimensions of the game window, as
 * well as the speed at which the game is currently running.
 */
public class GameData {

	private final Keyboard keyboard;
	private final Mouse mouse;
	private final int width;
	private final int height;
	private final Camera camera;
	private float speedMultiplier;

	public GameData(Keyboard keyboard, Mouse mouse, int width, int height, Camera camera) {
		this.keyboard = keyboard;
		this.mouse = mouse;
		this.width = width;
		this.height = height;
		this.camera = camera;
		this.speedMultiplier = 1;
	}

	/**
	 * Get the object representing an abstraction of the keyboard.
	 *
	 * @return object representing keyboard input
	 */
	public Keyboard getKeyboard() {
		return keyboard;
	}

	/**
	 * Get the object representing an abstraction of the mouse.
	 *
	 * @return object representing mouse input
	 */
	public Mouse getMouse() {
		return mouse;
	}

	/**
	 * Get the width of the game window, in pixels. This width is excluding the frame of the game window.
	 *
	 * @return the width of the game window, in pixels
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the height of the game window, in pixels. This height is excluding the frame of the game window.
	 *
	 * @return the height of the game window, in pixels
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get the object representing the camera that is used to move the player's view around the game world.
	 *
	 * @return the object representing the camera
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * Get the speed at which the game is running. This multiplier is applied to the delta value passed to all
	 * {@link com.kag.common.spinterfaces.ISystem}s and {@link com.kag.common.spinterfaces.IEntitySystem}s, making them
	 * run faster or slower.
	 *
	 * @return the multiplier of the bae speed of the game
	 */
	public float getSpeedMultiplier() {
		return speedMultiplier;
	}

	/**
	 * Set the speed at which the game should run. This multiplier will be applied to the delta value passed to all
	 * {@link com.kag.common.spinterfaces.ISystem}s and {@link com.kag.common.spinterfaces.IEntitySystem}s, making them
	 * run faster or slower.
	 * <p>
	 * A multiplier of 1 means that the game will run at normal speed. A multiplier of 0 makes the game pause. A
	 * multiplier greater than 1 makes the game run faster, and a multiplier between 0 and 1 makes the game run slower.
	 * A negative multiplier makes the game run in reverse, but since the game does not remember previous states, it
	 * might enter states that were not encountered previously. Here, a state refers to the collection of all data in
	 * the game world.
	 *
	 * @param speedMultiplier the multiplier to set
	 */
	public void setSpeedMultiplier(float speedMultiplier) {
		this.speedMultiplier = speedMultiplier;
	}
}
