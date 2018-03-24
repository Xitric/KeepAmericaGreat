package com.kag.core.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.TileMapPart;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.core.graphics.parts.TexturePart;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Kasper
 */
@ServiceProvider(service = IEntitySystem.class)
public class TileMapRenderer implements IEntitySystem {

	private static final Family FAMILY = Family.forAll(TileMapPart.class, TexturePart.class);

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		TexturePart texturePart = entity.getPart(TexturePart.class);
		Texture texture = texturePart.getTexture();
		TileMapPart tileMap = entity.getPart(TileMapPart.class);

		int tileRowLength = texture.getWidth() / tileMap.getTileWidth();

		OrthographicCamera cam = QueuedRenderer.getInstance().getDynamicCamera();
		RenderItem renderItem = new RenderItem(texturePart.getzIndex(), cam, sb -> {
			for (int l = 0; l < 2; l++) {
				for (int y = 0; y < tileMap.getHeight(); y++) {
					for (int x = 0; x < tileMap.getWidth(); x++) {
						int spriteIndex = tileMap.getTile(x, y).getLayer(l);

						if (spriteIndex > -1) {
							sb.draw(texture,
									x * tileMap.getTileWidth(),
									y * tileMap.getTileHeight(),
									tileMap.getTileWidth(),
									tileMap.getTileHeight(),
									spriteIndex % tileRowLength * tileMap.getTileWidth(),
									spriteIndex / tileRowLength * tileMap.getTileHeight(),
									tileMap.getTileWidth(),
									tileMap.getTileHeight(),
									false, true);
						}
					}
				}
			}
		});

		QueuedRenderer.getInstance().enqueue(renderItem);
	}

	@Override
	public Family getFamily() {
		return FAMILY;
	}

	@Override
	public int getPriority() {
		return RENDER_PASS;
	}
}
