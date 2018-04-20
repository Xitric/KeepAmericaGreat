package com.kag.core.graphics.parts;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.spinterfaces.IAsset;

/**
 * @author Kasper
 */
public class TexturePart extends AssetPart {

	private final IAsset asset;
	private final TextureRegion texture;

	public TexturePart(IAsset asset, TextureRegion texture) {
		this.asset = asset;
		this.texture = texture;
	}

	public TextureRegion getTexture() {
		return texture;
	}

	@Override
	public void dispose() {
		asset.dispose();
	}
}
