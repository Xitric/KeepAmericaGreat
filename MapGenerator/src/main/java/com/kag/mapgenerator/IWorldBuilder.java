/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.mapgenerator;

import com.kag.common.data.GameMap;

/**
 *
 * @author andre
 */
public interface IWorldBuilder {
	boolean build(float[][] heightMap, GameMap gameMap);
}
