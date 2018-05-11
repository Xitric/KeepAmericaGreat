package com.kag.towercontroller;

import com.kag.common.data.GameData;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.input.Mouse;
import com.kag.common.map.GameMap;
import com.kag.common.map.World;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commonplayer.entities.parts.PlayerPart;
import com.kag.commontd.entities.parts.MoneyPart;
import com.kag.commontower.spinterfaces.ITowerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.junit.MockServices;

import java.io.InputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Kasper
 */
public class TowerSellManagerTest {

	private World world;
	private GameData gameData;
	private static Entity tower;
	private Entity player;

	@Before
	public void setUp() throws Exception {
		//Arrange
		MockServices.setServices(AssetManagerMock.class, TowerServiceMock.class);

		world = new World(new GameMap(8, 8, 0, 0));

		Mouse mouse = mock(Mouse.class);
		when(mouse.isButtonPressed(Mouse.BUTTON_LEFT)).thenReturn(true);
		when(mouse.getX()).thenReturn(700);
		when(mouse.getY()).thenReturn(600);

		gameData = mock(GameData.class);
		when(gameData.getMouse()).thenReturn(mouse);

		tower = new Entity();
		tower.addPart(new MoneyPart(10));
		tower.addPart(new PositionPart(0, 0));

		player = new Entity();
		player.addPart(new PlayerPart());
		player.addPart(new MoneyPart(0));

		world.addEntity(tower);
		world.addEntity(player);
	}

	@Test
	public void update() {
		TowerSellManager tsm = new TowerSellManager();
		tsm.load(world);

		//TC#17
		//Act
		tsm.towerSelected(tower);
		tsm.update(0, world, gameData);

		//Assert
		Assert.assertFalse(world.getAllEntities().contains(tower));
		Assert.assertEquals(7, player.getPart(MoneyPart.class).getMoney());
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
			AssetPart asset = mock(AssetPart.class);
			when(asset.getWidth()).thenReturn(64);
			when(asset.getHeight()).thenReturn(64);
			return asset;
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

	public static class TowerServiceMock implements ITowerService {

		public TowerServiceMock() {

		}

		@Override
		public Entity getSelectedTower() {
			return tower;
		}

		@Override
		public void towerCreated(Entity tower) {

		}

		@Override
		public void towerRemoved(Entity tower) {

		}

		@Override
		public void towerSelected(Entity tower) {

		}

		@Override
		public void towerDeselected(Entity tower) {

		}
	}
}