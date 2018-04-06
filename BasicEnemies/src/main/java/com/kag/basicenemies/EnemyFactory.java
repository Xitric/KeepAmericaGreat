package com.kag.basicenemies;

import com.kag.common.data.IAsset;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.*;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.enemycontroller.parts.EnemyPart;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Kasper
 */
@ServiceProvider(service = IComponentLoader.class)
public class EnemyFactory implements IComponentLoader {

	private IAsset hatSpriteSheet;

	public Entity createEnemy(int imageOffset, float speed, int money, int life) {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

		AssetPart hatPart = assetManager.createTexture(hatSpriteSheet, imageOffset * 128, 0, 128, 128);
		hatPart.setWidth(48);
		hatPart.setHeight(48);
		hatPart.setxOffset(-hatPart.getWidth() / 2);
		hatPart.setyOffset(-hatPart.getHeight() / 2);
		hatPart.setzIndex(4);

		Entity enemy = new Entity();
		enemy.addPart(hatPart);
		enemy.addPart(new PositionPart(0, 0));
		enemy.addPart(new EnemyPart(speed));
		enemy.addPart(new BoundingBoxPart(hatPart.getWidth(), hatPart.getHeight()));
		enemy.addPart(new BlockingPart());
		enemy.addPart(new CurrencyPart(money));
		enemy.addPart(new LifePart(life));
		enemy.addPart(new BasicEnemyPart());

		AssetPart animationPart = assetManager.createAnimation(EnemyFactory.class.getResourceAsStream("/EnemyWalking.png"), 48, 52, (int) (60.0f * 100 / speed));
		animationPart.setxOffset(-24);
		animationPart.setyOffset(-26);
		animationPart.setzIndex(3);
		enemy.addPart(animationPart);

		return enemy;
	}

	@Override
	public void load(World world) {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		hatSpriteSheet = assetManager.loadAsset(getClass().getResourceAsStream("/AllHats.png"));
	}

	@Override
	public void dispose(World world) {
		hatSpriteSheet.dispose();

		for (Entity e: world.getAllEntities()) {
			if (e.hasPart(BasicEnemyPart.class)) {
				world.removeEntity(e);
			}
		}
	}
}
