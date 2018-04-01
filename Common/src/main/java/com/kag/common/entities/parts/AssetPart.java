package com.kag.common.entities.parts;

import com.kag.common.entities.IPart;

/**
 * An entity part that represents a graphical element such as a texture, or an animation, which can be rendered by the
 * core module. Instances of this interface should be created using the
 * {@link com.kag.common.spinterfaces.IAssetManager}. Assets should always be disposed again when they are no longer
 * needed, to prevent memory leaks.
 *
 * @author Kasper
 */
public abstract class AssetPart implements IPart {

	private int zIndex;
	private int xOffset, yOffset;
	private int width;
	private int height;

	public int getzIndex() {
		return zIndex;
	}

	public void setzIndex(int zIndex) {
		this.zIndex = zIndex;
	}

	public int getxOffset() {
		return xOffset;
	}

	public void setxOffset(int xOffset) {
		this.xOffset = xOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public void setyOffset(int yOffset) {
		this.yOffset = yOffset;
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

	/**
	 * Release this asset from memory. If the asset is already released, this method has no effect. This method should
	 * always be called on an asset before dropping the reference.
	 * <ul>
	 * <li>Pre-conditions: None</li>
	 * <li>Post-conditions: The asset is no longer in memory, and this part is not useful for rendering</li>
	 * </ul>
	 */
	public abstract void dispose();
}