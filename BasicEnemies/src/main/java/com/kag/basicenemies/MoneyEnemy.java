package com.kag.basicenemies;

import com.kag.common.entities.Entity;
import com.kag.tdcommon.spinterfaces.IEnemy;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Kasper
 */
@ServiceProvider(service = IEnemy.class)
public class MoneyEnemy implements IEnemy {

	@Override
	public int getDifficulty() {
		return 20;
	}

	@Override
	public Entity create() {
		return EnemyFactory.createEnemy(5, 90, 30, 6);
	}
}
