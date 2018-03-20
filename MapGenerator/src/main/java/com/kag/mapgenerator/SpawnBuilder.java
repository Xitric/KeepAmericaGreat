/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.mapgenerator;

import com.kag.common.data.GameMap;

/**
 *
 * @author mstruntze
 */
public class SpawnBuilder extends AbstractBuilder {

    @Override
    public boolean build(float[][] heightMap, GameMap gameMap) {
        for (int y = 0; y <= 2; y++) {
            for (int x = 0; x < heightMap[y].length; x++) {
                if(gameMap.getTile(x, y).getLayer(0) != TileOrientation.BASE.getSpriteIndex()) {
                    return false;
                }
                
                if(x < 1 || x == heightMap[y].length - 1) {
                    if(y < 2) {
                       gameMap.getTile(x, y).setLayer(0, (x < 1 ? TileOrientation.E.getSpriteIndex() + 26 : TileOrientation.W.getSpriteIndex() + 26)); 
                    } else {
                        gameMap.getTile(x, y).setLayer(0, (x < 1 ? TileOrientation.dNE.getSpriteIndex() + 26 : TileOrientation.dNW.getSpriteIndex() + 26)); 
                    }
                } else {
                    if(y < 2) {
                       gameMap.getTile(x, y).setLayer(0, TileOrientation.BASE.getSpriteIndex() + 26);  
                    } else {
                       gameMap.getTile(x, y).setLayer(0, TileOrientation.N.getSpriteIndex() + 26);
                    }
                }
            }
        }
        
        int startX = -1;
        int width = 0;
        
        int bestX = -1;
        int bestWidth = 0;
        
        int playerZoneWidth = 6;
        
        for(int x = 0; x < heightMap[heightMap.length - 1].length; x++) {
            if(gameMap.getTile(x, heightMap.length - 1).getLayer(0) == TileOrientation.BASE.getSpriteIndex()) {
                if(startX < 0) {
                    startX = x;
                    width = 1;
                } else {
                    width++;
                }
                
                for(int i = 1; i < 3; i++) {
                    if(gameMap.getTile(x, heightMap.length - 1 - i).getLayer(0) != TileOrientation.BASE.getSpriteIndex()) {
                        if(width - 1 >= playerZoneWidth) {
                            bestX = startX;
                            bestWidth = width - 1;
                        }
                        
                        startX = -1;
                        width = 0;
                        break;
                    }
                }
                
                if(bestX > 0 && startX < 0) {
                    break;
                }
            }
        }
        
        if(bestWidth < playerZoneWidth && width < playerZoneWidth) {
            return false;
        } else if(width >= playerZoneWidth && bestWidth == 0) {
            bestWidth = width;
            bestX = startX;
        }
        
        // Calculate start drawing X
        int startDrawingX = (bestWidth - playerZoneWidth) / 2 + bestX;
        
        for (int x = startDrawingX; x < startDrawingX + playerZoneWidth; x++){
            for (int y = heightMap.length - 1 - 2; y < heightMap.length; y++){
                if(x == startDrawingX || x == startDrawingX + playerZoneWidth - 1) {
                    if(y == heightMap.length - 1 - 2) {
                       gameMap.getTile(x, y).setLayer(0, (x == startDrawingX ? TileOrientation.dSE.getSpriteIndex()+13 : TileOrientation.dSW.getSpriteIndex() + 13));  
                    } else {
                       gameMap.getTile(x, y).setLayer(0, (x == startDrawingX ? TileOrientation.E.getSpriteIndex()+13 : TileOrientation.W.getSpriteIndex() + 13));
                    }
                     
                } else {
                    if(y == heightMap.length - 1 - 2) {
                        gameMap.getTile(x, y).setLayer(0, TileOrientation.S.getSpriteIndex() + 13);
                    } else {
                        gameMap.getTile(x, y).setLayer(0, TileOrientation.BASE.getSpriteIndex() + 13);
                    }
                }
                 
            }
        }
        
        return true;
    }
    
}
