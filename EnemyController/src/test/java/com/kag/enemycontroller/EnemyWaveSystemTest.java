package com.kag.enemycontroller;

import com.kag.common.data.GameData;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.input.Keyboard;
import com.kag.common.input.Mouse;
import com.kag.common.map.GameMap;
import com.kag.common.map.World;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commonenemy.entities.parts.EnemyPart;
import com.kag.commonenemy.spinterfaces.IEnemy;
import com.kag.commonplayer.entities.parts.PlayerPart;
import com.kag.commontd.entities.parts.MoneyPart;
import org.junit.Assert;
import org.netbeans.junit.MockServices;

import java.io.InputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Kasper
 */
public class EnemyWaveSystemTest {

	private float spawnDelta = 0.3f;
	private float waveDelta = 30f;
	private World world;
	private GameData gameData;

	@org.junit.Before
	public void setUp() throws Exception {
		//Arrange
		world = new World(new GameMap(8, 8, 64, 64));
		gameData = mock(GameData.class);
		Keyboard keyboard = mock(Keyboard.class);
		Mouse mouse = mock(Mouse.class);

		when(gameData.getKeyboard()).thenReturn(keyboard);
		when(gameData.getMouse()).thenReturn(mouse);
		when(keyboard.isKeyPressed(Keyboard.KEY_SPACE)).thenReturn(false);
		when(mouse.isButtonPressed(Mouse.BUTTON_LEFT)).thenReturn(false);
		when(mouse.getX()).thenReturn(0);
		when(mouse.getY()).thenReturn(0);

		MockServices.setServices(AssetManagerMock.class, EnemyFactoryMock.class);
	}

	@org.junit.Test
	public void updateSpawnPosition() {
		//Act
		//TC#29
		EnemyWaveSystem ews = new EnemyWaveSystem();
		ews.load(world);
		ews.update(0, world, gameData);

		ews.update(waveDelta, world, gameData);
		ews.update(spawnDelta*2, world, gameData);

		//Assert
		Entity enemy = world.getEntitiesByFamily(Family.forAll(EnemyPart.class)).stream().findFirst().orElse(null);
		Assert.assertEquals(world.getGameMap().getTileHeight() / 2.0f, enemy.getPart(PositionPart.class).getY());
	}

	@org.junit.Test
	public void updateDifficulty() {
		//Act
		//TC#7
		EnemyWaveSystem ews = new EnemyWaveSystem();
		ews.load(world);
		ews.update(0, world, gameData);

		ews.update(waveDelta, world, gameData);
		int firstWaveCount = getEnemyCountForWave(ews);

		//Skip some waves to ensure more enemies will spawn
		ews.update(waveDelta, world, gameData);
		getEnemyCountForWave(ews);
		ews.update(waveDelta, world, gameData);
		getEnemyCountForWave(ews);
		ews.update(waveDelta, world, gameData);
		getEnemyCountForWave(ews);
		ews.update(waveDelta, world, gameData);
		getEnemyCountForWave(ews);
		ews.update(waveDelta, world, gameData);
		getEnemyCountForWave(ews);
		ews.update(waveDelta, world, gameData);
		getEnemyCountForWave(ews);
		ews.update(waveDelta, world, gameData);
		getEnemyCountForWave(ews);
		ews.update(waveDelta, world, gameData);
		getEnemyCountForWave(ews);

		ews.update(waveDelta, world, gameData);
		int tenthWaveCount = getEnemyCountForWave(ews);

		//Assert
		Assert.assertTrue(tenthWaveCount > firstWaveCount);
	}

	@org.junit.Test
	public void updateMoney() {
		//TC#30
		//Arrange
		Entity player = new Entity();
		player.addPart(new PlayerPart());
		player.addPart(new MoneyPart(0));
		world.addEntity(player);

		//Act
		EnemyWaveSystem ews = new EnemyWaveSystem();
		ews.load(world);
		ews.update(0, world, gameData);

		ews.update(waveDelta, world, gameData);
		getEnemyCountForWave(ews);

		//Assert
		Assert.assertTrue(player.getPart(MoneyPart.class).getMoney() > 0);
	}

	private int getEnemyCountForWave(EnemyWaveSystem ews) {
		int existingCount = world.getEntitiesByFamily(Family.forAll(EnemyPart.class)).size();
		int previousCount = -1;
		int count = 0;
		while(count > previousCount) {
			previousCount = count;
			ews.update(spawnDelta, world, gameData);
			count = world.getEntitiesByFamily(Family.forAll(EnemyPart.class)).size();
		}

		return count - existingCount;
	}

	public static class AssetManagerMock implements IAssetManager {

		public AssetManagerMock() {

		}

		@Override
		public IAsset loadAsset(InputStream input) {
			return null;
		}

		@Override
		public AssetPart createTexture(IAsset asset, int x, int y, int width, int height) {
			return null;
		}

		@Override
		public AssetPart createTexture(InputStream input) {
			return mock(AssetPart.class);
		}

		@Override
		public AssetPart createAnimation(InputStream input, int frameWidth, int frameHeight, int frameDuration) {
			return null;
		}

		@Override
		public AssetPart createAnimation(IAsset asset, int x, int y, int width, int height, int frameWidth, int frameHeight, int frameDuration) {
			return null;
		}
	}

	public static class EnemyFactoryMock implements IEnemy {

		public EnemyFactoryMock() {

		}

		@Override
		public int getDifficulty() {
			return 1;
		}

		@Override
		public Entity create() {
			Entity enemy = new Entity();
			enemy.addPart(new EnemyPart());
			enemy.addPart(new PositionPart(0, 0));
			return enemy;
		}
	}
}