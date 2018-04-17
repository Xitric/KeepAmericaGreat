package com.kag.core.input;

import com.badlogic.gdx.InputAdapter;

/**
 * Input handler that forwards keyboard and mouse input to designated handlers.
 *
 * @author Kasper
 */
public class GdxInputProcessor extends InputAdapter {

	private final GdxKeyboard keyboard;
	private final GdxMouse mouse;

	public GdxInputProcessor(GdxKeyboard keyboard, GdxMouse mouse) {
		this.keyboard = keyboard;
		this.mouse = mouse;
	}

	@Override
	public boolean keyUp(int keycode) {
		return keyboard.keyUp(keycode) || super.keyUp(keycode);

	}

	@Override
	public boolean keyDown(int keycode) {
		return keyboard.keyDown(keycode) || super.keyDown(keycode);

	}

	@Override
	public boolean scrolled(int amount) {
		return mouse.scrolled(amount) || super.scrolled(amount);

	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return mouse.mouseMoved(screenX, screenY) || super.mouseMoved(screenX, screenY);

	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return mouse.mousePressed(screenX, screenY, button) || super.touchDown(screenX, screenY, pointer, button);

	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return mouse.mouseReleased(screenX, screenY, button) || super.touchUp(screenX, screenY, pointer, button);

	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return mouse.mouseDragged(screenX, screenY) || super.touchDragged(screenX, screenY, pointer);

	}
}
