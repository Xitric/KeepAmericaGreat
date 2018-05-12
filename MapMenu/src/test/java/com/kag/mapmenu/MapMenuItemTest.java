package com.kag.mapmenu;

import com.kag.common.entities.Entity;
import com.kag.common.map.GameMap;
import com.kag.common.map.World;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commonplayer.entities.parts.PlayerPart;
import org.junit.Assert;
import org.netbeans.junit.MockServices;

import java.io.InputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Kasper
 */
public class MapMenuItemTest {

	private World world;
	private Entity player;
	private MapMenuItem mmi;

	@org.junit.Before
	public void setUp() throws Exception {
		//Arrange
		MockServices.setServices(AssetManagerMock.class);

		world = new World(new GameMap(8, 8, 0, 0));

		player = new Entity();
		player.addPart(new PlayerPart());
		world.addEntity(player);

		mmi = new MapMenuItem();
		mmi.load(world);
	}

	@org.junit.Test
	public void update() {
		//TC#38
		//Act
		mmi.update(0, world, null);

		//Assert
		Assert.assertTrue(world.getAllEntities().contains(player));
		Assert.assertEquals(2, world.getAllEntities().size());

		//TC#39
		//Act
		world.removeEntity(player);
		mmi.update(0, world, null);

		//Assert
		Assert.assertFalse(world.getAllEntities().contains(player));
		Assert.assertEquals(2, world.getAllEntities().size());
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
			return null;
		}

		@Override
		public AssetPart createTexture(InputStream input) {
			AssetPart asset = mock(AssetPart.class);
			when(asset.getWidth()).thenReturn(0);
			when(asset.getHeight()).thenReturn(0);
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
}