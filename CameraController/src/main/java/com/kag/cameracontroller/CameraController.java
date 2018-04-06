package com.kag.cameracontroller;

import com.kag.common.data.Camera;
import com.kag.common.data.GameData;
import com.kag.common.data.Mouse;
import com.kag.common.data.World;
import com.kag.common.spinterfaces.ISystem;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Kasper
 */
@ServiceProvider(service = ISystem.class)
public class CameraController implements ISystem {

	private static final int activationHeight = 64;

	@Override
	public void update(float dt, World world, GameData gameData) {
		Mouse mouse = gameData.getMouse();
		Camera camera = gameData.getCamera();

		if (mouse.getX() > 768) return;
//
//		if (mouse.getY() < activationHeight) {
//			camera.setY(camera.getY() - 300f * dt);
//		} else if (mouse.getY() > gameData.getHeight() - activationHeight) {
//			camera.setY(camera.getY() + 300f * dt);
//		}
		
		if (mouse.getScrollAmount() != 0) {
			camera.setY(camera.getY() + 2400f * dt * mouse.getScrollAmount());
		}
		
		if (camera.getY() < gameData.getHeight() / 2) camera.setY(gameData.getHeight() / 2);
		if (camera.getY() > world.getGameMap().getHeight() * 64 - gameData.getHeight() / 2) camera.setY(world.getGameMap().getHeight() * 64 - gameData.getHeight() / 2);
	}

	@Override
	public int getPriority() {
		return 0;
	}
}
