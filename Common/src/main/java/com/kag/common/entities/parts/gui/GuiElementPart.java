package com.kag.common.entities.parts.gui;

import com.kag.common.entities.Entity;
import com.kag.common.entities.IPart;

/**
 * Part defining an entity that is part of the graphical user interface. Such an
 * entity can be set to have any other entity as its parent, and as a result
 * this gui element will follow its parent entity on the screen.
 *
 * @author Kasper
 */
public class GuiElementPart implements IPart {

	private Entity parent;

	public GuiElementPart() {

	}

	public GuiElementPart(Entity parent) {
		this.parent = parent;
	}

	public void setParent(Entity parent) {
		this.parent = parent;
	}

	public Entity getParent() {
		return parent;
	}
}
