package com.kag.mapmenu;

import com.kag.common.data.GameData;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IEntitySystem;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Kasper
 */
@ServiceProvider(service = IEntitySystem.class)
public class MenuController implements IEntitySystem {

	private static final Family FAMILY = Family.forAll(MapMenuPart.class, AbsolutePositionPart.class);

	private static final int scrollPosition = 476;
	private static final int scrollSpeed = 2000;

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		MapMenuPart mapMenuPart = entity.getPart(MapMenuPart.class);
		AbsolutePositionPart positionPart = entity.getPart(AbsolutePositionPart.class);

		//Transition map menu if it is not at the desired position yet
		if ((mapMenuPart.isVisible() && positionPart.getY() > gameData.getHeight() - scrollPosition) ||
				(!mapMenuPart.isVisible() && positionPart.getY() < gameData.getHeight())) {

			float vPos;
			if (mapMenuPart.isVisible()) {
				vPos = gameData.getHeight() - positionPart.getY();
			} else {
				vPos = scrollPosition - (gameData.getHeight() - positionPart.getY());
			}

			float speed = getSignSpeedMultiplier(vPos);
			int direction = mapMenuPart.isVisible() ? -1 : 1;

			positionPart.setPos(positionPart.getX(), positionPart.getY() + delta / gameData.getSpeedMultiplier() * direction * speed * scrollSpeed);

			//Stop if we reached the desired position
			if (mapMenuPart.isVisible() && positionPart.getY() < gameData.getHeight() - scrollPosition) {
				positionPart.setPos(positionPart.getX(), gameData.getHeight() - scrollPosition);
			} else if (!mapMenuPart.isVisible() && positionPart.getY() > gameData.getHeight()) {
				positionPart.setPos(positionPart.getX(), gameData.getHeight());
				world.removeEntity(entity);
			}
		}

		//If the menu is at the desired position, check for input
		if (mapMenuPart.isVisible() && positionPart.getY() == gameData.getHeight() - scrollPosition) {
			//TODO
		}
	}

	private float getSignSpeedMultiplier(float signPosition) {
		return (float) Math.cos(signPosition * signPosition / ((scrollPosition * scrollPosition * 2) / Math.PI));
	}

	@Override
	public Family getFamily() {
		return FAMILY;
	}

	@Override
	public int getPriority() {
		return UPDATE_PASS_1;
	}
}
