package com.kag.basicenemies;

import com.kag.common.entities.Entity;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commonenemy.spinterfaces.IEnemy;
import com.kag.commontd.entities.parts.MoneyPart;
import org.junit.Assert;
import org.netbeans.junit.MockServices;
import org.openide.util.Lookup;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Kasper
 */
public class EnemyMoneyTest {

	@org.junit.Test
	public void create() {
		//TC#30
		//Arrange
		MockServices.setServices(AssetManagerMock.class);
		new EnemyFactory().load(null);
		List<Entity> enemies = new ArrayList<>();
		List<Integer> difficulties = new ArrayList<>();

		//Act
		for (IEnemy enemyFactory : Lookup.getDefault().lookupAll(IEnemy.class)) {
			enemies.add(enemyFactory.create());
			difficulties.add(enemyFactory.getDifficulty());
		}

		//Assert
		List<Entity> enemiesSortedDifficulty = new ArrayList<>();
		for (int i = 0; i < enemies.size(); i++) {
			//Get index of weakest
			int minIndex = 0;
			int min = Integer.MAX_VALUE;
			for (int j = 0; j < difficulties.size(); j++) {
				if (difficulties.get(j) < min) {
					min = difficulties.get(j);
					minIndex = j;
				}
			}

			enemiesSortedDifficulty.add(enemies.get(minIndex));
			difficulties.set(minIndex, Integer.MAX_VALUE);
		}

		List<Entity> enemiesSortedMoney = new ArrayList<>(enemies);
		enemiesSortedMoney.sort(Comparator.comparingInt(e -> e.getPart(MoneyPart.class).getMoney()));

		Assert.assertEquals(enemiesSortedDifficulty.toArray(), enemiesSortedMoney.toArray());
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
