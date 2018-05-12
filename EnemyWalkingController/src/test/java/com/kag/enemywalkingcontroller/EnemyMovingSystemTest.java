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
import com.kag.commonenemy.entities.parts.EnemyPart;
import com.kag.commonenemywalking.entities.parts.WalkingPart;
import com.kag.commonplayer.entities.parts.PlayerPart;
import com.kag.commontd.entities.parts.LifePart;
import org.junit.Assert;
import org.netbeans.junit.MockServices;

/**
 * @author Kasper
 */
public class EnemyMovingSystemTest {

	private World world;
	private Entity enemy;
	private Entity player;
	private int initialLife;

	@org.junit.Before
	public void setUp() throws Exception {
		//Arrange
		world = new World(new GameMap(8, 8, 16, 16));

		enemy = new Entity();
		enemy.addPart(new EnemyPart());
		enemy.addPart(new WalkingPart());
		enemy.addPart(new MovingPart(0));
		enemy.addPart(new PositionPart(0, 0));
		enemy.addPart(new BoundingBoxPart(0, 0));
		enemy.getPart(WalkingPart.class).setNextNode(new Node(new Tile(0, 0), null));

		initialLife = 10;
		player = new Entity();
		player.addPart(new PlayerPart());
		player.addPart(new PositionPart(0, 0));
		player.addPart(new BoundingBoxPart(0, 0));
		player.addPart(new LifePart(initialLife));

		world.addEntity(enemy);
		world.addEntity(player);
	}

	@org.junit.Test
	public void updateCollision() {
		//Act
		MockServices.setServices(CollisionMock.class);
		EnemyMovingSystem ems = new EnemyMovingSystem();
		ems.update(0.0167f, enemy, world, null);

		//Assert
		//TC#4
		Assert.assertFalse(world.getAllEntities().contains(enemy));

		//TC#5
		Assert.assertEquals(initialLife - 1, player.getPart(LifePart.class).getHealth());
	}

	@org.junit.Test
	public void updateNoCollision() {
		//Act
		MockServices.setServices(NoCollisionMock.class);
		EnemyMovingSystem ems = new EnemyMovingSystem();
		ems.update(0.0167f, enemy, world, null);

		//Assert
		//TC#6
		Assert.assertTrue(world.getAllEntities().contains(enemy));
		Assert.assertEquals(initialLife, player.getPart(LifePart.class).getHealth());
	}

	public static class CollisionMock implements ICollision {

		public CollisionMock() {

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
}