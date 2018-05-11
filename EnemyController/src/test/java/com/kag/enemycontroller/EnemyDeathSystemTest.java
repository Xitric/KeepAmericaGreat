package com.kag.enemycontroller;

import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.map.GameMap;
import com.kag.common.map.World;
import com.kag.commonaudio.spinterfaces.IAudioManager;
import com.kag.commonaudio.spinterfaces.ISound;
import com.kag.commonenemy.entities.parts.EnemyPart;
import com.kag.commontd.entities.parts.LifePart;
import com.kag.commontd.entities.parts.MoneyPart;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.junit.MockServices;

import java.io.InputStream;

import static org.mockito.Mockito.mock;

/**
 * @author Kasper
 */
public class EnemyDeathSystemTest {

	private World world;
	private Entity enemy;
	private Entity player;

	@Before
	public void setUp() throws Exception {
		//Arrange
		world = new World(new GameMap(8, 8, 0, 0));

		enemy = new Entity();
		enemy.addPart(new EnemyPart());
		enemy.addPart(new LifePart(1));
		enemy.addPart(new MoneyPart(10));

		player = new Entity();
		player.addPart(new MoneyPart(0));
		player.addPart(new LifePart(0));
		player.addPart(new PositionPart(0, 0));
		player.addPart(new BoundingBoxPart(0, 0));

		world.addEntity(enemy);
		world.addEntity(player);

		MockServices.setServices(AudioManagerMock.class);
	}

	@Test
	public void update() {
		EnemyDeathSystem eds = new EnemyDeathSystem();
		eds.load(world);

		//Act
		eds.update(0, enemy, world, null);

		//Assert
		//TC#8
		Assert.assertEquals(0, player.getPart(MoneyPart.class).getMoney());

		//Act
		enemy.getPart(LifePart.class).setHealth(0);
		eds.update(0, enemy, world, null);

		//Assert
		//TC#9
		Assert.assertEquals(enemy.getPart(MoneyPart.class).getMoney(), player.getPart(MoneyPart.class).getMoney());
	}

	public static class AudioManagerMock implements IAudioManager {

		public AudioManagerMock() {

		}

		@Override
		public ISound loadSound(InputStream input, String extension) {
			return mock(ISound.class);
		}
	}
}