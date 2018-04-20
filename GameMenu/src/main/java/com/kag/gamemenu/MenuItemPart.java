package com.kag.gamemenu;

import com.kag.common.entities.IPart;

/**
 * @author Kasper
 */
public class MenuItemPart implements IPart {

	private IMenuItem menuItem;


	public MenuItemPart(IMenuItem menuItem) {
		this.menuItem = menuItem;
	}

	public IMenuItem getMenuItem() {
		return menuItem;
	}
}
