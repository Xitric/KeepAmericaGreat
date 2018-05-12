package com.kag.core.game;

import com.kag.common.map.GameMap;
import com.kag.common.map.Node;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IMapGenerator;
import com.kag.common.spinterfaces.IPathFinder;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import org.junit.Assert;
import org.netbeans.junit.MockServices;

import java.io.InputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Kasper
 */
public class GameTest {

	private static boolean invoked = false;
	private Game game;

	@org.junit.Before
	public void setUp() throws Exception {
		//Arrange
		MockServices.setServices(MapGeneratorMock.class, PathfinderMock.class);
		game = new Game();
	}

	@org.junit.Test
	public void startNewGame() {
		//TC#19
		//Act
		game.startNewGame();

		//Assert
		Assert.assertTrue(invoked);
	}

	public static class MapGeneratorMock implements IMapGenerator {

		public MapGeneratorMock() {

		}

		@Override
		public GameMap generateMap(int width, int height) {
			invoked = true;
			return new GameMap(8, 8, 0, 0);
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
			return mock(AssetPart.class);
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