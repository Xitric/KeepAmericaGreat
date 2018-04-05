package com.kag.basicenemies;

import com.kag.common.entities.Entity;
import com.kag.enemycontroller.interfaces.IEnemy;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Kasper
 */
@ServiceProvider(service = IEnemy.class)
public class SpeedEnemy implements IEnemy {

	@Override
	public int getDifficulty() {
		return 15;
	}

	@Override
	public Entity create() {
		return EnemyFactory.createEnemy(7, 300, 25, 3);
	}
}
