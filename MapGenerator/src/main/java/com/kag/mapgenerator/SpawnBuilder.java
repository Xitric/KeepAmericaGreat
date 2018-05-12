package com.kag.mapgenerator;

import com.kag.common.map.GameMap;

public class SpawnBuilder extends AbstractBuilder {

    private final static int offset = 26;
    private final static int playerZoneWidth = 6;

    private boolean drawEnemySpawn(float[][] heightMap, GameMap gameMap) {
	    for (int y = 0; y <= 2; y++) {
		    for (int x = 0; x < heightMap[y].length; x++) {
			    if(gameMap.getTile(x, y).getLayer(0) != TileOrientation.BASE.getSpriteIndex()) {
				    return false;
			    }

			    if(x < 1 || x == heightMap[y].length - 1) {
				    if(y < 2) {
					    gameMap.getTile(x, y).setLayer(0, (x < 1 ? TileOrientation.E.getSpriteIndex() + offset : TileOrientation.W.getSpriteIndex() + offset));
				    } else {
					    gameMap.getTile(x, y).setLayer(0, (x < 1 ? TileOrientation.dNE.getSpriteIndex() + offset : TileOrientation.dNW.getSpriteIndex() + offset));
				    }
			    } else {
				    if(y < 2) {
					    gameMap.getTile(x, y).setLayer(0, TileOrientation.BASE.getSpriteIndex() + offset);
				    } else {
					    gameMap.getTile(x, y).setLayer(0, TileOrientation.N.getSpriteIndex() + offset);
				    }
			    }

			    gameMap.getTile(x, y).setOccupied(true);
		    }
	    }

	    return true;
    }

    private int getPlayerZoneStart(float[][] heightMap, GameMap gameMap) {
	    int startX = -1;
	    int width = 0;

	    for (int x = 0; x < heightMap[heightMap.length - 1].length; x++) {
		    boolean validcolumn = true;

		    for(int i = 0; i < 3; i++) {
			    if (gameMap.getTile(x, heightMap.length - 1 - i).getLayer(0) != TileOrientation.BASE.getSpriteIndex()) {
				    validcolumn = false;
				    break;
			    }
		    }

		    if(validcolumn) {
			    if(startX < 0) {
				    startX = x;
			    }
			    width++;
		    } else if (width >= playerZoneWidth) {
			    break;
		    } else {
			    startX = -1;
			    width = 0;
		    }
	    }

	    if(width < playerZoneWidth) {
		    return -1;
	    }

	    return (width - playerZoneWidth) / 2 + startX;
    }

    private void drawPlayerZone(float[][] heightMap, GameMap gameMap, int startX) {
	    for (int x = startX; x < startX + playerZoneWidth; x++){
		    for (int y = heightMap.length - 1 - 2; y < heightMap.length; y++){
			    if(x == startX || x == startX + playerZoneWidth - 1) {
				    if(y == heightMap.length - 1 - 2) {
					    gameMap.getTile(x, y).setLayer(0, (x == startX ? TileOrientation.dSE.getSpriteIndex()+13 : TileOrientation.dSW.getSpriteIndex() + 13));
				    } else {
					    gameMap.getTile(x, y).setLayer(0, (x == startX ? TileOrientation.E.getSpriteIndex()+13 : TileOrientation.W.getSpriteIndex() + 13));
				    }

			    } else {
				    if(y == heightMap.length - 1 - 2) {
					    gameMap.getTile(x, y).setLayer(0, TileOrientation.S.getSpriteIndex() + 13);
				    } else {
					    gameMap.getTile(x, y).setLayer(0, TileOrientation.BASE.getSpriteIndex() + 13);
				    }
			    }

			    gameMap.getTile(x, y).setOccupied(true);
		    }
	    }
    }

    @Override
    public boolean build(float[][] heightMap, GameMap gameMap) {
	    // If the condition is not satisfied, the enemy spawn zone cant be made, and the map is then discarded.
		if(!drawEnemySpawn(heightMap, gameMap)) {
			return false;
		}

		int startDrawingX = getPlayerZoneStart(heightMap, gameMap);

        // If no player zone is possible, discard the map
        if(startDrawingX < 0) {
        	return false;
        }

	    drawPlayerZone(heightMap, gameMap, startDrawingX);
		
		gameMap.setPlayerX(startDrawingX + playerZoneWidth / 2);
		gameMap.setPlayerY(heightMap.length - 1);
        
        return true;
    }
}
