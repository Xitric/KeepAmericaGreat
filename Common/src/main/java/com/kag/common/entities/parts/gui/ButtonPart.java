package com.kag.common.entities.parts.gui;

import com.kag.common.entities.IPart;

/**
 * Part defining a button in the graphical user interface, that can either be
 * highlighted when the mouse hovers over it or pressed down.
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
