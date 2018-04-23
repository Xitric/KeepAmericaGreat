package com.kag.common.input;

/**
 * Class containing information about the state of the mouse.
 */
public class Mouse {

	/**
	 * The numerical code for a button on the mouse, which is supported by this
	 * game.
	 */
	public static final int BUTTON_FIRST = 0,
			BUTTON_NONE = 0,
			BUTTON_LEFT = 1,
			BUTTON_RIGHT = 2,
			BUTTON_MIDDLE = 3,
			BUTTON_FORWARD = 4,
			BUTTON_BACKWARD = 5,
			BUTTON_LAST = 5;

	/**
	 * A state in which a button can be.
	 */
	private static final int STATE_IDLE = 0,
			STATE_PRESSED = 1,
			STATE_RELEASED = 2;

	/**
	 * Array used to store the actions performed on each button on the last
	 * input poll.
	 */
	private final int[] buttonStates = new int[BUTTON_LAST + 1];

	/**
	 * Array used to store the pressed states of each button. Index into this
	 * array using the button code of the button to test for.
	 */
	private final boolean[] pressedButtons = new boolean[BUTTON_LAST + 1];

	/**
	 * The current coordinate of the mouse pointer in screen space.
	 */
	private int newX, newY;

	/**
	 * The coordinate of the mouse pointer in screen space from the previous
	 * input poll.
	 */
	private int oldX, oldY;

	/**
	 * The amount of scrolling performed on the mouse wheel since the previous
	 * input poll.
	 */
	private int scrollAmount;

	/**
	 * Set the horizontal position of the mouse pointer in screen space.
	 *
	 * @param x the horizontal position of the mouse pointer
	 */
	public void setX(int x) {
		newX = x;
	}

	/**
	 * Set the vertical position of the mouse pointer in screen space.
	 *
	 * @param y the vertical position of the mouse pointer
	 */
	public void setY(int y) {
		newY = y;
	}

	/**
	 * Set the horizontal position of the mouse pointer in screen space on the
	 * previous input poll.
	 *
	 * @param x the horizontal position of the mouse pointer on the previous
	 *          input poll
	 */
	public void setPreviousX(int x) {
		oldX = x;
	}

	/**
	 * Set the vertical position of the mouse pointer in screen space on the
	 * previous input poll.
	 *
	 * @param y the vertical position of the mouse pointer on the previous input
	 *          poll
	 */
	public void setPreviousY(int y) {
		oldY = y;
	}

	/**
	 * Set the amount of scrolling performed on the mouse wheel since the
	 * previous input poll.
	 *
	 * @param amount the amount of scrolling
	 */
	public void setScrollAmount(int amount) {
		scrollAmount = amount;
	}

	/**
	 * Set the state of the specified mouse button to be pressed at the
	 * specified coordinates.
	 *
	 * @param x      the horizontal position of the mouse pointer
	 * @param y      the vertical position of the mouse pointer
	 * @param button the button to change the state for
	 */
	public void setButtonPressed(int x, int y, int button) {
		buttonStates[button] = STATE_PRESSED;
		pressedButtons[button] = true;
	}

	/**
	 * Set the state of the specified mouse button to be released at the
	 * specified coordinates.
	 *
	 * @param x      the horizontal position of the mouse pointer
	 * @param y      the vertical position of the mouse pointer
	 * @param button the button to change the state for
	 */
	public void setButtonReleased(int x, int y, int button) {
		buttonStates[button] = STATE_RELEASED;
		pressedButtons[button] = false;
	}

	/**
	 * Set the state of the specified mouse button to be idle. An idle button
	 * has not changed its state during the last update poll.
	 *
	 * @param button the button to change the state for
	 */
	public void setButtonIdle(int button) {
		buttonStates[button] = STATE_IDLE;
	}

	/**
	 * Get the horizontal position of the mouse pointer in screen space.
	 *
	 * @return the horizontal position of the mouse pointer
	 */
	public int getX() {
		return newX;
	}

	/**
	 * Get the vertical position of the mouse pointer in screen space.
	 *
	 * @return the vertical position of the mouse pointer
	 */
	public int getY() {
		return newY;
	}

	/**
	 * Get the horizontal position of the mouse pointer in screen space on the
	 * previous input poll.
	 *
	 * @return the horizontal position of the mouse pointer on the previous
	 *         input poll
	 */
	public int getPreviousX() {
		return oldX;
	}

	/**
	 * Get the vertical position of the mouse pointer in screen space on the
	 * previous input poll.
	 *
	 * @return the vertical position of the mouse pointer on the previous input
	 *         poll
	 */
	public int getPreviousY() {
		return oldY;
	}

	/**
	 * Get the horizontal distance that the mouse has moved since the previous
	 * input poll.
	 *
	 * @return the horizontal distance that the mouse has moved
	 */
	public int getMovementX() {
		return newX - oldX;
	}

	/**
	 * Get the vertical distance that the mouse has moved since the previous
	 * input poll.
	 *
	 * @return the vertical distance that the mouse has moved
	 */
	public int getMovementY() {
		return newY - oldY;
	}

	/**
	 * Get the amount of scrolling performed on the mouse wheel since the
	 * previous input poll.
	 *
	 * @return the amount of scrolling
	 */
	public int getScrollAmount() {
		return scrollAmount;
	}

	/**
	 * Test if the button with the specified code is currently being pressed
	 * down.
	 *
	 * @param button the button to test for
	 * @return true if the button is currently being pressed, false otherwise
	 */
	public boolean isButtonDown(int button) {
		return pressedButtons[button];
	}

	/**
	 * Test if the button with the specified code was just pressed down.
	 *
	 * @param button the button to test for
	 * @return true if the button was just pressed down, false otherwise
	 */
	public boolean isButtonPressed(int button) {
		return buttonStates[button] == STATE_PRESSED;
	}

	/**
	 * Test if the button with the specified code was just released.
	 *
	 * @param button the button to test for
	 * @return true if the button was just released, false otherwise
	 */
	public boolean isButtonReleased(int button) {
		return buttonStates[button] == STATE_RELEASED;
	}

	/**
	 * Test if the specified mouse button was used to drag the mouse since the
	 * previous input poll. The mouse is dragged when the specified button is
	 * held down and the mouse has moved.
	 *
	 * @param button the button performing the drag
	 * @return true if the mouse was dragged, false otherwise
	 */
	public boolean isMouseDragged(int button) {
		return isButtonDown(button)
				&& (getMovementX() != 0 || getMovementY() != 0);
	}
}
