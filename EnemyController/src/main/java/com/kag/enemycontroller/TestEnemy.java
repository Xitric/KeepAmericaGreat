package com.kag.enemycontroller;

import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.*;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.enemycontroller.interfaces.IEnemy;
import com.kag.enemycontroller.parts.EnemyPart;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 * @author Kasper
 */
@ServiceProviders(value = {
		@ServiceProvider(service = IEnemy.class),
		@ServiceProvider(service = IComponentLoader.class)
})
public class TestEnemy implements IEnemy, IComponentLoader {

	@Override
	public int getDifficulty() {
		return 1;
	}

	@Override
	public Entity create() {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

		Entity enemy = new Entity();
		enemy.addPart(new PositionPart(6 * 64, 32));
		enemy.addPart(new EnemyPart(100));
		enemy.addPart(new BoundingBoxPart(64,64));
		enemy.addPart(new CurrencyPart(10));
		enemy.addPart(new LifePart(5));
		AssetPart hatPart = assetManager.createTexture(getClass().getResourceAsStream("/Yellow1.png"));
		hatPart.setWidth(48);
		hatPart.setHeight(48);
		hatPart.setxOffset(-24);
		hatPart.setyOffset(-24);
		hatPart.setzIndex(4);
		enemy.addPart(hatPart);

		AssetPart animationPart = assetManager.createAnimation(getClass().getResourceAsStream("/EnemyWalking.png"), 48, 52, 60);
		animationPart.setxOffset(-24);
		animationPart.setyOffset(-26);
		animationPart.setzIndex(3);
		enemy.addPart(animationPart);

		return enemy;
	}

	@Override
	public void load(World world) {
		world.addEntity(create());
	}

	@Override
	public void dispose(World world) {

	}
}
