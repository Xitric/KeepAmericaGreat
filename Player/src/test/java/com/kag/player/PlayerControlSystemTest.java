package com.kag.player;

import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.map.GameMap;
import com.kag.common.map.World;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.entities.parts.LabelPart;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commonplayer.entities.parts.PlayerPart;
import com.kag.commontd.entities.parts.LifePart;
import com.kag.commontd.entities.parts.MoneyPart;
import com.kag.commontower.entities.parts.TowerPart;
import org.junit.Assert;
import org.netbeans.junit.MockServices;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Kasper
 */
public class PlayerControlSystemTest {

	private World world;
	private List<Entity> towers;

	@org.junit.Before
	public void setUp() throws Exception {
		//Arrange
		MockServices.setServices(AssetManagerMock.class);

		world = new World(new GameMap(8, 8, 0, 0));

		towers = new ArrayList<>();
		towers.add(makeTower());
		towers.add(makeTower());
		towers.add(makeTower());
		towers.add(makeTower());

		for (Entity tower : towers) {
			world.addEntity(tower);
		}
	}

	private Entity makeTower() {
		Entity tower = new Entity();
		tower.addPart(new TowerPart(null));
		return tower;
	}

	@org.junit.Test
	public void load() {
		//TC#18
		//Act
		PlayerControlSystem pcs = new PlayerControlSystem();
		pcs.load(world);

		//Assert
		Entity player = world.getEntitiesByFamily(Family.forAll(PlayerPart.class)).stream().findFirst().orElse(null);
		String health = String.valueOf(player.getPart(LifePart.class).getHealth());
		String money = String.valueOf(player.getPart(MoneyPart.class).getMoney());

		List<Entity> labels = new ArrayList<>(world.getEntitiesByFamily(Family.forAll(LabelPart.class)));

		//Shows both health and money
		if (labels.get(0).getPart(LabelPart.class).getLabel().equals(health)) {
			Assert.assertEquals(money, labels.get(1).getPart(LabelPart.class).getLabel());
		} else if (labels.get(0).getPart(LabelPart.class).getLabel().equals(money)) {
			Assert.assertEquals(health, labels.get(1).getPart(LabelPart.class).getLabel());
		} else {
			Assert.fail("Player labels constructed incorrectly");
		}

		//Positioned correctly
		for (Entity label : labels) {
			Assert.assertTrue(label.getPart(AbsolutePositionPart.class).getX() > 480);
			Assert.assertTrue(label.getPart(AbsolutePositionPart.class).getY() < 320);
		}
	}

	@org.junit.Test
	public void deathTest() {
		//TC#37
		//Arrange
		PlayerControlSystem pcs = new PlayerControlSystem();
		pcs.load(world);

		//Act
		Entity player = world.getEntitiesByFamily(Family.forAll(PlayerPart.class)).stream().findFirst().orElse(null);
		player.getPart(LifePart.class).setHealth(0);
		pcs.update(0, player, world, null);

		//Assert
		//Towers removed
		for (Entity tower : towers) {
			Assert.assertFalse(world.getAllEntities().contains(tower));
		}

		//Player removed
		Assert.assertFalse(world.getAllEntities().contains(player));
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