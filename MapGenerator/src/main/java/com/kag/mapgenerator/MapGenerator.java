package com.kag.mapgenerator;

import com.kag.common.data.GameMap;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IMapGenerator;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

import java.util.ArrayList;
import java.util.List;

@ServiceProvider(service = IMapGenerator.class)
public class MapGenerator implements IMapGenerator {

    private int width;
    private int height;
    private final int octaves = 3;
    private List<IWorldBuilder> worldBuilders;

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
                .using(new GroundBuilder())
                .using(new WaterBuilder())
                .using(new SpawnBuilder())
                .using(new ObstacleBuilder())
                .create();
    }

    private MapGenerator using(IWorldBuilder worldBuilder) {
        worldBuilders.add(worldBuilder);
        return this;
    }

    private GameMap create() {
        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
        AssetPart asset = assetManager.createTexture(getClass().getResourceAsStream("/tilesheet.png"));
        GameMap gameMap;

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

        } while (failed);

        gameMap.setSpriteSheet(asset);
        return gameMap;

    }
}
