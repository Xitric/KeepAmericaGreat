package com.kag.mapmenu;

import com.kag.common.data.GameData;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.input.Mouse;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.common.spinterfaces.IGame;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IEntitySystem.class)
public class MenuController implements IEntitySystem {

	private static final Family FAMILY = Family.forAll(MapMenuPart.class, AbsolutePositionPart.class);

	private Entity buttonEntity;

	public MenuController() {
		buttonEntity = new Entity();
		buttonEntity.addPart(new AbsolutePositionPart(384, 458));
		buttonEntity.addPart(new BoundingBoxPart(178, 91));
	}

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		if (gameData.getMouse().isButtonDown(Mouse.BUTTON_LEFT) && world.isEntityAt(buttonEntity, gameData.getMouse().getX(), gameData.getMouse().getY())) {
			IGame game = Lookup.getDefault().lookup(IGame.class);
			if (game != null) {
				game.startNewGame();
			}

			world.removeEntity(entity);
		}
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
