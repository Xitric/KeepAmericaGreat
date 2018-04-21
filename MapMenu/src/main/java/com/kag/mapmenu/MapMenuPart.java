package com.kag.mapmenu;

import com.kag.common.entities.IPart;

/**
 * @author Kasper
 */
public class MapMenuPart implements IPart {

	private boolean visible;

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
