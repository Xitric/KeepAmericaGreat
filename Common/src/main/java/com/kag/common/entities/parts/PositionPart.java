package com.kag.common.entities.parts;

import com.kag.common.entities.IPart;

public class PositionPart implements IPart {

	private float x;
	private float y;

	/**
	 * The orientation in degrees from 0 to 360. A rotation of 0 indicates that the entity "looks" to the right.
	 */
	private float rotation;

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

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation % 360;
	}
}
