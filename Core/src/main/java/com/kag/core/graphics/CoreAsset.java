package com.kag.core.graphics;

import com.kag.common.data.IAsset;

import java.util.UUID;

/**
 * Implementation of the {@link IAsset} interface for use in the core module. This implementation provides some
 * augmented information to assist the {@link AssetManager}.
 *
 * @author Kasper
 */
public class CoreAsset implements IAsset {

	/**
	 * Used to determine the unique identity of an asset, irrespective of the resource it represents.
	 */
	private final UUID id;

	private final AssetManager assetManager;
	private final int width, height;

	/**
	 * Constructs a new, uniquely identifiable asset.
	 *
	 * @param assetManager the manager responsible for controlling the life time of this asset
	 */
	public CoreAsset(AssetManager assetManager, int width, int height) {
		id = UUID.randomUUID();
		this.assetManager = assetManager;
		this.width = width;
		this.height = height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		CoreAsset asset = (CoreAsset) o;

		return id.equals(asset.id);
	}

	@Override
	public void dispose() {
		assetManager.disposeAsset(this);
	}
}
