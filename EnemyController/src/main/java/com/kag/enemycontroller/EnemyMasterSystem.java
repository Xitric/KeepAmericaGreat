/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.enemycontroller;

import com.kag.common.data.World;
import com.kag.common.spinterfaces.ISystem;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ISystem.class)
public class EnemyMasterSystem implements ISystem {

	
	@Override
	public void update(float dt, World world) {
		System.out.println("Update inde i EnemyMasterSystem");
	}

	@Override
	public int getPriority() {
		return 0;
	}

}
