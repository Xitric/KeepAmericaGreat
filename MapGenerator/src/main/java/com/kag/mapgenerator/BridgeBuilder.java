package com.kag.mapgenerator;

import com.kag.common.data.GameMap;

/**
 * @author Kasper
 */
public class BridgeBuilder extends AbstractBuilder {

	private static final int waterTile = 51;
	private static final int bridgeTile = 46;
	private static final int bridgeTileNorth = 47;
	private static final int bridgeTileSouth = 48;

	@Override
	public boolean build(float[][] heightMap, GameMap gameMap) {
		int bridgeX = getBridgeX(gameMap.getWidth());
		boolean building = false;

		for (int y = 0; y < gameMap.getHeight(); y++) {

			if (gameMap.getTile(bridgeX, y).getLayer(0) == waterTile && building) {
				gameMap.getTile(bridgeX, y).setLayer(1, bridgeTile);
				gameMap.getTile(bridgeX, y).setWalkable(true);
			} else if (connectsNorth(gameMap, bridgeX, y)) {
				gameMap.getTile(bridgeX, y).setLayer(1, bridgeTileNorth);
				gameMap.getTile(bridgeX, y).setWalkable(true);

				//Start new bridge
				building = true;
			} else if (connectsSouth(gameMap, bridgeX, y) && building) {
				gameMap.getTile(bridgeX, y).setLayer(1, bridgeTileSouth);
				gameMap.getTile(bridgeX, y).setWalkable(true);

				//Offset next bridge
				bridgeX = getBridgeX(gameMap.getWidth());
				building = false;
			}
		}

		return true;
	}

	private int getBridgeX(int width) {
		//Prevent bridges form spawning at the edges
		return (int) (Math.random() * (width - 2) + 1);
	}

	private boolean connectsNorth(GameMap gameMap, int x, int y) {
		return gameMap.getTile(x, y).getLayer(0) == TileOrientation.S.getSpriteIndex() ||
				gameMap.getTile(x, y).getLayer(0) == TileOrientation.SW.getSpriteIndex() ||
				gameMap.getTile(x, y).getLayer(0) == TileOrientation.SE.getSpriteIndex();
	}

	private boolean connectsSouth(GameMap gameMap, int x, int y) {
		return gameMap.getTile(x, y).getLayer(0) == TileOrientation.N.getSpriteIndex() ||
				gameMap.getTile(x, y).getLayer(0) == TileOrientation.NW.getSpriteIndex() ||
				gameMap.getTile(x, y).getLayer(0) == TileOrientation.NE.getSpriteIndex();
	}
}
