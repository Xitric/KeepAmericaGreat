package com.kag.commonmenu.spinterfaces;

import com.kag.common.data.GameData;
import com.kag.common.entities.Entity;
import com.kag.common.map.World;

public interface IMenuItem {

	Entity getMenuItemEntity();

	int getHotkey();

	void onAction(World world, GameData gameData);
}
