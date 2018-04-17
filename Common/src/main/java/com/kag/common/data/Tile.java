package com.kag.common.data;

public class Tile {
    private final int x;
	private final int y;
    private final int[] layers;
    private boolean walkable;
    private boolean occupied;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.layers = new int[] {-1, -1};
        walkable = true;
        occupied = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLayer(int layer) {
        if (layer < 0 || layer >= layers.length) {
            throw new IndexOutOfBoundsException("The given layer was out of bounds. Layer: " + layer + ", and length was: " + layers.length);
        }

        return layers[layer];
    }

    public void setLayer(int layer, int value) {
        if (layer < 0 || layer >= layers.length) {
            throw new IndexOutOfBoundsException("The given layer was out of bounds. Layer: " + layer + ", and length was: " + layers.length);
        }

        layers[layer] = value;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public boolean isOccupied() { return occupied; }

    public void setOccupied(boolean occupied) { this.occupied = occupied; }
}
