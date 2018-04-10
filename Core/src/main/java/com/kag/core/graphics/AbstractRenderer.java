package com.kag.core.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.PositionPart;

/**
 * Representation of a type that can render graphics using both a static and dynamic camera. This class combines logic
 * shared by many rendering systems.
 *
 * @author Kasper
 */
public abstract class AbstractRenderer {

	/**
	 * Get a camera for rendering the specified type of entity. If the entity has a regular {@link PositionPart}, this
	 * method will return a dynamic camera. If, instead, the entity has an
	 * {@link com.kag.common.entities.parts.AbsolutePositionPart} or no position at all, this method will return a
	 * static camera.
	 *
	 * @param entity the entity to get a camera for
	 * @return a dynamic or static camera depending on the entity's position information
	 */
	protected OrthographicCamera getCamera(Entity entity) {
		if (entity.hasPart(PositionPart.class)) {
			//Render in world coordinates
			return QueuedRenderer.getInstance().getDynamicCamera();
		} else {
			//Render in screen coordinates
			return QueuedRenderer.getInstance().getStaticCamera();
		}
	}

	/**
	 * Get the positioning information from the specified entity. If the entity has a regular {@link PositionPart}, it
	 * will be returned. Otherwise this method will try to extract an
	 * {@link com.kag.common.entities.parts.AbsolutePositionPart}. If this fails, the method will return null,
	 * indicating that the entity has no position.
	 *
	 * @param entity the entity to extract a position from
	 * @return the positioning information of the entity
	 */
	protected PositionPart getPosition(Entity entity) {
		if (entity.hasPart(PositionPart.class)) {
			return entity.getPart(PositionPart.class);
		} else {
			//Render in screen coordinates
			return entity.getPart(AbsolutePositionPart.class);
		}
	}
}
