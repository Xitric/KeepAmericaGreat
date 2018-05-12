package com.kag.upgradecontroller;

import com.kag.common.data.Camera;
import com.kag.common.data.GameData;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.input.Mouse;
import com.kag.common.map.World;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commonplayer.entities.parts.PlayerPart;
import com.kag.commontd.entities.parts.MoneyPart;
import com.kag.commontower.entities.parts.WeaponPart;
import com.kag.commonupgrade.spinterfaces.IUpgrade;
import org.junit.Assert;
import org.mockito.ArgumentCaptor;
import org.netbeans.junit.MockServices;

import java.io.InputStream;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * @author Kasper
 */
public class UpgradeControllerTest {

	private World world;
	private GameData gameData;
	private Mouse mouse;
	private Entity tower;
	private Entity player;

	@org.junit.Before
	public void setUp() throws Exception {
		//Arrange
		MockServices.setServices(AssetManagerMock.class, UpgradeMock.class);

		tower = new Entity();
		tower.addPart(new MoneyPart(0));
		tower.addPart(new PositionPart(0, 0));
		tower.addPart(new WeaponPart(0, 0, 0));

		player = new Entity();
		player.addPart(new PlayerPart());
		player.addPart(new MoneyPart(15));

		world = mock(World.class);
		when(world.getEntityAt(anyFloat(), anyFloat())).thenReturn(tower);
		when(world.getEntitiesByFamily(any())).thenReturn(Collections.singletonList(player));

		Camera cam = mock(Camera.class);
		when(cam.getX()).thenReturn(480f);
		when(cam.getX()).thenReturn(320f);

		mouse = mock(Mouse.class);
		when(mouse.getX()).thenReturn(0);
		when(mouse.getY()).thenReturn(0);
		when(mouse.isButtonPressed(anyInt())).thenReturn(true);

		gameData = mock(GameData.class);
		when(gameData.getCamera()).thenReturn(cam);
		when(gameData.getWidth()).thenReturn(960);
		when(gameData.getHeight()).thenReturn(640);
		when(gameData.getMouse()).thenReturn(mouse);
	}

	@org.junit.Test
	public void update() {
		//TC#34
		//Act
		UpgradeController uc = new UpgradeController();
		uc.update(0, world, gameData);

		//Assert
		ArgumentCaptor<Entity> captor = ArgumentCaptor.forClass(Entity.class);
		verify(world, times(1)).addEntity(captor.capture());

		Entity upgradeEntity = captor.getValue();
		Assert.assertEquals(788f, upgradeEntity.getPart(AbsolutePositionPart.class).getX());
		Assert.assertEquals(410f, upgradeEntity.getPart(AbsolutePositionPart.class).getY());

		//TC#35
		//Arrange
		when(mouse.getX()).thenReturn(788 + 24);
		when(mouse.getY()).thenReturn(410 + 24);

		//Act
		uc.update(0, world, gameData);

		//Assert
		Assert.assertEquals(5, player.getPart(MoneyPart.class).getMoney());
		Assert.assertEquals(10f, tower.getPart(WeaponPart.class).getAttackSpeed());

		//TC#36
		//Act
		uc.update(0, world, gameData);

		//Assert
		Assert.assertEquals(5, player.getPart(MoneyPart.class).getMoney());
		Assert.assertEquals(10f, tower.getPart(WeaponPart.class).getAttackSpeed());
	}

	public static class UpgradeMock implements IUpgrade {

		public UpgradeMock() {

		}

		@Override
		public IAsset getAsset() {
			IAsset asset = mock(IAsset.class);
			when(asset.getWidth()).thenReturn(48);
			when(asset.getHeight()).thenReturn(48);
			return asset;
		}

		@Override
		public boolean isTowerCompatible(Entity entity) {
			return true;
		}

		@Override
		public void upgrade(Entity entity) {
			entity.getPart(WeaponPart.class).setAttackSpeed(entity.getPart(WeaponPart.class).getAttackSpeed() + 10);
		}

		@Override
		public int getCost(Entity entity) {
			return 10;
		}
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
			AssetPart assetPart = mock(AssetPart.class);
			when(assetPart.getWidth()).thenReturn(width);
			when(assetPart.getHeight()).thenReturn(height);
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
			return null;
		}
	}
}