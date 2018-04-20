package com.kag.cameracontroller;

import com.kag.common.data.Camera;
import com.kag.common.data.GameData;
import com.kag.common.input.Mouse;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.ISystem;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Kasper
 */
@ServiceProvider(service = ISystem.class)
public class CameraController implements ISystem {

	@Override
	public void update(float dt, World world, GameData gameData) {
		Mouse mouse = gameData.getMouse();
		Camera camera = gameData.getCamera();

		if (mouse.getX() > 768) return;
		
		if (mouse.getScrollAmount() != 0) {
			camera.setY(camera.getY() + 40f * mouse.getScrollAmount());
		}
		
		if (camera.getY() < gameData.getHeight() / 2) {
			camera.setY(gameData.getHeight() / 2);
		}

		if (camera.getY() > world.getGameMap().getHeight() * world.getGameMap().getTileHeight() - gameData.getHeight() / 2) {
			camera.setY(world.getGameMap().getHeight() * world.getGameMap().getTileHeight() - gameData.getHeight() / 2);
		}
	}

	@Override
	public int getPriority() {
		return 0;
	}
}
