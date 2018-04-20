package com.kag.core.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.kag.common.data.GameData;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.core.graphics.parts.TexturePart;
import org.openide.util.lookup.ServiceProvider;

import java.util.Collection;

/**
 * System for rendering 2d textures. This system must be placed in the core module, since this is the only module that
 * is able to use LibGDX. This system only processes entities with the following
 * parts:
 * <ul>
 * <li>{@link TexturePart}</li>
 * </ul>
 * And at least one of these parts:
 * <ul>
 * <li>{@link PositionPart}</li>
 * <li>{@link AbsolutePositionPart}</li>
 * </ul>
 *
 * @author Kasper
 */
@ServiceProvider(service = IEntitySystem.class)
public class TextureRenderer extends AbstractRenderer implements IEntitySystem {

	private static final Family FAMILY = Family.forAll(TexturePart.class)
			.includingAny(PositionPart.class, AbsolutePositionPart.class);

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		Collection<TexturePart> textureParts = entity.getParts(TexturePart.class);
		PositionPart position = getPosition(entity);
		OrthographicCamera cam = getCamera(entity);

		for (TexturePart texturePart : textureParts) {
			TextureRegion texture = texturePart.getTexture();

			//Apply entity position and rotation
			Matrix4 transform = new Matrix4();
			transform.idt()
					.translate(position.getX(), position.getY(), 0)
					.rotate(Vector3.Z, position.getRotation())
					.translate(texturePart.getxOffset(), texturePart.getyOffset(), 0);

			//Render and apply texture rotation
			SpriteRenderItem renderItem = new SpriteRenderItem(texturePart.getzIndex(), cam, sb -> {
				sb.setTransformMatrix(transform);
				sb.draw(texture,
						0, 0,
						texturePart.getOriginX(), texturePart.getOriginY(),
						texturePart.getWidth(), texturePart.getHeight(),
						1, 1,
						texturePart.getRotation());
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
