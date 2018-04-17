/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.spinterfaces;

import com.kag.common.data.IAsset;
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
	 * Loads a graphical resource into memory in a format specific to the current rendering framework. Use this method
	 * when the same graphical element must be shared among multiple entities that cannot share the same
	 * {@link AssetPart}.
	 * <p>
	 * When the asset is no longer needed it must be disposed. When an asset is disposed, all asset parts constructed
	 * from it must be released, as they will contain dangling references.
	 * <ul>
	 * <li>Pre-conditions: The graphics environment must be initialized</li>
	 * <li>Post-conditions: The graphical resource is in memory and ready for rendering</li>
	 * </ul>
	 *
	 * @param input a stream of bytes from the resource to load
	 * @return the reference to the new asset or null if the asset could not be loaded
	 */
	IAsset loadAsset(InputStream input);

	/**
	 * Creates a new {@link AssetPart} wrapping a texture from a region of the specified asset. This method can be used
	 * to create multiple asset parts that share the same graphical resource, thus lowering memory consumption. Note,
	 * however, that when this asset part is disposed, the {@link IAsset} is disposed for all other asset parts that
	 * were made from it.
	 * <ul>
	 * <li>Pre-conditions: The graphics environment must be initialized</li>
	 * <li>Post-conditions: None</li>
	 * </ul>
	 *
	 * @param asset  the graphical resource to extract the texture from
	 * @param x      the x-coordinate of the upper left corner of the texture
	 * @param y      the y-coordinate of the upper left corner of the texture
	 * @param width  the width of the texture to extract
	 * @param height the height of the texture to extract
	 * @return the new asset part or null if the asset was invalid
	 */
	AssetPart createTexture(IAsset asset, int x, int y, int width, int height);

	/**
	 * Loads an image into memory in a format specific to the current rendering framework, and wraps it in an
	 * {@link AssetPart}. Use this method only when the image is either only used by one entity, or the same asset part
	 * can be shared among multiple entities. When the asset part is no longer needed it must be disposed.
	 * <ul>
	 * <li>Pre-conditions: The graphics environment must be initialized</li>
	 * <li>Post-conditions: The texture asset is in memory and ready for rendering</li>
	 * </ul>
	 *
	 * @param input a stream of bytes from the resource to load
	 * @return the new asset part or null if the resource could not be loaded
	 */
	AssetPart createTexture(InputStream input);

	/**
	 * Loads an image into memory and converts it to an animation in a format specific to the current rendering
	 * framework. Use this method only when the animation is either only used by one entity, or the same asset part
	 * can be shared among multiple entities. When the asset part is no longer needed it must be disposed.
	 * <ul>
	 * <li>Pre-conditions: The graphics environment must be initialized</li>
	 * <li>Post-conditions: The animation is in memory and ready for rendering</li>
	 * </ul>
	 *
	 * @param input         a stream of bytes from the resource to load
	 * @param frameWidth    the width of each frame of the animation, in pixels
	 * @param frameHeight   the height of each frame of the animation, in pixels
	 * @param frameDuration the duration of each frame of the animation, in milliseconds
	 * @return the new asset part or null if the resource could not be loaded
	 */
	AssetPart createAnimation(InputStream input, int frameWidth, int frameHeight, int frameDuration);

	/**
	 * Creates a new animation from a region of an existing asset. This method can be used to create multiple animations
	 * that share the same graphical resource, thus lowering memory consumption. Note,
	 * however, that when this animation is disposed, the {@link IAsset} is disposed for all other asset parts that
	 * were made from it.
	 * <ul>
	 * <li>Pre-conditions: The graphics environment must be initialized</li>
	 * <li>Post-conditions: None</li>
	 * </ul>
	 *
	 * @param asset         the graphical resource to extract the animation from
	 * @param x             the x-coordinate of the upper left corner of the texture
	 * @param y             the y-coordinate of the upper left corner of the texture
	 * @param width         the width of the texture to extract
	 * @param height        the height of the texture to extract
	 * @param frameWidth    the width of each frame of the animation, in pixels
	 * @param frameHeight   the height of each frame of the animation, in pixels
	 * @param frameDuration the duration of each frame of the animation, in milliseconds
	 * @return the new asset part or null if the resource could not be loaded
	 */
	AssetPart createAnimation(IAsset asset, int x, int y, int width, int height, int frameWidth, int frameHeight, int frameDuration);
}
