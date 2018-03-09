package com.kag.core.input;

import com.badlogic.gdx.Input;
import com.kag.common.data.Keyboard;

/**
 * A keyboard input manager for interpreting LinGDX input and translating it
 * into something that the game engine can work with. The primary responsibility
 * of this class is to allow the game to get input through LibGDX without having
 * a dependency on LibGDX.
 *
 * @author Kasper
 */
public class GdxKeyboard {

	/**
	 * Array used to map LibGDX key codes to the key codes used by this engine.
	 * Each index in the array is the value of a LibGDX key, and the value at
	 * that index is the corresponding key code in this engine. For unsupported
	 * keys, the value at that index will be {@link Keyboard#KEY_NONE}.
	 */
	private final int[] keyMapper = new int[255];

	private final Keyboard keyboard;

	/**
	 * Constructs a new keyboard input manager for interpreting LibGDX input.
	 *
	 * @param keyboard class containing the keyboard state
	 */
	public GdxKeyboard(Keyboard keyboard) {
		this.keyboard = keyboard;
		initKeyMapper();
	}

	/**
	 * Test if the key with the specified LibGDX key code is unknown to this
	 * engine.
	 *
	 * @param keycode the key code to test for
	 * @return true if the key is unknown, false otherwise
	 */
	private boolean isKeyUnknown(int keycode) {
		return keycode < 0
				|| keycode >= keyMapper.length
				|| keyMapper[keycode] == Keyboard.KEY_NONE;
	}

	/**
	 * Informs this keyboard input handler that an update cycle was completed.
	 * This method should be called once every iteration of the game loop to
	 * differentiate between which iteration an input event occurred.
	 */
	public void update() {
		//Reset all key states to IDLE
		for (int key = Keyboard.KEY_FIRST; key <= Keyboard.KEY_LAST; key++) {
			keyboard.setKeyIdle(key);
		}
	}

	/**
	 * Call when a key on the keyboard is pressed.
	 *
	 * @param keycode the LibGDX code of the key that was pressed
	 * @return true if the input was processed, false otherwise
	 */
	public boolean keyDown(int keycode) {
		if (isKeyUnknown(keycode)) {
			return false;
		}

		keyboard.setKeyPressed(keyMapper[keycode]);
		return true;
	}

	/**
	 * Call when a key on the keyboard is released.
	 *
	 * @param keycode the LibGDX code of the key that was released
	 * @return true if the input was processed, false otherwise
	 */
	public boolean keyUp(int keycode) {
		if (isKeyUnknown(keycode)) {
			return false;
		}

		keyboard.setKeyReleased(keyMapper[keycode]);
		return true;
	}

