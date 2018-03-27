package com.kag.core.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.core.graphics.parts.TexturePart;
import org.openide.util.lookup.ServiceProvider;

import java.util.Collection;

/**
 * @author Kasper
 */
@ServiceProvider(service = IEntitySystem.class)
public class IconRenderer implements IEntitySystem {

	private static final Family FAMILY = Family.forAll(AbsolutePositionPart.class, TexturePart.class);

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		Collection<TexturePart> textureParts = entity.getParts(TexturePart.class);
		AbsolutePositionPart position = entity.getPart(AbsolutePositionPart.class);

		OrthographicCamera cam = QueuedRenderer.getInstance().getStaticCamera();

		for (TexturePart texturePart : textureParts) {
			TextureRegion texture = texturePart.getTexture();

			RenderItem renderItem = new RenderItem(texturePart.getzIndex(), cam, sb -> {
				sb.draw(texture,
						position.getX() + texturePart.getxOffset(),
						position.getY() + texturePart.getyOffset(),
						-texturePart.getxOffset(), -texturePart.getyOffset(),
						texturePart.getWidth(), texturePart.getHeight(),
						1, 1,
						position.getRotation());
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
