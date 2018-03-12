/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.data;

import com.kag.common.entities.Entity;

/**
 *
 * @author Sofie Jørgensen
 */
public class World {

    /**
     * isWalkable considers only Tile.isWalkable()
     *
     * @param x The x-coordinate for the tile to check upon
     * @param y The y-coordinate for the tile to check upon
     * @return true if Tile.isWalkable is true, false otherwise
     */
    public boolean isWalkable(int x, int y) {
	//Find a tile with the given x and y
	//return Tile.isWalkable();
	return false;
    }

    /**
     * isOccupied considers all entities with the BlockingPart and
     * Tile.isWalkable()
     *
     * @param x The x-coordinate for the tile to check upon
     * @param y The y-coordinate for the tile to check upon
     * @return true if Tile.isWalkable is false or if there's an entity on x,y
     * with a BlockingPart
     */
    public boolean isOccupied(int x, int y) {
	//if IsWalkable is false, return true
	//else, loop through entities and check for BlockingPart
	return false;
    }

    public void addEntity(Entity entity) {

    }

    public void removeEntity(Entity entity) {

    }

    public Entity[] getAllEntities() {
	return null;
    }
	
    public Entity getEntityAt(int screenX, int screenY){
	return null;
    }
}
