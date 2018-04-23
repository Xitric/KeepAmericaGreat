package com.kag.basicenemies;

import com.kag.common.entities.Entity;
import com.kag.commonenemy.spinterfaces.IEnemy;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IEnemy.class)
public class BlackEnemy implements IEnemy {

	@Override
	public int getDifficulty() {
		return 5;
	}

	@Override
	public Entity create() {
		return EnemyFactory.createEnemy(3, 90, 10, 10);
	}
}
