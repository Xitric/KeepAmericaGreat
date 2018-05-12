package com.kag.commonasset.spinterfaces;

import java.io.InputStream;

/**
 * Representation of a graphical element that can be rendered by the Core module. Instances of this interface should be
 * created by calling {@link IAssetManager#loadAsset(InputStream)}.
 */
public interface IAsset {

	/**
	 * Get the width of the asset represented by this object.
	 *
	 * @return the width
	 */
	int getWidth();

	/**
	 * Get the height of the asset represented by this object.
	 *
	 * @return the height
	 */
	int getHeight();

	/**
	 * Release this asset from memory. If the asset is already released, this method has no effect. This method should
	 * always be called for an asset before dropping the reference, however all assets are automatically disposed when
	 * the game quits.
	 */
	void dispose();
}
