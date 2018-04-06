package com.kag.basicenemies;

import com.kag.common.entities.Entity;
import com.kag.enemycontroller.interfaces.IEnemy;
import org.openide.util.Lookup;
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
		return Lookup.getDefault().lookup(EnemyFactory.class).createEnemy(6, 20, 50, 50);
	}
}
