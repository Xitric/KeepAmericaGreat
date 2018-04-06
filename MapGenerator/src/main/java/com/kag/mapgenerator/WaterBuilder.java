package com.kag.mapgenerator;

import com.kag.common.data.GameMap;

/**
 * @author Kasper
 */
public class WaterBuilder extends AbstractBuilder {

	private static final int waterTile = 51;

	@Override
	public boolean build(float[][] heightMap, GameMap gameMap) {
		//Fix illegal ground tiles
		for (int y = 0; y < gameMap.getHeight(); y++) {
			for (int x = 0; x < gameMap.getWidth(); x++) {
				fixTile(heightMap, x, y);
			}
		}

		//Build tiles
		for (int y = 0; y < gameMap.getHeight(); y++) {
			for (int x = 0; x < gameMap.getWidth(); x++) {

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

	private void fixTile(float[][] heightMap, int x, int y) {
		//Water tiles are always legal
		if (!isGroundLevel(heightMap, x, y)) return;

		if (isWaterOnOppositeSides(heightMap, x, y) || isSurroundedByWater(heightMap, x, y)) {
			//Submerge tile in water
			heightMap[y][x] = 0;

			//Call neighbors as they might have become illegal
			fixTile(heightMap, x - 1, y);
			fixTile(heightMap, x + 1, y);
			fixTile(heightMap, x, y - 1);
			fixTile(heightMap, x, y + 1);
		}
	}

	private boolean isWaterOnOppositeSides(float[][] heightMap, int x, int y) {
		return (!isGroundLevel(heightMap, x - 1, y) && !isGroundLevel(heightMap, x + 1, y)) ||
				(!isGroundLevel(heightMap, x, y - 1) && !isGroundLevel(heightMap, x, y + 1));
	}

	private boolean isSurroundedByWater(float[][] heightMap, int x, int y) {
		int waterCount = 0;

		if (!isGroundLevel(heightMap, x - 1, y)) waterCount++;
		if (!isGroundLevel(heightMap, x + 1, y)) waterCount++;
		if (!isGroundLevel(heightMap, x, y - 1)) waterCount++;
		if (!isGroundLevel(heightMap, x, y + 1)) waterCount++;

		//If there are more than three tiles of water around a tile, it is illegal
		return waterCount >= 3;
	}
}
