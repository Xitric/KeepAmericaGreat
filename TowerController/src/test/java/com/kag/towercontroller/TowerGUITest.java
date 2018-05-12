package com.kag.towercontroller;

import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.map.GameMap;
import com.kag.common.map.World;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commontower.spinterfaces.ITower;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.junit.MockServices;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Kasper
 */
public class TowerGUITest {

	private World world;
	private TowerGUI towerGUI;

	@Before
	public void setUp() throws Exception {
		//Arrange
		MockServices.setServices(AssetManagerMock.class);

		world = new World(new GameMap(8, 8, 0, 0));
		towerGUI = new TowerGUI();
		towerGUI.load(world);
	}

	@Test
	public void buyMenuTest() {
		//TC#32
		//Act
		MockServices.setServices(AssetManagerMock.class, Tower1.class);
		towerGUI.update(0, world, null);

		//Assert
		Collection<Entity> buyElements = world.getEntitiesByFamily(Family.forAll(TowerBuyMenuPart.class));
		Assert.assertEquals(1, buyElements.size());

		Entity buyMenuelement = buyElements.stream().findFirst().orElse(null);
		Assert.assertTrue(isAt(buyMenuelement, 812, 178));

		//Act
		MockServices.setServices(AssetManagerMock.class, Tower1.class, Tower2.class);
		towerGUI.update(0, world, null);

		//Assert
		List<Entity> buyMenuElements = new ArrayList<>(world.getEntitiesByFamily(Family.forAll(TowerBuyMenuPart.class)));
		Assert.assertEquals(2, buyMenuElements.size());

		if (isAt(buyMenuElements.get(0), 812, 178)) {
			Assert.assertTrue(isAt(buyMenuElements.get(1), 812 + 52, 178));
		} else if (isAt(buyMenuElements.get(0), 812 + 52, 178)) {
			Assert.assertTrue(isAt(buyMenuElements.get(1), 812, 178));
		} else {
			Assert.fail("Tower buy menu elements positioned incorrectly");
		}

		//TC#33
		//Act
		MockServices.setServices(AssetManagerMock.class, Tower2.class);
		towerGUI.update(0, world, null);

		//Assert
		buyElements = world.getEntitiesByFamily(Family.forAll(TowerBuyMenuPart.class));
		Assert.assertEquals(1, buyElements.size());

		buyMenuelement = buyElements.stream().findFirst().orElse(null);
		Assert.assertTrue(isAt(buyMenuelement, 812, 178));
	}

	private boolean isAt(Entity e, float x, float y) {
		if (e == null) return false;

		AbsolutePositionPart pos = e.getPart(AbsolutePositionPart.class);
		return pos.getX() == x && pos.getY() == y;
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
			when(assetPart.getWidth()).thenReturn(48);
			when(assetPart.getHeight()).thenReturn(48);
			return assetPart;
		}

		@Override
		public AssetPart createTexture(InputStream input) {
			AssetPart assetPart = mock(AssetPart.class);
			when(assetPart.getWidth()).thenReturn(0);
			when(assetPart.getHeight()).thenReturn(0);
			return assetPart;
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

	public static class Tower1 implements ITower {

		public Tower1() {

		}

		@Override
		public IAsset getAsset() {
			IAsset asset = mock(IAsset.class);
			when(asset.getWidth()).thenReturn(48);
			when(asset.getHeight()).thenReturn(48);
			return asset;
		}

		@Override
		public Entity create() {
			return null;
		}

		@Override
		public IAsset getProjectileAsset() {
			return null;
		}

		@Override
		public int getCost() {
			return 0;
		}
	}

	public static class Tower2 implements ITower {

		public Tower2() {

		}

		@Override
		public IAsset getAsset() {
			IAsset asset = mock(IAsset.class);
			when(asset.getWidth()).thenReturn(48);
			when(asset.getHeight()).thenReturn(48);
			return asset;
		}

		@Override
		public Entity create() {
			return null;
		}

		@Override
		public IAsset getProjectileAsset() {
			return null;
		}

		@Override
		public int getCost() {
			return 0;
		}
	}
}