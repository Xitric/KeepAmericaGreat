package com.kag.core.graphics;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.kag.common.data.IAsset;
import com.kag.common.spinterfaces.IAssetManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Implementation of the {@link IAssetManager} interface to manage the lifetime
 * of LibGDX textures.
 *
 * @author Kasper
 */
@ServiceProvider(service = IAssetManager.class)
public class AssetManager implements IAssetManager {

	private final Map<IAsset, Texture> assets;

	public AssetManager() {
		assets = new HashMap<>();
	}

	/**
	 * Get the texture referenced by the specified asset. If the asset does not
	 * reference a texture - either because it was disposed or because it was
	 * not created by this AssetManager - this method will return null.
	 *
	 * @param asset an asset referencing a texture
	 * @return the texture referenced by the asset, or null if no texture was
	 *         referenced
	 */
	public Texture getResource(IAsset asset) {
		return assets.get(asset);
	}

	/**
	 * Dispose all textures managed by this AssetManager. This method should be
	 * called before shutting down the application. Calling this method will
	 * result in all assets being invalid, and thus it should never be called in
	 * a running application.
	 */
	public void disposeAll() {
		for (Texture texture : assets.values()) {
			texture.dispose();
		}

		assets.clear();
	}

	@Override
	public IAsset createAsset(InputStream input) {
		Gdx2DPixmap pixmap;

		try {
			pixmap = new Gdx2DPixmap(input, Gdx2DPixmap.GDX2D_FORMAT_RGBA8888);
		} catch (IOException e) {
			Logger.getLogger(AssetManager.class.getName()).log(Level.SEVERE, null, e);
			return null;
		}

		Texture texture = new Texture(new Pixmap(pixmap));
		IAsset asset = new CoreAsset();

		assets.put(asset, texture);

		return asset;
	}

	@Override
	public void disposeAsset(IAsset asset) {
		Texture texture = assets.get(asset);

		if (texture != null) {
			texture.dispose();
			assets.remove(asset);
		}
	}
}
