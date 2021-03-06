package com.kag.basicenemies;

import com.kag.common.entities.Entity;
import com.kag.commonenemy.spinterfaces.IEnemy;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IEnemy.class)
public class YellowEnemyAlpha implements IEnemy {

	@Override
	public int getDifficulty() {
		return 3;
	}

	@Override
	public Entity create() {
		return EnemyFactory.createEnemy(1, 60, 5, 5);
	}
}
