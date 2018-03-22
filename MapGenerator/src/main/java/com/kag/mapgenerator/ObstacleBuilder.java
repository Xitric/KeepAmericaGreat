package com.kag.mapgenerator;

import com.kag.common.data.GameMap;

public class ObstacleBuilder extends AbstractBuilder {
    private final static int spawnChance = 5;
    private final static int offset = 39;

    @Override
    public boolean build(float[][] heightMap, GameMap gameMap) {
        for (int y = 0; y < heightMap.length; y++) {
            for (int x = 0; x < heightMap[0].length; x++) {
                if (gameMap.getTile(x, y).getLayer(0) != TileOrientation.BASE.getSpriteIndex()) {
                    continue;
                }
                if ((int) (Math.random() * 100 + 1) <= spawnChance) {
                    int randomObstacle = (int) Math.random() * 7;
                    gameMap.getTile(x, y).setLayer(1, randomObstacle + offset);
                    gameMap.getTile(x, y).setWalkable(false);
                }
            }
        }

        return true;
    }
}
