package com.kag.tdcommon.spinterfaces;

import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;

/**
 * @author Kasper
 */
public interface IMenuItem {

	Entity getMenuItemEntity();

	int getHotkey();

	void onAction(World world, GameData gameData);
}
