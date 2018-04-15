package com.kag.gamespeedmenu;

import com.kag.common.data.IAsset;
import com.kag.common.data.ZIndex;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.spinterfaces.IAssetManager;
import org.openide.util.Lookup;

/**
 * @author Kasper
 */
public abstract class MultiIconMenuItem {

	public AssetPart getAssetPart(IAsset asset) {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

		AssetPart iconAsset = assetManager.createTexture(asset, 0, 0, asset.getWidth(), asset.getHeight());
		iconAsset.setzIndex(ZIndex.MENU_ITEM_ICON);

		return iconAsset;
	}
}
