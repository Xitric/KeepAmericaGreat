package com.kag.basicenemies;

import com.kag.common.entities.Entity;
import com.kag.commonenemy.spinterfaces.IEnemy;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IEnemy.class)
public class MetalEnemy implements IEnemy {

	@Override
	public int getDifficulty() {
		return 25;
	}

	@Override
	public Entity create() {
		return EnemyFactory.createEnemy(6, 20, 50, 50);
	}
}
