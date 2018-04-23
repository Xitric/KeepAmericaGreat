package com.kag.core.input;

import com.badlogic.gdx.Input;
import com.kag.common.input.Mouse;

/**
 * A mouse input manager for interpreting LinGDX input and translating it into
 * something that the game engine can work with. The primary responsibility of
 * this class is to allow the game to get input through LibGDX without having a
 * dependency on LibGDX.
 */
public class GdxMouse {

	/**
	 * Array used to map LibGDX button codes to the button codes used by this
	 * engine. Each index in the array is the value of a LibGDX button, and the
	 * value at that index is the corresponding button code in this engine. For
	 * unsupported buttons, the value at that index will be
	 * {@link Mouse#BUTTON_NONE}.
	 */
	private final int[] buttonMapper = new int[5];

	private final Mouse mouse;

	/**
	 * Constructs a new mouse input manager for interpreting LibGDX input.
	 *
	 * @param mouse class containing the mouse state
	 */
	public GdxMouse(Mouse mouse) {
		this.mouse = mouse;
		initButtonMapper();
	}

	/**
	 * Test if the button with the specified LibGDX button code is unknown to
	 * this engine.
	 *
	 * @param buttoncode the button code to test for
	 * @return true if the button is unknown, false otherwise
	 */
	private boolean isButtonUnknown(int buttoncode) {
		return buttoncode < 0
				|| buttoncode >= buttonMapper.length
				|| buttonMapper[buttoncode] == Mouse.BUTTON_NONE;
	}

	/**
	 * Informs this mouse input handler that an update cycle was completed. This
	 * method should be called once every iteration of the game loop to
	 * differentiate between which iteration an input event occurred.
	 */
	public void update() {
		//Reset all mouse button states to IDLE
		for (int button = Mouse.BUTTON_FIRST; button <= Mouse.BUTTON_LAST; button++) {
			mouse.setButtonIdle(button);
		}

		//Save position in old coordinates
		mouse.setPreviousX(mouse.getX());
		mouse.setPreviousY(mouse.getY());

		//Reset scroll amount
		mouse.setScrollAmount(0);
	}

	/**
	 * Call when the scroll wheel is moved.
	 *
	 * @param amount the amount of scrolling
	 * @return true if the input was processed, false otherwise
	 */
	@SuppressWarnings("SameReturnValue")
	public boolean scrolled(int amount) {
		mouse.setScrollAmount(amount);
		return true;
	}

	/**
	 * Call when the mouse pointer is moved.
	 *
	 * @param x the horizontal position of the mouse
	 * @param y the vertical position of the mouse
	 * @return true if the input was processed, false otherwise
	 */
	@SuppressWarnings("SameReturnValue")
	public boolean mouseMoved(int x, int y) {
		mouse.setX(x);
		mouse.setY(y);
		return true;
	}

	/**
	 * Call when a mouse button is pressed.
	 *
	 * @param x          the horizontal position of the mouse
	 * @param y          the vertical position of the mouse
	 * @param buttoncode the LibGDX code of the button that was pressed
	 * @return true if the input was processed, false otherwise
	 */
	public boolean mousePressed(int x, int y, int buttoncode) {
		if (isButtonUnknown(buttoncode)) {
			return false;
		}

		mouse.setButtonPressed(x, y, buttonMapper[buttoncode]);
		return true;
	}

	/**
	 * Call when a mouse button is released.
	 *
	 * @param x          the horizontal position of the mouse
	 * @param y          the vertical position of the mouse
	 * @param buttoncode the LibGDX code of the button that was pressed
	 * @return true if the input was processed, false otherwise
	 */
	public boolean mouseReleased(int x, int y, int buttoncode) {
		if (isButtonUnknown(buttoncode)) {
			return false;
		}

		mouse.setButtonReleased(x, y, buttonMapper[buttoncode]);
		return true;
	}

	/**
	 * Call when the mouse is dragged.
	 *
	 * @param x the horizontal position of the mouse
	 * @param y the vertical position of the mouse
	 * @return true if the input was processed, false otherwise
	 */
	@SuppressWarnings("SameReturnValue")
	public boolean mouseDragged(int x, int y) {
		mouse.setX(x);
		mouse.setY(y);
		return true;
	}

	private void initButtonMapper() {
		buttonMapper[Input.Buttons.LEFT] = Mouse.BUTTON_LEFT;
		buttonMapper[Input.Buttons.RIGHT] = Mouse.BUTTON_RIGHT;
		buttonMapper[Input.Buttons.MIDDLE] = Mouse.BUTTON_MIDDLE;
		buttonMapper[Input.Buttons.FORWARD] = Mouse.BUTTON_FORWARD;
		buttonMapper[Input.Buttons.BACK] = Mouse.BUTTON_BACKWARD;
	}
}
