package com.kag.common.spinterfaces;

import com.kag.common.map.World;

public interface IGameStateListener {

	//Not called when starting the game - only when a new game is started after this
	void newGame(World world);
}
