/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.spinterfaces;

import com.kag.common.data.IAsset;
import java.io.InputStream;

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
	 * <p>
	 * To use this method, clients can call
	 * {@code getClass().getResourceAsStream(String name)} to obtain an input
	 * stream to a resource from the class loader.
	 * <ul>
	 * <li>Pre-conditions: None</li>
	 * <li>Post-conditions: The asset is in memory and ready for rendering</li>
	 * </ul>
	 *
	 * @param input a stream of bytes from the resource to load
	 * @return the reference to the new asset or null if the asset could not be
	 *         loaded
	 * @see IAssetManager#disposeAsset(IAsset)
	 */
	IAsset createAsset(InputStream input);

	/**
	 * Release the specified asset from memory. If the asset is already
	 * released, this method has no effect. This method should always be called
	 * for an asset before dropping the reference, however all assets are
	 * automatically disposed when the game quits.
	 * <ul>
	 * <li>Pre-conditions: None</li>
	 * <li>Post-conditions: The asset is no longer in memory</li>
	 * </ul>
	 *
	 * @param asset the asset to dispose
	 */
	void disposeAsset(IAsset asset);
}
