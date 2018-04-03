package com.kag.common.data;

import java.io.InputStream;

/**
 * Representation of a graphical element that can be rendered by the Core module. Instances of this interface should be
 * created by calling {@link com.kag.common.spinterfaces.IAssetManager#loadAsset(InputStream)}.
 *
 * @author Sofie Jørgensen
 * @author Kasper
 */
public interface IAsset {

	/**
	 * Release this asset from memory. If the asset is already released, this method has no effect. This method should
	 * always be called for an asset before dropping the reference, however all assets are automatically disposed when
	 * the game quits.
	 */
	void dispose();
}
