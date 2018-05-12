package com.kag.enemywalkingcontroller;

import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.MovingPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.map.GameMap;
import com.kag.common.map.Node;
import com.kag.common.map.Tile;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.ICollision;
import com.kag.common.spinterfaces.IPathFinder;
import com.kag.commonenemy.entities.parts.EnemyPart;
import com.kag.commonenemywalking.entities.parts.WalkingPart;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.stubbing.Answer;
import org.netbeans.junit.MockServices;

import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * @author Kasper
 */
public class EnemyMovingSystemNewPathTest {

	private World world;
	private GameMap gameMap;
	private Entity enemy;

	@org.junit.Before
	public void setUp() throws Exception {
		gameMap = mock(GameMap.class);
		when(gameMap.getPathNodes()).thenAnswer(getPathNodes(8, 8));
		when(gameMap.getTileEntity()).thenReturn(new Entity());
		when(gameMap.getTileWidth()).thenReturn(64);
		when(gameMap.getTileHeight()).thenReturn(64);
		when(gameMap.doesCollideWithTile(any(), any())).thenCallRealMethod();
		world = new World(gameMap);

		enemy = new Entity();
		enemy.addPart(new EnemyPart());
		enemy.addPart(new WalkingPart());
		enemy.addPart(new MovingPart(0));
		enemy.addPart(new PositionPart(0, 0));
		enemy.addPart(new BoundingBoxPart(0, 0));

		world.addEntity(enemy);
	}

	private Answer<Node[][]> getPathNodes(int w, int h) {
		return invocationOnMock -> {
			Node[][] nodes = new Node[h][w];
			Tile tile = new Tile(128, 128);
			tile.setWalkable(false);
			for (Node[] row : nodes) {
				Arrays.fill(row, new Node(tile, null));
			}
			return nodes;
		};
	}

	@Test
	public void update() {
		//TC#27
		//Arrange
		MockServices.setServices(NoCollisionMock.class, PathfinderMock.class);

		//Act
		EnemyMovingSystem ems = new EnemyMovingSystem();
		ems.update(1, enemy, world, null);

		//Assert new path generated
		verify(gameMap, times(1)).getPathNodes();
		Assert.assertNotNull(enemy.getPart(WalkingPart.class).getNextNode());

		//TC#28
		//The collision service simulates that we hit an obstacle
		//Arrange
		MockServices.setServices(CollisionPathMock.class, PathfinderMock.class);

		//Act
		ems.update(1, enemy, world, null); //The path becomes invalidated

		//Assert
		Assert.assertNull(enemy.getPart(WalkingPart.class).getNextNode());

		//Act
		MockServices.setServices(NoCollisionMock.class, PathfinderMock.class);
		ems.update(1, enemy, world, null); //A new path is generated

		//Assert
		verify(gameMap, times(2)).getPathNodes();
		Assert.assertNotNull(enemy.getPart(WalkingPart.class).getNextNode());
	}

	public static class CollisionPathMock implements ICollision {

		public CollisionPathMock() {

		}

		@Override
		public boolean doesCollide(Entity a, Entity b) {
			return true;
		}
	}

	public static class NoCollisionMock implements ICollision {

		public NoCollisionMock() {

		}

		@Override
		public boolean doesCollide(Entity a, Entity b) {
			return false;
		}
	}

	public static class PathfinderMock implements IPathFinder {

		public PathfinderMock() {

		}

		@Override
		public Node getPath(int startX, int startY, int endX, int endY, World world) {
			return new Node(null, null);
		}

		@Override
		public Node[][] getPath(int endX, int endY, World world) {
			return new Node[0][];
		}
	}
}
