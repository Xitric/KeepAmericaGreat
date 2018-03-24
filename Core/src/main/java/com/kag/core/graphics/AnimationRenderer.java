package com.kag.core.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.core.graphics.parts.AnimationPart;
import org.openide.util.lookup.ServiceProvider;

import java.util.Collection;

/**
 * @author Kasper
 */
@ServiceProvider(service = IEntitySystem.class)
public class AnimationRenderer implements IEntitySystem {

	private static final Family FAMILY = Family.forAll(PositionPart.class, AnimationPart.class);

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		Collection<AnimationPart> animationParts = entity.getParts(AnimationPart.class);
		PositionPart position = entity.getPart(PositionPart.class);

		OrthographicCamera cam = QueuedRenderer.getInstance().getDynamicCamera();

		for (AnimationPart animationPart : animationParts) {
			animationPart.addStateTime(delta);
			Animation animation = animationPart.getAnimation();
			TextureRegion stateTexture = animation.getKeyFrame(animationPart.getStateTime(), true);

			RenderItem renderItem = new RenderItem(animationPart.getzIndex(), cam, sb -> {
				sb.draw(stateTexture,
						position.getX() + animationPart.getxOffset(),
						position.getY() + animationPart.getyOffset(),
						-animationPart.getxOffset(), -animationPart.getyOffset(),
						stateTexture.getRegionWidth(), stateTexture.getRegionHeight(),
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
