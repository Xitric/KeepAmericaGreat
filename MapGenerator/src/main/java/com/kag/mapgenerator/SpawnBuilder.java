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
        
        return true;
    }
    
}
