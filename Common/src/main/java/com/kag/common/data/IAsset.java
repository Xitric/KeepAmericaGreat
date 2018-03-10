package com.kag.common.data;

/**
 * Representation of a graphical element that can be rendered by the Core
 * module. Instances of this interface should be created by calling
 * {@link com.kag.common.spinterfaces.IAssetManager#createAsset(String)}.
 *
 * @author Sofie Jørgensen
 * @author Kasper
 */
public interface IAsset {

	/**
	 * Get the platform specific source of this IAsset. This string can be
	 * thought of as the identifier of a resource, allowing IAssets representing
	 * the same graphical resource on the disk to be identified.
	 *
	 * @return the platform specific idenfitier
	 */
	String getSource();
}
