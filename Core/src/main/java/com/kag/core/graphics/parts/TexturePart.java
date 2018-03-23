package com.kag.core.graphics.parts;

import com.badlogic.gdx.graphics.Texture;
import com.kag.common.entities.parts.AssetPart;

/**
 * @author Kasper
 */
public class TexturePart extends AssetPart {

	private Texture texture;

	public TexturePart(Texture texture) {
		this.texture = texture;
	}

	public Texture getTexture() {
		return texture;
	}

	@Override
	public void dispose() {
		texture.dispose();
	}
}
