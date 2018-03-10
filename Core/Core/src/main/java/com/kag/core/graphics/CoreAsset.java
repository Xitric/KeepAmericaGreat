package com.kag.core.graphics;

import com.kag.common.data.IAsset;
import java.util.UUID;

/**
 * Implementation of the {@link IAsset} interface for use in the core module.
 * This implementation provides some augmented information to assist the
 * {@link AssetManager}.
 *
 * @author Kasper
 */
public class CoreAsset implements IAsset {

	/**
	 * Used to determine the unique identity of an asset, irrespective of the
	 * resource it represents.
	 */
	private final UUID id;

	/**
	 * Constructs a new, uniquely identifiable asset.
	 */
	public CoreAsset() {
		id = UUID.randomUUID();
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
}
