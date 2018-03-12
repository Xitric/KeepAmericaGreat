package com.kag.common.entities.parts;

import com.kag.common.entities.IPart;

public class BoundingBoxPart implements IPart {

	private int width;
	private int height;

	public BoundingBoxPart(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
