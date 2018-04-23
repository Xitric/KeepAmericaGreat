package com.kag.commonasset.entities.parts;

import com.kag.common.entities.IPart;
import com.kag.commonasset.ZIndex;

/**
 * An entity part that represents a graphical element such as a texture, or an animation, which can be rendered by the
 * core module. Instances of this interface should be created using the
 * {@link com.kag.commonasset.spinterfaces.IAssetManager}. Assets should always be disposed again when they are no
 * longer needed, to prevent memory leaks. This class is an abstract supertype, and thus all actual asset objects will
 * be more specific classes, some of which are concealed in the core module.
 */
public abstract class AssetPart implements IPart {

	private int zIndex;
	private int xOffset, yOffset;
	private int width;
	private int height;
	private float rotation;
	private int originX, originY;

	public int getzIndex() {
		return zIndex;
	}

	public void setzIndex(ZIndex zIndex) {
		this.zIndex = zIndex.value;
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

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public int getOriginX() {
		return originX;
	}

	public void setOriginX(int originX) {
		this.originX = originX;
	}

	public int getOriginY() {
		return originY;
	}

	public void setOriginY(int originY) {
		this.originY = originY;
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
