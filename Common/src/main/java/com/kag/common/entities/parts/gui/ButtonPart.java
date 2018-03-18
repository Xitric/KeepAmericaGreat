package com.kag.common.entities.parts.gui;

import com.kag.common.entities.IPart;

/**
 *
 * @author Kasper
 */
public class ButtonPart implements IPart {

	private boolean highlighted;
	private boolean pressed;

	public void setHighlighted(boolean h) {
		highlighted = h;
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	public void setPressed(boolean p) {
		pressed = p;
	}

	public boolean isPressed() {
		return pressed;
	}
}
