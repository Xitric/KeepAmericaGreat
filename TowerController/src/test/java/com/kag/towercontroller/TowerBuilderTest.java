package com.kag.towercontroller;

import com.kag.common.data.Camera;
import com.kag.common.data.GameData;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.BlockingPart;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.input.Mouse;
import com.kag.common.map.GameMap;
import com.kag.common.map.Node;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.ICollision;
import com.kag.common.spinterfaces.IPathFinder;
import com.kag.commonplayer.entities.parts.PlayerPart;
import com.kag.commontd.entities.parts.MoneyPart;
import com.kag.commontower.spinterfaces.ITower;
import org.junit.Assert;
import org.mockito.stubbing.Answer;
import org.netbeans.junit.MockServices;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Kasper
 */
public class TowerBuilderTest {

	private World world;
	private GameData gameData;
	private Entity towerPreview;
	private Entity player;
	private Entity lastTower;
	private Mouse mouse;

	@org.junit.Before
	public void setUp() throws Exception {
		//Arrange
		world = new World(new GameMap(8, 8, 32, 32));
		world.getGameMap().getTile(0, 0).setOccupied(true);

		gameData = mock(GameData.class);
		mouse = mock(Mouse.class);
		when(gameData.getMouse()).thenReturn(mouse);
		when(gameData.getWidth()).thenReturn(0);
		when(gameData.getHeight()).thenReturn(0);

		Camera camera = mock(Camera.class);
		when(gameData.getCamera()).thenReturn(camera);
		when(camera.getX()).thenReturn(0f);
		when(camera.getY()).thenReturn(0f);

		ITower towerFactory = mock(ITower.class);
		when(towerFactory.create()).thenAnswer(getTowerMock());

		towerPreview = mock(Entity.class);
		when(towerPreview.getPart(TowerBuyMenuPart.class)).thenReturn(new TowerBuyMenuPart(towerFactory));

		player = new Entity();
		player.addPart(new PlayerPart());
		player.addPart(new MoneyPart(15));
		world.addEntity(player);

		MockServices.setServices(TowerServiceMock.class);
	}

	private Answer getTowerMock() {
		return invocation -> {
			lastTower = new Entity();
			lastTower.addPart(new MoneyPart(10));
			lastTower.addPart(new PositionPart(0, 0));
			return lastTower;
		};
	}

	@org.junit.Test
	public void tryPlaceTowerValid() {
		TowerBuilder builder = new TowerBuilder();

		//TC#10
		//Arrange
		when(mouse.getX()).thenReturn(32);
		when(mouse.getY()).thenReturn(0);

		//Act
		builder.tryPlaceTower(towerPreview, world, gameData);

		//Assert
		Assert.assertTrue(world.getAllEntities().contains(lastTower));

		//TC#13
		//Assert
		Assert.assertEquals(5, player.getPart(MoneyPart.class).getMoney());

		//TC#14
		//Arrange
		when(mouse.getX()).thenReturn(64);

		//Act
		builder.tryPlaceTower(towerPreview, world, gameData);

		//Assert
		Assert.assertFalse(world.getAllEntities().contains(lastTower));
		Assert.assertEquals(5, player.getPart(MoneyPart.class).getMoney());
	}

	@org.junit.Test
	public void tryPlaceTowerOnEnemy() {
		TowerBuilder builder = new TowerBuilder();

		//TC#11
		//Arrange
		when(mouse.getX()).thenReturn(32);
		when(mouse.getY()).thenReturn(0);

		Entity enemy = new Entity();
		enemy.addPart(new PositionPart(32, 0));
		enemy.addPart(new BoundingBoxPart(32, 32));
		enemy.addPart(new BlockingPart());
		world.addEntity(enemy);

		MockServices.setServices(CollisionMock.class);

		//Act
		builder.tryPlaceTower(towerPreview, world, gameData);

		//Assert
		Assert.assertFalse(world.getAllEntities().contains(lastTower));
		Assert.assertEquals(15, player.getPart(MoneyPart.class).getMoney());
	}

	@org.junit.Test
	public void tryPlaceTowerOccupied() {
		TowerBuilder builder = new TowerBuilder();

		//TC#12
		//Arrange
		when(mouse.getX()).thenReturn(0);
		when(mouse.getY()).thenReturn(0);

		//Act
		builder.tryPlaceTower(towerPreview, world, gameData);

		//Assert
		Assert.assertFalse(world.getAllEntities().contains(lastTower));
		Assert.assertEquals(15, player.getPart(MoneyPart.class).getMoney());
	}

	@org.junit.Test
	public void tryPlaceTowerBlockRoutes() {
		TowerBuilder builder = new TowerBuilder();

		//TC#15
		//Arrange
		when(mouse.getX()).thenReturn(32);
		when(mouse.getY()).thenReturn(0);

		//Simulate blocking all paths
		MockServices.setServices(PathfinderMock.class);

		//Act
		builder.tryPlaceTower(towerPreview, world, gameData);

		//Assert
		Assert.assertFalse(world.getAllEntities().contains(lastTower));
		Assert.assertEquals(5, player.getPart(MoneyPart.class).getMoney());
	}

	public static class TowerServiceMock extends TowerService {

		public TowerServiceMock() {

		}

		@Override
		public void towerCreated(Entity tower) {

		}
	}

	public static class CollisionMock implements ICollision {

		public CollisionMock() {

		}

		@Override
		public boolean doesCollide(Entity a, Entity b) {
			return true;
		}
	}

	public static class PathfinderMock implements IPathFinder {

		public PathfinderMock() {

		}

		@Override
		public Node getPath(int startX, int startY, int endX, int endY, World world) {
			return null;
		}

		@Override
		public Node[][] getPath(int endX, int endY, World world) {
			return new Node[0][];
		}
	}
}