package com.kag.basicenemies;

import com.kag.common.entities.Entity;
import com.kag.commonenemy.spinterfaces.IEnemy;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Kasper
 */
@ServiceProvider(service = IEnemy.class)
public class BasicEnemy implements IEnemy {

	@Override
	public int getDifficulty() {
		return 1;
	}

	@Override
	public Entity create() {
		return EnemyFactory.createEnemy(0, 40, 2, 2);
	}
}
