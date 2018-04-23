/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.core.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kag.common.data.*;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.input.Keyboard;
import com.kag.common.input.Mouse;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.*;
import com.kag.core.graphics.AssetManager;
import com.kag.core.graphics.QueuedRenderer;
import com.kag.core.input.GdxInputProcessor;
import com.kag.core.input.GdxKeyboard;
import com.kag.core.input.GdxMouse;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author niels
 */
@ServiceProvider(service = IGame.class)
public class Game implements ApplicationListener, IGame {

	private SystemManager systemManager;
	private World world;
	private GameData gameData;
	private GdxKeyboard keyboard;
	private GdxMouse mouse;

	public Game() {

	}

	@Override
	public void create() {
		Camera camera = new Camera();
		camera.setX(Gdx.graphics.getWidth() / 2);
		camera.setY(Gdx.graphics.getHeight() / 2);

		gameData = new GameData(new Keyboard(), new Mouse(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		keyboard = new GdxKeyboard(gameData.getKeyboard());
		mouse = new GdxMouse(gameData.getMouse());
		Gdx.input.setInputProcessor(new GdxInputProcessor(keyboard, mouse));
		generateNewMap();
		systemManager = new SystemManager();
               
	}

	@Override
	public void resize(int i, int i1) {

	}

	@Override
	public synchronized void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		OrthographicCamera camera = QueuedRenderer.getInstance().getDynamicCamera();
		camera.position.x = (int) gameData.getCamera().getX();
		camera.position.y = (int) gameData.getCamera().getY();
		camera.update();

		systemManager.update(world);

		synchronized (systemManager) {
			systemManager.getActiveSystems().forEach((system) -> {
				system.update(Gdx.graphics.getDeltaTime() * gameData.getSpeedMultiplier(), world, gameData);
			});
		}

		synchronized (systemManager) {
			systemManager.getActiveEntitySystems().forEach((entitySystem) -> {
				Family systemFamily = entitySystem.getFamily();

				world.getEntitiesByFamily(systemFamily).forEach((entity) -> {
					entitySystem.update(Gdx.graphics.getDeltaTime() * gameData.getSpeedMultiplier(), entity, world, gameData);
				});
			});
		}

		//Render the jobs that were enqueued during this iteration
		QueuedRenderer.getInstance().render();

		keyboard.update();
		mouse.update();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		QueuedRenderer.getInstance().dispose();

		AssetManager assetManager = Lookup.getDefault().lookup(AssetManager.class);
		if (assetManager != null) {
			assetManager.disposeAll();
		}

		systemManager.dispose(world);
	}

	private void generateNewMap() {
		IMapGenerator mapGenerator = Lookup.getDefault().lookup(IMapGenerator.class);

		if (world == null) {
			world = new World(mapGenerator.generateMap(12, 36));
		} else {
			world.setMap(mapGenerator.generateMap(12, 36));
		}
	}

	private synchronized void notifyGameListeners() {
		for (IGameStateListener gameStateListener : Lookup.getDefault().lookupAll(IGameStateListener.class)) {
			gameStateListener.newGame(world);
		}
	}

	@Override
	public void startNewGame() {
		generateNewMap();
		notifyGameListeners();
	}
}
