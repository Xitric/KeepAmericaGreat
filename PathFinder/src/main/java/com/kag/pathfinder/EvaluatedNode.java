package com.kag.pathfinder;

import com.kag.common.data.Node;
import com.kag.common.data.Tile;

public class EvaluatedNode extends Node implements Comparable<EvaluatedNode> {


    // The cost of getting here
    private int gValue;

    // The cost of getting here + here to goal. In other words, g + h.
    private int fValue;

    public EvaluatedNode(Tile tile) {
        super(tile, null);
    }

    public void setNext(Node node) {
        next = node;
    }

    public int getgValue() {
        return gValue;
    }

    public void setgValue(int gValue) {
        this.gValue = gValue;
    }

    public int getfValue() {
        return fValue;
    }

    public void setfValue(int fValue) {
        this.fValue = fValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EvaluatedNode that = (EvaluatedNode) o;

        return getTile() == that.getTile();
    }

	@Override
	public int hashCode() {
		return getTile().hashCode();
	}

	@Override
	public int compareTo(EvaluatedNode o) {
    	return Integer.compare(this.fValue, o.getfValue());
	}
}
