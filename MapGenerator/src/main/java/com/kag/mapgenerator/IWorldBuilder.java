package com.kag.mapgenerator;

import com.kag.common.map.GameMap;

public interface IWorldBuilder {
	boolean build(float[][] heightMap, GameMap gameMap);
}
