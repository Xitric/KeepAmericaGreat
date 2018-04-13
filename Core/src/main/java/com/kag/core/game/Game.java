/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.core.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kag.common.data.*;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.spinterfaces.*;
import com.kag.core.graphics.AssetManager;
import com.kag.core.graphics.QueuedRenderer;
import com.kag.core.input.GdxInputProcessor;
import com.kag.core.input.GdxKeyboard;
import com.kag.core.input.GdxMouse;
import org.openide.util.Lookup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author niels
 */
public class Game implements ApplicationListener {

	private final List<ISystem> systems;
	private final List<IEntitySystem> entitySystems;
	private ServiceManager<IComponentLoader> componentManager;
	private ServiceManager<ISystem> systemManager;
	private ServiceManager<IEntitySystem> entitySystemManager;
	private Collection<Runnable> scheduledJobs;
	private World world;
	private GameData gameData;
	private GdxKeyboard keyboard;
	private GdxMouse mouse;

	public Game() {
		systems = new CopyOnWriteArrayList<>();
		entitySystems = new CopyOnWriteArrayList<>();
		scheduledJobs = new ArrayList<>();
	}

	@Override
	public void create() {
		Camera camera = new Camera();
		camera.setX(Gdx.graphics.getWidth() / 2);
		camera.setY(Gdx.graphics.getHeight() / 2);

		IMapGenerator mapGenerator = Lookup.getDefault().lookup(IMapGenerator.class);
		world = new World(mapGenerator.generateMap(12, 36));

		gameData = new GameData(new Keyboard(), new Mouse(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		keyboard = new GdxKeyboard(gameData.getKeyboard());
		mouse = new GdxMouse(gameData.getMouse());
		Gdx.input.setInputProcessor(new GdxInputProcessor(keyboard, mouse));

		componentManager = new ServiceManager<>(IComponentLoader.class, this::addComponent, this::removeComponent);
		systemManager = new ServiceManager<>(ISystem.class, this::addSystem, this::removeSystem);
		entitySystemManager = new ServiceManager<>(IEntitySystem.class, this::addEntitySystem, this::removeEntitySystem);
	}

	@Override
	public void resize(int i, int i1) {

	}

	@Override
	public synchronized void render() {
		//Run jobs that were scheduled for the OpenGL thread
		if (!scheduledJobs.isEmpty()) {
			for (Runnable job : scheduledJobs) {
				job.run();
			}
			scheduledJobs.clear();
		}

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

		OrthographicCamera camera = QueuedRenderer.getInstance().getDynamicCamera();
		camera.position.x = (int) gameData.getCamera().getX();
		camera.position.y = (int) gameData.getCamera().getY();
		camera.update();

		for (ISystem system : systems) {
			system.update(Gdx.graphics.getDeltaTime(), world, gameData);
		}

		for (IEntitySystem entitySystem : entitySystems) {
			Family systemFamily = entitySystem.getFamily();

			for (Entity entity : world.getEntitiesByFamily(systemFamily)) {
				entitySystem.update(Gdx.graphics.getDeltaTime(), entity, world, gameData);
			}
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

		//Dispose plugins
		componentManager.getServiceProviders().forEach(c -> c.dispose(world));
	}

	private synchronized void addComponent(IComponentLoader component) {
		scheduledJobs.add(() -> component.load(world));
	}

	private synchronized void removeComponent(IComponentLoader component) {
		scheduledJobs.add(() -> component.dispose(world));
	}

	private synchronized void addSystem(ISystem system) {
		systems.add(system);
		systems.sort(Comparator.comparingInt(IPrioritizable::getPriority));
	}

	private synchronized void removeSystem(ISystem system) {
		systems.remove(system);
	}

	private synchronized void addEntitySystem(IEntitySystem system) {
		entitySystems.add(system);
		entitySystems.sort(Comparator.comparingInt(IPrioritizable::getPriority));
	}

	private synchronized void removeEntitySystem(IEntitySystem system) {
		entitySystems.remove(system);
	}
}
