package com.kag.core.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
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
		TextureRegion texture = texturePart.getTexture();
		TileMapPart tileMap = entity.getPart(TileMapPart.class);

		OrthographicCamera cam = QueuedRenderer.getInstance().getDynamicCamera();
		SpriteRenderItem renderItem = new SpriteRenderItem(texturePart.getzIndex(), cam, sb -> {
			sb.setTransformMatrix(new Matrix4().idt());
			renderTileMap(sb, texture, cam, tileMap);
		});

		QueuedRenderer.getInstance().enqueue(renderItem);
	}

	private void renderTileMap(SpriteBatch sb, TextureRegion textureRegion, OrthographicCamera cam, TileMapPart tileMap) {
		int tileRowLength = textureRegion.getRegionWidth() / tileMap.getTileWidth();
		int startX = (int) (cam.position.x - cam.viewportWidth / 2) / tileMap.getTileWidth();
		int endX = (int) (cam.position.x + cam.viewportWidth / 2) / tileMap.getTileWidth() + 1;
		int startY = (int) (cam.position.y - cam.viewportHeight / 2) / tileMap.getTileHeight();
		int endY = (int) (cam.position.y + cam.viewportHeight / 2) / tileMap.getTileHeight() + 1;

		if (startX < 0) startX = 0;
		if (startY < 0) startY = 0;
		if (endX > tileMap.getWidth()) endX = tileMap.getWidth();
		if (endY > tileMap.getHeight()) endY = tileMap.getHeight();

		Texture texture = textureRegion.getTexture();
		int offsetX = textureRegion.getRegionX();
		int offsetY = texture.getHeight() - textureRegion.getRegionY();

		for (int l = 0; l < 2; l++) {
			for (int y = startY; y < endY; y++) {
				for (int x = startX; x < endX; x++) {
					int spriteIndex = tileMap.getTile(x, y).getLayer(l);

					if (spriteIndex > -1) {
						sb.draw(texture,
								x * tileMap.getTileWidth(),
								y * tileMap.getTileHeight(),
								tileMap.getTileWidth(),
								tileMap.getTileHeight(),
								spriteIndex % tileRowLength * tileMap.getTileWidth() + offsetX,
								spriteIndex / tileRowLength * tileMap.getTileHeight() + offsetY,
								tileMap.getTileWidth(),
								tileMap.getTileHeight(),
								false, true);
					}
				}
			}
		}
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
