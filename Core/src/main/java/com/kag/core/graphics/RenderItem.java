package com.kag.core.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Representation of a rendering operation with LibGDX which can be queued up. The point of queuing render operations is
 * that different types of assets (which are handled by separate entity systems) might need to be rendered at
 * interleaving layers.
 *
 * @author Kasper
 */
public abstract class RenderItem implements Comparable<RenderItem> {

	private final int zIndex;
	private int insertionIndex;
	private final OrthographicCamera camera;

	public RenderItem(int zIndex, OrthographicCamera camera) {
		this.zIndex = zIndex;
		this.camera = camera;
	}

	public int getzIndex() {
		return zIndex;
	}

	public int getInsertionIndex() {
		return insertionIndex;
	}

	public void setInsertionIndex(int insertionIndex) {
		this.insertionIndex = insertionIndex;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	@Override
	public int compareTo(RenderItem that) {
		int result = Integer.compare(this.zIndex, that.zIndex);

		//If on the same layer, sort by insertion index to prevent flickering
		if (result == 0) {
			result = Integer.compare(this.insertionIndex, that.insertionIndex);
		}

		return result;
	}
}
