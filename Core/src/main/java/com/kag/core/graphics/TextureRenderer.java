package com.kag.core.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.core.graphics.parts.TexturePart;
import org.openide.util.lookup.ServiceProvider;

import java.util.Collection;

/**
 * @author Kasper
 */
@ServiceProvider(service = IEntitySystem.class)
public class TextureRenderer implements IEntitySystem {

	private static final Family FAMILY = Family.forAll(PositionPart.class, TexturePart.class);

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		Collection<TexturePart> textureParts = entity.getParts(TexturePart.class);
		PositionPart position = entity.getPart(PositionPart.class);

		OrthographicCamera cam = QueuedRenderer.getInstance().getDynamicCamera();

		for (TexturePart texturePart : textureParts) {
			Texture texture = texturePart.getTexture();

			RenderItem renderItem = new RenderItem(texturePart.getzIndex(), cam, sb -> {
				sb.draw(texture,
						position.getX() + texturePart.getxOffset(),
						position.getY() + texturePart.getyOffset(),
						-texturePart.getxOffset(), -texturePart.getyOffset(),
						texturePart.getWidth(), texturePart.getHeight(),
						1, 1,
						position.getRotation(),
						0, 0,
						texture.getWidth(), texture.getHeight(),
						false, true);
			});

			QueuedRenderer.getInstance().enqueue(renderItem);
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
