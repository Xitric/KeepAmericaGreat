package com.kag.core.graphics;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.core.graphics.parts.AnimationPart;
import com.kag.core.graphics.parts.TexturePart;
import org.openide.util.lookup.ServiceProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the {@link IAssetManager} interface to manage the lifetime of LibGDX textures and animations.
 */
@ServiceProvider(service = IAssetManager.class)
public class AssetManager implements IAssetManager {

	private final Map<IAsset, Texture> assets;

	public AssetManager() {
		assets = new HashMap<>();
	}

	@Override
	public IAsset loadAsset(InputStream input) {
		Gdx2DPixmap pixmap;

		try {
			pixmap = new Gdx2DPixmap(input, Gdx2DPixmap.GDX2D_FORMAT_RGBA8888);
		} catch (IOException e) {
			Logger.getLogger(AssetManager.class.getName()).log(Level.SEVERE, "Error loading texture. Cause:", e);
			return null;
		}

		Texture texture = new Texture(new Pixmap(pixmap));
		texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		IAsset asset = new CoreAsset(this, texture.getWidth(), texture.getHeight());
		assets.put(asset, texture);

		return asset;
	}

	@Override
	public AssetPart createTexture(InputStream input) {
		IAsset asset = loadAsset(input);
		if (asset == null) return null;

		return createTexture(asset, 0, 0, asset.getWidth(), asset.getHeight());
	}

	@Override
	public AssetPart createTexture(IAsset asset, int x, int y, int width, int height) {
		Texture texture = assets.get(asset);
		if (texture == null) return null;

		TextureRegion region = new TextureRegion(texture, x, y, width, height);
		region.flip(false, true);

		TexturePart texturePart = new TexturePart(asset, region);
		texturePart.setWidth(width);
		texturePart.setHeight(height);

		return texturePart;
	}

	@Override
	public AssetPart createAnimation(InputStream input, int frameWidth, int frameHeight, int frameDuration) {
		IAsset asset = loadAsset(input);
		if (asset == null) return null;

		return createAnimation(asset, 0, 0, asset.getWidth(), asset.getHeight(), frameWidth, frameHeight, frameDuration);
	}

	@Override
	public AssetPart createAnimation(IAsset asset, int x, int y, int width, int height, int frameWidth, int frameHeight, int frameDuration) {
		Texture texture = assets.get(asset);
		if (texture == null) return null;

		TextureRegion[][] tiles2D = TextureRegion.split(texture, frameWidth, frameHeight);
		int tileH = tiles2D.length == 0 ? 0 : tiles2D[0].length;
		int tileV = tiles2D.length;

		TextureRegion[] tiles1D = new TextureRegion[tileH * tileV];
		int insertionIndex = 0;
		for (TextureRegion[] aTiles2D : tiles2D) {
			for (int col = 0; col < tileH; col++) {
				aTiles2D[col].flip(false, true);
				tiles1D[insertionIndex++] = aTiles2D[col];
			}
		}

		Animation animation = new Animation(frameDuration / 1000f, tiles1D);
		AnimationPart animationPart = new AnimationPart(asset, animation);
		animationPart.setWidth(frameWidth);
		animationPart.setHeight(frameHeight);

		return animationPart;
	}

	/**
	 * Release the specified asset from memory. If the asset is already released, this method has no effect.
	 * <ul>
	 * <li>Pre-conditions: None</li>
	 * <li>Post-conditions: The resources of the asset are no longer in memory</li>
	 * </ul>
	 *
	 * @param asset the asset to dispose
	 */
	public void disposeAsset(IAsset asset) {
		Texture texture = assets.get(asset);

		if (texture != null) {
			texture.dispose();
			assets.remove(asset);
		}
	}

	/**
	 * Dispose all graphical resources managed by this AssetManager. This method should be called before shutting down
	 * the application. Calling this method will result in all assets being invalid, and thus it should never be called
	 * in a running application.
	 */
	public void disposeAll() {
		for (Texture texture : assets.values()) {
			texture.dispose();
		}

		assets.clear();
	}
}
