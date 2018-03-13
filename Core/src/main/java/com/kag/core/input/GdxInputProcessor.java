package com.kag.core.input;

import com.badlogic.gdx.InputAdapter;

/**
 * Input handler that forwards keyboard and mouse input to designated handlers.
 *
 * @author Kasper
 */
public class GdxInputProcessor extends InputAdapter {

	private GdxKeyboard keyboard;
	private GdxMouse mouse;

	public GdxInputProcessor(GdxKeyboard keyboard, GdxMouse mouse) {
		this.keyboard = keyboard;
		this.mouse = mouse;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (!keyboard.keyUp(keycode)) {
			return super.keyUp(keycode);
		}

		return true;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (!keyboard.keyDown(keycode)) {
			return super.keyDown(keycode);
		}

		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		if (!mouse.scrolled(amount)) {
			return super.scrolled(amount);
		}

		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (!mouse.mouseMoved(screenX, screenY)) {
			return super.mouseMoved(screenX, screenY);
		}

		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (!mouse.mousePressed(screenX, screenY, button)) {
			return super.touchDown(screenX, screenY, pointer, button);
		}

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (!mouse.mouseReleased(screenX, screenY, button)) {
			return super.touchUp(screenX, screenY, pointer, button);
		}

		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (!mouse.mouseDragged(screenX, screenY)) {
			return super.touchDragged(screenX, screenY, pointer);
		}

		return true;
	}
}
