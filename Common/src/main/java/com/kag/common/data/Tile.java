package com.kag.common.data;

public class Tile {

    private int x, y;
    private int[] layers;
    private boolean walkable;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.layers = new int[2];
        walkable = true;
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
}
