package com.kag.mapgenerator;

import com.kag.common.map.GameMap;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IMapGenerator;
import com.kag.common.spinterfaces.IPathFinder;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

import java.util.ArrayList;
import java.util.List;

@ServiceProvider(service = IMapGenerator.class)
public class MapGenerator implements IMapGenerator {

	private final static int octaves = 3;
	private int width;
	private int height;
	private List<IWorldBuilder> worldBuilders;
	private IAsset spriteSheet;

	public MapGenerator() {
		//Not to be used.
	}

	private MapGenerator(int width, int height) {
		this.width = width;
		this.height = height;
		worldBuilders = new ArrayList<>();
	}

	private static MapGenerator newWorld(int width, int height) {
		return new MapGenerator(width, height);
	}

	@Override
	public GameMap generateMap(int width, int height) {
		return MapGenerator.newWorld(width, height)
				.using(new WaterBuilder())
				.using(new SpawnBuilder())
				.using(new BridgeBuilder())
				.using(new ObstacleBuilder())
				.create();
	}

	private MapGenerator using(IWorldBuilder worldBuilder) {
		worldBuilders.add(worldBuilder);
		return this;
	}

	private GameMap create() {
		AssetPart asset = getSpriteSheet();
		GameMap gameMap;
		IPathFinder pathFinder = Lookup.getDefault().lookup(IPathFinder.class);

		boolean failed;
		do {
			failed = false;
			gameMap = new GameMap(width, height, 64, 64);
			float[][] whiteNoise = PerlinNoiseGenerator.generateWhiteNoise(gameMap.getHeight(), gameMap.getWidth());
			float[][] heightMap = PerlinNoiseGenerator.generateSmoothNoise(whiteNoise, octaves);

			for (IWorldBuilder worldBuilder : worldBuilders) {
				if (!worldBuilder.build(heightMap, gameMap)) {
					//Map failed to build, create a new one
					failed = true;
					break;
				}
			}

			if (!failed) {
				World tempWorld = new World(gameMap);

				if (pathFinder != null) {
					if (pathFinder.getPath(0, 0, gameMap.getPlayerX(), gameMap.getPlayerY(), tempWorld) == null) {
						failed = true;
					} else {
						gameMap.setPathNodes(pathFinder.getPath(gameMap.getPlayerX(), gameMap.getPlayerY(), tempWorld));
					}
				}
			}

		} while (failed);
		gameMap.getTileEntity().addPart(asset);
		return gameMap;
	}

	private AssetPart getSpriteSheet() {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		if (spriteSheet == null) {
			spriteSheet = assetManager.loadAsset(getClass().getResourceAsStream("/tilesheet.png"));
		}

		return assetManager.createTexture(spriteSheet, 0, 0, spriteSheet.getWidth(), spriteSheet.getHeight());
	}
}
