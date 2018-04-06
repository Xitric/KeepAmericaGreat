package com.kag.basicenemies;

import com.kag.common.entities.Entity;
import com.kag.enemycontroller.interfaces.IEnemy;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Kasper
 */
@ServiceProvider(service = IEnemy.class)
public class BlackEnemy implements IEnemy {

	@Override
	public int getDifficulty() {
		return 5;
	}

	@Override
	public Entity create() {
		return Lookup.getDefault().lookup(EnemyFactory.class).createEnemy(3, 90, 10, 1);
	}
}
