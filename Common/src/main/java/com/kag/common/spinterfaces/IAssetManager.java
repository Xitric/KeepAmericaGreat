/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.spinterfaces;

import com.kag.common.data.IAsset;

/**
 * Service used to create graphical elements irrespective of the underlying
 * rendering framework. The main limitation of this architecture is that the
 * rendering framework cannot be changed during runtime, but since the Core
 * module is never changed, this is not an apparent issue.
 *
 * @author Kasper
 */
public interface IAssetManager {

	/**
	 * Constructs a new asset specific to the current rendering framework. When
	 * the asset is no longer needed it must be disposed. Assets to the same
	 * resource must be disposed individually, however all assets are
	 * automatically disposed when the game quits.
	 * <ul>
	 * <li>Pre-conditions: None</li>
	 * <li>Post-conditions: The asset at the specified location is in memory and
	 * ready for rendering</li>
	 * </ul>
	 *
	 * @param source the location where the graphical element is stored in the
	 *               file system
	 * @return the reference to the new asset
	 * @see IAssetManager#disposeAsset(IAsset)
	 */
	IAsset createAsset(String source);

	/**
	 * Release the specified asset. The implementation is free to determine the
	 * best time to remove the asset from memory. If the asset is already
	 * released, this method has no effect.
	 * <ul>
	 * <li>Pre-conditions: None</li>
	 * <li>Post-conditions: The asset is no longer bound in memory</li>
	 * </ul>
	 *
	 * @param asset the asset to dispose
	 */
	void disposeAsset(IAsset asset);
}
