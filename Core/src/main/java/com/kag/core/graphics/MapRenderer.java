package com.kag.core.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kag.common.data.GameMap;
import org.openide.util.Lookup;

public class MapRenderer {

	private SpriteBatch spriteBatch;
	private AssetManager assetManager;

	public MapRenderer() {
		spriteBatch = new SpriteBatch();
		assetManager = Lookup.getDefault().lookup(AssetManager.class);
	}

	public void render(GameMap gameMap) {
		spriteBatch.begin();
		Texture texture = assetManager.getResource(gameMap.getSpriteSheet());

		for (int y = 0; y < gameMap.getHeight(); y++) {
			for (int x = 0; x < gameMap.getWidth(); x++) {
				int spriteIndex = gameMap.getTile(x, y).getLayer(0);
				spriteBatch.draw(texture, x * 64, y * 64, 64, 64, spriteIndex % 13 * 64, spriteIndex / 13 * 64, 64, 64, false, false);
			}
		}
		spriteBatch.end();
	}
}
