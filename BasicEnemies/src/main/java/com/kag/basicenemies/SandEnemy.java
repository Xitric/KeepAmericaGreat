package com.kag.basicenemies;

import com.kag.common.entities.Entity;
import com.kag.enemycontroller.interfaces.IEnemy;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Kasper
 */
@ServiceProvider(service = IEnemy.class)
public class SandEnemy implements IEnemy {

	@Override
	public int getDifficulty() {
		return 7;
	}

	@Override
	public Entity create() {
		return EnemyFactory.createEnemy(4, 90, 4, 4);
	}
}
