package com.kag.common.data;

public class Tile {

    private int x, y;
    private int[] layers;
    private boolean walkable;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.layers = new int[2];
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }
}
