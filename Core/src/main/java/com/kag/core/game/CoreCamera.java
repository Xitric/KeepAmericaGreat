package com.kag.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * @author Kasper
 */
public class CoreCamera {

	private static OrthographicCamera camera;

	public static OrthographicCamera getCamera() {
		if (camera == null) {
			camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			camera.setToOrtho(true);
			camera.update();
		}

		return camera;
	}
}
