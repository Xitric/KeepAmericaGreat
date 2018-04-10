package com.kag.common.entities.parts;

import com.kag.common.data.Color;

/**
 * @author Kasper
 */
public class CirclePart extends AssetPart {

	private float radius;
	private Color color;

	public CirclePart(float radius) {
		this(radius, Color.WHITE);
	}

	public CirclePart(float radius, Color color) {
		this.radius = radius;
		this.color = color;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void dispose() {
		//NO-OP
	}
}
