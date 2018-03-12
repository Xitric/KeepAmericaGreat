package com.kag.common.data;

public class GameData {

	private final Keyboard keyboard;
	private final Mouse mouse;
	private final int width;
	private final int height;
	private final Camera camera;

	public GameData(Keyboard keyboard, Mouse mouse, int width, int height, Camera camera) {
		this.keyboard = keyboard;
		this.mouse = mouse;
		this.width = width;
		this.height = height;
		this.camera = camera;
	}

	/**
	 * @return the mouse
	 */
	public Mouse getMouse() {
		return mouse;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the camera
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * @return the keyboard
	 */
	public Keyboard getKeyboard() {
		return keyboard;
	}
}
