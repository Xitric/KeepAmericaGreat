package com.kag.mapgenerator;

import com.kag.common.map.GameMap;
import com.kag.common.map.Node;
import com.kag.common.map.World;
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
public class MapGeneratorTest {

	private static int mapRegenerateCount;

	@org.junit.Test
	public void generateMap() {
		//Arrange
		MockServices.setServices(PathfinderMock.class, AssetManagerMock.class);

		//TC#16
		//Act
		MapGenerator generator = new MapGenerator();
		GameMap map = generator.generateMap(12, 36);

		//Assert
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < map.getWidth(); x++) {
				if (!map.getTile(x, y).isOccupied()) {
					Assert.fail("The tile at (" + x + ", " + y + ") in the enemy spawn area was not occupied");
				}
			}
		}

		//TC#20
		//Assert
		Assert.assertEquals(12, map.getWidth());
		Assert.assertEquals(36, map.getHeight());

		//TC#21
		//Assert
		boolean foundObstacle = false;
		for (int y = 3; y < map.getHeight() - 3 && !foundObstacle; y++) {
			for (int x = 0; x < map.getWidth(); x++) {
				if (map.getTile(x, y).isWalkable()) {
					foundObstacle = true;
					break;
				}
			}
		}

		if(! foundObstacle) {
			Assert.fail("The map contained insufficient obstacles");
		}
	}

	@org.junit.Test(timeout = 2000l) //Fail on unexpected loop
	public void generateMapNoPath() {
		//Arrange
		MockServices.setServices(PathfinderMockFail.class, AssetManagerMock.class);

		//TC#22
		//Act
		MapGenerator generator = new MapGenerator();
		GameMap map = generator.generateMap(12, 36);

		//Assert
		//The path finder service was called multiple times, meaning the map was discarded and regenerated
		Assert.assertEquals(2, mapRegenerateCount);
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

	public static class PathfinderMockFail implements IPathFinder {

		public PathfinderMockFail() {

		}

		@Override
		public Node getPath(int startX, int startY, int endX, int endY, World world) {
			mapRegenerateCount++;
			if (mapRegenerateCount < 2) {
				return null;
			}

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