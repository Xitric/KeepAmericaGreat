package com.kag.mapgenerator;

import com.kag.common.map.GameMap;

public class GroundBuilder extends AbstractBuilder{
	@Override
	public boolean build(float[][] heightMap, GameMap gameMap) {
		for (int y = 0; y < gameMap.getHeight(); y++) {
			for(int x = 0; x < gameMap.getWidth(); x++){
				gameMap.getTile(x, y).setLayer(0, TileOrientation.BASE.getSpriteIndex());
			}
		}
		return true;
	}
}
