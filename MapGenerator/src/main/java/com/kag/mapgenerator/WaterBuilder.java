package com.kag.mapgenerator;

import com.kag.common.data.GameMap;

/**
 * @author Kasper
 */
public class WaterBuilder extends AbstractBuilder {

    private static final int waterTile = 51;

    @Override
    public boolean build(float[][] heightMap, GameMap gameMap) {
        for (int y = 0; y < heightMap.length; y++) {
            for (int x = 0; x < heightMap[y].length; x++) {

                if (!isGroundLevel(heightMap, x, y)) {
                    gameMap.getTile(x, y).setLayer(0, waterTile);
                    gameMap.getTile(x, y).setWalkable(false);
                } else {
                    gameMap.getTile(x, y).setLayer(0, getGroundTileOrientation(heightMap, x, y).getSpriteIndex());
                }
            }
        }

        return true;
    }
}
