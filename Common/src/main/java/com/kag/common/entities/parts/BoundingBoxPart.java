package com.kag.common.entities.parts;

import com.kag.common.entities.IPart;

/**
 * A part describing the area covered by an entity. An entity can be positioned with a {@link PositionPart}, but when it
 * is necessary to describe the position of an entity as more than just a single point, the position can be accompanied
 * by this part.
 * <p>
 * Systems are free to determine how to interpret the bounding box of an entity, but generally a bounding box should be
 * centered on an entity. An entity at position (x,y) with a bounding box of width w and height h thus covers the points
 * between x - w/2 and x + w/2 on the horizontal axis and between y - h/2 and y + h/2 on the vertical axis.
 */
public class BoundingBoxPart implements IPart {

	private int width;
	private int height;

	/**
	 * Constructs a new bounding box with the specified width and height.
	 *
	 * @param width  the width of the bounding box, in pixels
	 * @param height the height of the bounding box, in pixels
	 */
	public BoundingBoxPart(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Get the width of this bounding box, in pixels.
	 *
	 * @return the width of this bounding box, in pixels
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Set the width of this bounding box, in pixels.
	 *
	 * @param width the width of this bounding box, in pixels.
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Get the height of this bounding box, in pixels.
	 *
	 * @return the height of this bounding box, in pixels
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Set the height of this bounding box, in pixels.
	 *
	 * @param height the height of this bounding box, in pixels
	 */
	public void setHeight(int height) {
		this.height = height;
	}
}
