package com.kag.basicenemies;

import com.kag.common.entities.Entity;
import com.kag.commonenemy.spinterfaces.IEnemy;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Kasper
 */
@ServiceProvider(service = IEnemy.class)
public class YellowEnemyBeta implements IEnemy {

	@Override
	public int getDifficulty() {
		return 3;
	}

	@Override
	public Entity create() {
		return EnemyFactory.createEnemy(2, 60, 6, 8);
	}
}
