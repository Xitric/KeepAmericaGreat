package com.kag.core.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kag.common.data.GameMap;
import org.openide.util.Lookup;

public class MapRenderer {

	private SpriteBatch spriteBatch;
	private AssetManager assetManager;
	private OrthographicCamera camera;

	public MapRenderer(OrthographicCamera camera) {
		spriteBatch = new SpriteBatch();
		assetManager = Lookup.getDefault().lookup(AssetManager.class);
		this.camera = camera;
	}

	public void render(GameMap gameMap) {
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		Texture texture = assetManager.getResource(gameMap.getSpriteSheet());

		for (int y = 0; y < gameMap.getHeight(); y++) {
			for (int x = 0; x < gameMap.getWidth(); x++) {
				int spriteIndex = gameMap.getTile(x, y).getLayer(0);
				spriteBatch.draw(texture, x * 64, y * 64, 64, 64, spriteIndex % 13 * 64, spriteIndex / 13 * 64, 64, 64, false, true);
				spriteIndex = gameMap.getTile(x, y).getLayer(1);
				if (spriteIndex > 0) {
					spriteBatch.draw(texture, x * 64, y * 64, 64, 64, spriteIndex % 13 * 64, spriteIndex / 13 * 64, 64, 64, false, true);
				}
			}
		}
		spriteBatch.end();
	}
}
