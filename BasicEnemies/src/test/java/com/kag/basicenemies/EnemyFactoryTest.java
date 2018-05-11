package com.kag.basicenemies;

import com.kag.common.map.GameMap;
import com.kag.common.map.World;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import org.junit.Assert;
import org.netbeans.junit.MockServices;

import java.io.InputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Kasper
 */
public class EnemyFactoryTest {

	private EnemyFactory enemyFactory;
	private World world;

	@org.junit.Before
	public void setUp() throws Exception {
		//Arrange
		MockServices.setServices(AssetManagerMock.class);

		world = new World(new GameMap(0, 0, 0, 0));

		enemyFactory = new EnemyFactory();
		enemyFactory.load(world);
		world.addEntity(EnemyFactory.createEnemy(0, 0, 0, 1));
		world.addEntity(EnemyFactory.createEnemy(0, 0, 0, 1));
		world.addEntity(EnemyFactory.createEnemy(0, 0, 0, 1));
		world.addEntity(EnemyFactory.createEnemy(0, 0, 0, 1));
		world.addEntity(EnemyFactory.createEnemy(0, 0, 0, 1));
	}

	@org.junit.Test
	public void dispose() {
		//Assert
		//Six entities because we count the tile map entity
		Assert.assertEquals(6, world.getAllEntities().size());

		//TC#24
		//Act
		enemyFactory.dispose(world);

		//Assert
		//The one entity is the tile map entity
		Assert.assertEquals(1, world.getAllEntities().size());

		System.out.println(world.getAllEntities().iterator().next());
	}

	public static class AssetManagerMock implements IAssetManager {

		public AssetManagerMock() {

		}

		@Override
		public IAsset loadAsset(InputStream input) {
			IAsset asset = mock(IAsset.class);
			when(asset.getWidth()).thenReturn(0);
			when(asset.getHeight()).thenReturn(0);
			return asset;
		}

		@Override
		public AssetPart createTexture(IAsset asset, int x, int y, int width, int height) {
			AssetPart assetPart = mock(AssetPart.class);
			when(assetPart.getWidth()).thenReturn(0);
			when(assetPart.getHeight()).thenReturn(0);
			return assetPart;
		}

		@Override
		public AssetPart createTexture(InputStream input) {
			return null;
		}

		@Override
		public AssetPart createAnimation(InputStream input, int frameWidth, int frameHeight, int frameDuration) {
			return null;
		}

		@Override
		public AssetPart createAnimation(IAsset asset, int x, int y, int width, int height, int frameWidth, int frameHeight, int frameDuration) {
			return mock(AssetPart.class);
		}
	}
}