package com.kag.core.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.CirclePart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.spinterfaces.IEntitySystem;
import org.openide.util.lookup.ServiceProvider;

import java.util.Collection;

/**
 * System for rendering solid circles. This system must be placed in the core module, since this is the only module that
 * is able to use LibGDX. This system only processes entities with the following parts:
 * <ul>
 * <li>{@link CirclePart}</li>
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
public class CircleRenderer extends AbstractRenderer implements IEntitySystem {

	private static final Family FAMILY = Family.forAll(CirclePart.class)
			.includingAny(PositionPart.class, AbsolutePositionPart.class);

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		Collection<CirclePart> circleParts = entity.getParts(CirclePart.class);
		PositionPart position = getPosition(entity);
		OrthographicCamera cam = getCamera(entity);

		for (CirclePart circlePart : circleParts) {
			//Apply entity position and rotation
			Matrix4 transform = new Matrix4();
			transform.idt()
					.translate(position.getX(), position.getY(), 0)
					.rotate(Vector3.Z, position.getRotation())
					.translate(circlePart.getxOffset(), circlePart.getyOffset(), 0);

			ShapeRenderItem renderItem = new ShapeRenderItem(circlePart.getzIndex(), cam, ShapeRenderer.ShapeType.Filled, sr -> {
				//Enable blending if the circle is translucent
				Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
				Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

				sr.setTransformMatrix(transform);
				sr.setColor(circlePart.getColor().getRed() / 255.0f,
						circlePart.getColor().getGreen() / 255.0f,
						circlePart.getColor().getBlue() / 255.0f,
						circlePart.getColor().getAlpha() / 255.0f);

				sr.circle(0, 0, circlePart.getRadius());
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
