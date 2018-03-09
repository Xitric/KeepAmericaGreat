/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.data;

/**
 *
 * @author Sofie Jørgensen
 */
public class GameMap {

    private Asset spriteSheet;
    private Tile[][] tiles;
    private Node[][] pathNodes;

    public GameMap(int width, int height) {
	this.tiles = new Tile[height][width];
    }

    public Tile getTile(int x, int y) {
	return tiles[y][x];
    }

    public Node[][] getPathNodes() {
	return pathNodes;
    }

    public void setPathNodes(Node[][] pathNodes) {
	this.pathNodes = pathNodes;
    }

    /**
     * @return the spriteSheet
     */
    public Asset getSpriteSheet() {
	return spriteSheet;
    }

    /**
     * @param spriteSheet the spriteSheet to set
     */
    public void setSpriteSheet(Asset spriteSheet) {
	this.spriteSheet = spriteSheet;
    }

    public void recalculatePathNodeMap() {

    }
}
