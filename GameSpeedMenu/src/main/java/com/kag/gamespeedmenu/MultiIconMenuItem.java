package com.kag.gamespeedmenu;

import com.kag.commonasset.ZIndex;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import org.openide.util.Lookup;

public abstract class MultiIconMenuItem {

	public AssetPart getAssetPart(IAsset asset) {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

		AssetPart iconAsset = assetManager.createTexture(asset, 0, 0, asset.getWidth(), asset.getHeight());
		iconAsset.setzIndex(ZIndex.MENU_ITEM_ICON);

		return iconAsset;
	}
}
