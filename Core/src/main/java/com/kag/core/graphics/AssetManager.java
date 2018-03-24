package com.kag.core.graphics;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.core.graphics.parts.AnimationPart;
import com.kag.core.graphics.parts.TexturePart;
import org.openide.util.lookup.ServiceProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the {@link IAssetManager} interface to manage the lifetime of LibGDX textures and animations.
 *
 * @author Kasper
 */
@ServiceProvider(service = IAssetManager.class)
public class AssetManager implements IAssetManager {

	@Override
	public AssetPart createTexture(InputStream input) {
		TexturePart texturePart = new TexturePart(loadTexture(input));
		texturePart.setWidth(texturePart.getTexture().getWidth());
		texturePart.setHeight(texturePart.getTexture().getHeight());

		return texturePart;
	}

	@Override
	public AssetPart createAnimation(InputStream input, int frameWidth, int frameHeight, int frameDuration) {
		Texture texture = loadTexture(input);
		if (texture == null) return null;

		TextureRegion[][] tiles2D = TextureRegion.split(texture, frameWidth, frameHeight);
		int tileH = tiles2D.length == 0 ? 0 : tiles2D[0].length;
		int tileV = tiles2D.length;

		TextureRegion[] tiles1D = new TextureRegion[tileH * tileV];
		int insertionIndex = 0;
		for (int y = 0; y < tileV; y++) {
			for (int x = 0; x < tileH; x++) {
				tiles2D[y][x].flip(false, true);
				tiles1D[insertionIndex++] = tiles2D[y][x];
			}
		}

		Animation animation = new Animation(frameDuration / 1000f, tiles1D);
		AnimationPart animationPart = new AnimationPart(texture, animation);
		animationPart.setWidth(frameWidth);
		animationPart.setHeight(frameHeight);

		return animationPart;
	}

	private Texture loadTexture(InputStream input) {
		Gdx2DPixmap pixmap;

		try {
			pixmap = new Gdx2DPixmap(input, Gdx2DPixmap.GDX2D_FORMAT_RGBA8888);
		} catch (IOException e) {
			Logger.getLogger(AssetManager.class.getName()).log(Level.SEVERE, "Error loading texture. Cause:", e);
			return null;
		}

		return new Texture(new Pixmap(pixmap));
	}
}
