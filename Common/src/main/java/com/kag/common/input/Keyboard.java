package com.kag.common.input;

/**
 * Class containing information about the state of the keyboard.
 *
 * @author Kasper
 */
public class Keyboard {

	/**
	 * The numerical code for a key on the keyboard, which is supported by this
	 * game.
	 */
	public static final int KEY_FIRST = 0,
			KEY_NONE = 0,
			KEY_ESCAPE = 1,
			KEY_1 = 2,
			KEY_2 = 3,
			KEY_3 = 4,
			KEY_4 = 5,
			KEY_5 = 6,
			KEY_6 = 7,
			KEY_7 = 8,
			KEY_8 = 9,
			KEY_9 = 10,
			KEY_0 = 11,
			KEY_MINUS = 12,
			KEY_PLUS = 13,
			KEY_EQUALS = 14,
			KEY_BACK = 15,
			KEY_TAB = 16,
			KEY_Q = 17,
			KEY_W = 18,
			KEY_E = 19,
			KEY_R = 20,
			KEY_T = 21,
			KEY_Y = 22,
			KEY_U = 23,
			KEY_I = 24,
			KEY_O = 25,
			KEY_P = 26,
			KEY_A = 27,
			KEY_S = 28,
			KEY_D = 29,
			KEY_F = 30,
			KEY_G = 31,
			KEY_H = 32,
			KEY_J = 33,
			KEY_K = 34,
			KEY_L = 35,
			KEY_Z = 36,
			KEY_X = 37,
			KEY_C = 38,
			KEY_V = 39,
			KEY_B = 40,
			KEY_N = 41,
			KEY_M = 42,
			KEY_LBRACKET = 43,
			KEY_RBRACKET = 44,
			KEY_ENTER = 45,
			KEY_LCONTROL = 46,
			KEY_SEMICOLON = 47,
			KEY_APOSTROPHE = 48,
			KEY_GRAVE = 49,
			KEY_LSHIFT = 50,
			KEY_BACKSLASH = 51,
			KEY_COMMA = 52,
			KEY_PERIOD = 53,
			KEY_SLASH = 54,
			KEY_RSHIFT = 55,
			KEY_MULTIPLY = 56,
			KEY_RCONTROL = 57,
			KEY_SPACE = 58,
			KEY_F1 = 59,
			KEY_F2 = 60,
			KEY_F3 = 61,
			KEY_F4 = 62,
			KEY_F5 = 63,
			KEY_F6 = 64,
			KEY_F7 = 65,
			KEY_F8 = 66,
			KEY_F9 = 67,
			KEY_F10 = 68,
			KEY_F11 = 69,
			KEY_F12 = 70,
			KEY_NUMLOCK = 71,
			KEY_NUMPAD0 = 72,
			KEY_NUMPAD1 = 73,
			KEY_NUMPAD2 = 74,
			KEY_NUMPAD3 = 75,
			KEY_NUMPAD4 = 76,
			KEY_NUMPAD5 = 77,
			KEY_NUMPAD6 = 78,
			KEY_NUMPAD7 = 79,
			KEY_NUMPAD8 = 80,
			KEY_NUMPAD9 = 81,
			KEY_LALT = 82,
			KEY_RALT = 83,
			KEY_BACKSPACE = 84,
			KEY_PAGEUP = 85,
			KEY_PAGEDOWN = 86,
			KEY_COLON = 87,
			KEY_HOME = 88,
			KEY_UP = 89,
			KEY_LEFT = 90,
			KEY_RIGHT = 91,
			KEY_DOWN = 93,
			KEY_END = 94,
			KEY_INSERT = 95,
			KEY_DELETE = 96,
			KEY_POWER = 97,
			KEY_LAST = 97;

	/**
	 * A state in which a key can be.
	 */
	private static final int STATE_IDLE = 0,
			STATE_PRESSED = 1,
			STATE_RELEASED = 2,
			STATE_REPEATED = 3;

	/**
	 * Array used to store the actions performed on each key on the last input
	 * poll.
	 */
	private final int[] keyStates = new int[KEY_LAST + 1];

	/**
	 * Array used to store the pressed states of each key. Index into this array
	 * using the key code of the key to test for.
	 */
	private final boolean[] pressedKeys = new boolean[KEY_LAST + 1];

	/**
	 * Set the state of the specified key to have just been pressed.
	 *
	 * @param key the key to change the state for
	 */
	public void setKeyPressed(int key) {
		keyStates[key] = STATE_PRESSED;
		pressedKeys[key] = true;
	}

	/**
	 * Set the state of the specified key to have just been released.
	 *
	 * @param key the key to change the state for
	 */
	public void setKeyReleased(int key) {
		keyStates[key] = STATE_RELEASED;
		pressedKeys[key] = false;
	}

	/**
	 * Set the state of the specified key to be repeating. A key is repeating
	 * when it has been pressed down for X milliseconds.
	 *
	 * @param key the key to change the state for
	 */
	public void setKeyRepeated(int key) {
		keyStates[key] = STATE_REPEATED;
	}

	/**
	 * Set the state of the specified key to be idle. An idle key has not
	 * changed its state during the last update poll.
	 *
	 * @param key the key to change the state for
	 */
	public void setKeyIdle(int key) {
		keyStates[key] = STATE_IDLE;
	}

	/**
	 * Test if the key with the specified code was just pressed down.
	 *
	 * @param key the key to test for
	 * @return true if the key was just pressed down, false otherwise
	 */
	public boolean isKeyPressed(int key) {
		return keyStates[key] == STATE_PRESSED;
	}

	/**
	 * Test if the key with the specified code was just released.
	 *
	 * @param key the key to test for
	 * @return true if the key was just released, false otherwise
	 */
	public boolean isKeyReleased(int key) {
		return keyStates[key] == STATE_RELEASED;
	}

	/**
	 * Test if the key with the specified code is repeating. A key is repeating
	 * when it has been pressed down for X milliseconds.
	 *
	 * @param key the key to test for
	 * @return true if the key is repeating, false otherwise
	 */
	public boolean isKeyRepeated(int key) {
		return keyStates[key] == STATE_REPEATED;
	}

	/**
	 * Test if the key with the specified code is currently being pressed down.
	 *
	 * @param key the key to test for
	 * @return true if the key is currently being pressed, false otherwise
	 */
	public boolean isKeyDown(int key) {
		return pressedKeys[key];
	}
}
