package com.kag.mapgenerator;

public abstract class AbstractBuilder implements IWorldBuilder {

	private static final int waterLevel = 70;

	boolean isGroundLevel(float[][] heightMap, int x, int y) {
		if (y < 0 || y >= heightMap.length || x < 0 || x >= heightMap[0].length) {
			return true;
		}
		return heightMap[y][x] * 255 - waterLevel > 0;
	}

	TileOrientation getGroundTileOrientation(float[][] heightMap, int x, int y) {
		if (!isGroundLevel(heightMap, x, y + 1)) {
			if (!isGroundLevel(heightMap, x + 1, y)) {
				return TileOrientation.SE;
			} else if (!isGroundLevel(heightMap, x - 1, y)) {
				return TileOrientation.SW;
			}

			return TileOrientation.S;
		} else if (!isGroundLevel(heightMap, x + 1, y)) {
			if (!isGroundLevel(heightMap, x, y - 1)) {
				return TileOrientation.NE;
			}

			return TileOrientation.E;
		} else if (!isGroundLevel(heightMap, x, y - 1)) {
			if (!isGroundLevel(heightMap, x - 1, y)) {
				return TileOrientation.NW;
			}

			return TileOrientation.N;
		} else if (!isGroundLevel(heightMap, x - 1, y)) {
			return TileOrientation.W;
		}

		if (!isGroundLevel(heightMap, x + 1, y + 1)) {
			return TileOrientation.dSE;
		} else if (!isGroundLevel(heightMap, x + 1, y - 1)) {
			return TileOrientation.dNE;
		} else if (!isGroundLevel(heightMap, x - 1, y - 1)) {
			return TileOrientation.dNW;
		} else if (!isGroundLevel(heightMap, x - 1, y + 1)) {
			return TileOrientation.dSW;
		}

		return TileOrientation.BASE;
	}

}
