package com.kag.core.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kag.common.data.GameData;
import com.kag.common.map.GameMap;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IMapGenerator;
import com.kag.common.spinterfaces.ISystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.junit.MockServices;
import org.openide.util.Lookup;

/**
 * @author Kasper
 */
public class SystemManagerTest {

	private SystemManager systemManager;
	private World world;

	@Before
	public void setUp() throws Exception {
		//Arrange
		MockServices.setServices(MapGeneratorMock.class);

		//We need an OpenGL environment
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Keep America Great";
		cfg.width = 960;
		cfg.height = 640;
		cfg.useGL30 = false;
		cfg.resizable = false;

		new LwjglApplication(Lookup.getDefault().lookup(Game.class), cfg);

		systemManager = new SystemManager();

		//We just create a new world instance to avoid null pointer exceptions
		world = new World(new GameMap(0, 0, 0, 0));
	}

	@Test
	public void getActiveSystems() throws InterruptedException {
		//TC#25
		//Act
		MockServices.setServices(MapGeneratorMock.class, SystemMock.class);
		Thread.sleep(500);
		systemManager.update(world);

		//Assert
		SystemMock mock = Lookup.getDefault().lookup(SystemMock.class);
		Assert.assertTrue(systemManager.getActiveSystems().contains(mock));

		//TC#26
		//Act
		MockServices.setServices(MapGeneratorMock.class);
		Thread.sleep(500);
		systemManager.update(world);

		//Assert
		mock = Lookup.getDefault().lookup(SystemMock.class);
		Assert.assertFalse(systemManager.getActiveSystems().contains(mock));
	}

	public static class SystemMock implements ISystem {

		public SystemMock() {

		}

		@Override
		public void update(float dt, World world, GameData gameData) {

		}

		@Override
		public int getPriority() {
			return 0;
		}
	}

	public static class MapGeneratorMock implements IMapGenerator {

		public MapGeneratorMock() {

		}

		@Override
		public GameMap generateMap(int width, int height) {
			return new GameMap(8, 8, 0, 0);
		}
	}
}