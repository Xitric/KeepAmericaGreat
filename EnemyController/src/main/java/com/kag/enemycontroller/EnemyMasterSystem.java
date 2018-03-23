/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.enemycontroller;

import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.spinterfaces.ISystem;

//@ServiceProvider(service = ISystem.class)
public class EnemyMasterSystem implements ISystem {

	
	@Override
	public void update(float dt, World world, GameData gameData) {

	}

	@Override
	public int getPriority() {
		return 0;
	}

}
