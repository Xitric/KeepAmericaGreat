package com.kag.enemycontroller;

import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.entities.parts.PositionPart;
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
		enemy.addPart(new EnemyPart(200));
		AssetPart assetPart = assetManager.createTexture(getClass().getResourceAsStream("/Yellow1.png"));
		assetPart.setWidth(48);
		assetPart.setHeight(48);
		assetPart.setzIndex(4);
		enemy.addPart(assetPart);

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