	private void initKeyMapper() {
		keyMapper[Input.Keys.UNKNOWN] = Keyboard.KEY_NONE;
		keyMapper[Input.Keys.ESCAPE] = Keyboard.KEY_ESCAPE;
		keyMapper[Input.Keys.NUM_1] = Keyboard.KEY_1;
		keyMapper[Input.Keys.NUM_2] = Keyboard.KEY_2;
		keyMapper[Input.Keys.NUM_3] = Keyboard.KEY_3;
		keyMapper[Input.Keys.NUM_4] = Keyboard.KEY_4;
		keyMapper[Input.Keys.NUM_5] = Keyboard.KEY_5;
		keyMapper[Input.Keys.NUM_6] = Keyboard.KEY_6;
		keyMapper[Input.Keys.NUM_7] = Keyboard.KEY_7;
		keyMapper[Input.Keys.NUM_8] = Keyboard.KEY_8;
		keyMapper[Input.Keys.NUM_9] = Keyboard.KEY_9;
		keyMapper[Input.Keys.NUM_0] = Keyboard.KEY_0;
		keyMapper[Input.Keys.MINUS] = Keyboard.KEY_MINUS;
		keyMapper[Input.Keys.PLUS] = Keyboard.KEY_PLUS;
		keyMapper[Input.Keys.EQUALS] = Keyboard.KEY_EQUALS;
		keyMapper[Input.Keys.BACK] = Keyboard.KEY_BACK;
		keyMapper[Input.Keys.TAB] = Keyboard.KEY_TAB;
		keyMapper[Input.Keys.Q] = Keyboard.KEY_Q;
		keyMapper[Input.Keys.W] = Keyboard.KEY_W;
		keyMapper[Input.Keys.E] = Keyboard.KEY_E;
		keyMapper[Input.Keys.R] = Keyboard.KEY_R;
		keyMapper[Input.Keys.T] = Keyboard.KEY_T;
		keyMapper[Input.Keys.Y] = Keyboard.KEY_Y;
		keyMapper[Input.Keys.U] = Keyboard.KEY_U;
		keyMapper[Input.Keys.I] = Keyboard.KEY_I;
		keyMapper[Input.Keys.O] = Keyboard.KEY_O;
		keyMapper[Input.Keys.P] = Keyboard.KEY_P;
		keyMapper[Input.Keys.A] = Keyboard.KEY_A;
		keyMapper[Input.Keys.S] = Keyboard.KEY_S;
		keyMapper[Input.Keys.D] = Keyboard.KEY_D;
		keyMapper[Input.Keys.F] = Keyboard.KEY_F;
		keyMapper[Input.Keys.G] = Keyboard.KEY_G;
		keyMapper[Input.Keys.H] = Keyboard.KEY_H;
		keyMapper[Input.Keys.J] = Keyboard.KEY_J;
		keyMapper[Input.Keys.K] = Keyboard.KEY_K;
		keyMapper[Input.Keys.L] = Keyboard.KEY_L;
		keyMapper[Input.Keys.Z] = Keyboard.KEY_Z;
		keyMapper[Input.Keys.X] = Keyboard.KEY_X;
		keyMapper[Input.Keys.C] = Keyboard.KEY_C;
		keyMapper[Input.Keys.V] = Keyboard.KEY_V;
		keyMapper[Input.Keys.B] = Keyboard.KEY_B;
		keyMapper[Input.Keys.N] = Keyboard.KEY_N;
		keyMapper[Input.Keys.M] = Keyboard.KEY_M;
		keyMapper[Input.Keys.LEFT_BRACKET] = Keyboard.KEY_LBRACKET;
		keyMapper[Input.Keys.RIGHT_BRACKET] = Keyboard.KEY_RBRACKET;
		keyMapper[Input.Keys.ENTER] = Keyboard.KEY_ENTER;
		keyMapper[Input.Keys.CONTROL_LEFT] = Keyboard.KEY_LCONTROL;
		keyMapper[Input.Keys.SEMICOLON] = Keyboard.KEY_SEMICOLON;
		keyMapper[Input.Keys.APOSTROPHE] = Keyboard.KEY_APOSTROPHE;
		keyMapper[Input.Keys.GRAVE] = Keyboard.KEY_GRAVE;
		keyMapper[Input.Keys.SHIFT_LEFT] = Keyboard.KEY_LSHIFT;
		keyMapper[Input.Keys.BACKSLASH] = Keyboard.KEY_BACKSLASH;
		keyMapper[Input.Keys.COMMA] = Keyboard.KEY_COMMA;
		keyMapper[Input.Keys.PERIOD] = Keyboard.KEY_PERIOD;
		keyMapper[Input.Keys.SLASH] = Keyboard.KEY_SLASH;
		keyMapper[Input.Keys.SHIFT_RIGHT] = Keyboard.KEY_RSHIFT;
		keyMapper[Input.Keys.STAR] = Keyboard.KEY_MULTIPLY;
		keyMapper[Input.Keys.CONTROL_RIGHT] = Keyboard.KEY_RCONTROL;
		keyMapper[Input.Keys.SPACE] = Keyboard.KEY_SPACE;
		keyMapper[Input.Keys.F1] = Keyboard.KEY_F1;
		keyMapper[Input.Keys.F2] = Keyboard.KEY_F2;
		keyMapper[Input.Keys.F3] = Keyboard.KEY_F3;
		keyMapper[Input.Keys.F4] = Keyboard.KEY_F4;
		keyMapper[Input.Keys.F5] = Keyboard.KEY_F5;
		keyMapper[Input.Keys.F6] = Keyboard.KEY_F6;
		keyMapper[Input.Keys.F7] = Keyboard.KEY_F7;
		keyMapper[Input.Keys.F8] = Keyboard.KEY_F8;
		keyMapper[Input.Keys.F9] = Keyboard.KEY_F9;
		keyMapper[Input.Keys.F10] = Keyboard.KEY_F10;
		keyMapper[Input.Keys.F11] = Keyboard.KEY_F11;
		keyMapper[Input.Keys.F12] = Keyboard.KEY_F12;
		keyMapper[Input.Keys.NUM] = Keyboard.KEY_NUMLOCK;
		keyMapper[Input.Keys.NUMPAD_0] = Keyboard.KEY_NUMPAD0;
		keyMapper[Input.Keys.NUMPAD_1] = Keyboard.KEY_NUMPAD1;
		keyMapper[Input.Keys.NUMPAD_2] = Keyboard.KEY_NUMPAD2;
		keyMapper[Input.Keys.NUMPAD_3] = Keyboard.KEY_NUMPAD3;
		keyMapper[Input.Keys.NUMPAD_4] = Keyboard.KEY_NUMPAD4;
		keyMapper[Input.Keys.NUMPAD_5] = Keyboard.KEY_NUMPAD5;
		keyMapper[Input.Keys.NUMPAD_6] = Keyboard.KEY_NUMPAD6;
		keyMapper[Input.Keys.NUMPAD_7] = Keyboard.KEY_NUMPAD7;
		keyMapper[Input.Keys.NUMPAD_8] = Keyboard.KEY_NUMPAD8;
		keyMapper[Input.Keys.NUMPAD_9] = Keyboard.KEY_NUMPAD9;
		keyMapper[Input.Keys.ALT_LEFT] = Keyboard.KEY_LALT;
		keyMapper[Input.Keys.ALT_RIGHT] = Keyboard.KEY_RALT;
		keyMapper[Input.Keys.BACKSPACE] = Keyboard.KEY_BACKSPACE;
		keyMapper[Input.Keys.PAGE_UP] = Keyboard.KEY_PAGEUP;
		keyMapper[Input.Keys.PAGE_DOWN] = Keyboard.KEY_PAGEDOWN;
		keyMapper[Input.Keys.COLON] = Keyboard.KEY_COLON;
		keyMapper[Input.Keys.HOME] = Keyboard.KEY_HOME;
		keyMapper[Input.Keys.UP] = Keyboard.KEY_UP;
		keyMapper[Input.Keys.LEFT] = Keyboard.KEY_LEFT;
		keyMapper[Input.Keys.RIGHT] = Keyboard.KEY_RIGHT;
		keyMapper[Input.Keys.DOWN] = Keyboard.KEY_DOWN;
		keyMapper[Input.Keys.END] = Keyboard.KEY_END;
		keyMapper[Input.Keys.INSERT] = Keyboard.KEY_INSERT;
		keyMapper[Input.Keys.FORWARD_DEL] = Keyboard.KEY_DELETE;
		keyMapper[Input.Keys.POWER] = Keyboard.KEY_POWER;
	}
}
