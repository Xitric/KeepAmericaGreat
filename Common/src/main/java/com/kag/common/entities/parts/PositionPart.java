package com.kag.common.entities.parts;

import com.kag.common.entities.IPart;

public class PositionPart implements IPart {

	private float x;
	private float y;
	public int rotation; //0-360 degrees. Ex. 0 can indicate a downwards direction from the top.

	public PositionPart(float x, float y) {
		setPos(x, y);
		this.rotation = 0;
	}

	public PositionPart(float x, float y, int rotation) {
		setPos(x, y);
		this.rotation = rotation % 360;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public final void setPos(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation % 360;
	}
}
