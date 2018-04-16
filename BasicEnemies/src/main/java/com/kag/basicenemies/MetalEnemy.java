package com.kag.basicenemies;

import com.kag.common.entities.Entity;
import com.kag.tdcommon.spinterfaces.IEnemy;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Kasper
 */
@ServiceProvider(service = IEnemy.class)
public class MetalEnemy implements IEnemy {

	@Override
	public int getDifficulty() {
		return 15;
	}

	@Override
	public Entity create() {
		return EnemyFactory.createEnemy(6, 20, 50, 50);
	}
}
