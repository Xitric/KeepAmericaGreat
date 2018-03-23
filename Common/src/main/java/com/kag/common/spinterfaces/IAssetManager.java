/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.spinterfaces;

import com.kag.common.entities.parts.AssetPart;

import java.io.InputStream;

/**
 * Service used to create graphical elements irrespective of the underlying rendering framework. The main limitation of
 * this architecture is that the rendering framework cannot be changed during runtime, but since the Core module is
 * never changed, this is not an apparent issue.
 *
 * @author Kasper
 */
public interface IAssetManager {

	/**
	 * Constructs a new texture asset specific to the current rendering framework. When the asset is no longer needed it
	 * must be disposed. Assets of the same resource must be disposed individually.
	 * <p>
	 * To use this method, clients can call {@code getClass().getResourceAsStream(String name)} to obtain an input
	 * stream to a resource from the class loader.
	 * <ul>
	 * <li>Pre-conditions: None</li>
	 * <li>Post-conditions: The texture asset is in memory and ready for rendering</li>
	 * </ul>
	 *
	 * @param input a stream of bytes from the resource to load
	 * @return the reference to the new asset or null if the asset could not be
	 * loaded
	 */
	AssetPart createTexture(InputStream input);

	/**
	 * Constructs a new animation specific to the current rendering framework. When the animation is no longer needed it
	 * must be disposed. Animations of the same resource must be disposed individually.
	 * <p>
	 * To use this method, clients can call {@code getClass().getResourceAsStream(String name)} to obtain an input
	 * stream to a resource from the class loader.
	 * <ul>
	 * <li>Pre-conditions: None</li>
	 * <li>Post-conditions: The animation is in memory and ready for rendering</li>
	 * </ul>
	 *
	 * @param input         a stream of bytes from the resource to load
	 * @param frameWidth    the width of each frame of the animation, in pixels
	 * @param frameHeight   the height of each frame of the animation, in pixels
	 * @param frameDuration the duration of each frame of the animation, in milliseconds
	 * @return the reference to the new asset or null if the asset could not be
	 * loaded
	 */
	AssetPart createAnimation(InputStream input, int frameWidth, int frameHeight, int frameDuration);
}
