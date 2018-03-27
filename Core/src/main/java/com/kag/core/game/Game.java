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
import com.kag.common.spinterfaces.*;
import com.kag.core.graphics.AssetManager;
import com.kag.core.graphics.QueuedRenderer;
import com.kag.core.input.GdxInputProcessor;
import com.kag.core.input.GdxKeyboard;
import com.kag.core.input.GdxMouse;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author niels
 */
public class Game implements ApplicationListener {

	private final Lookup lookup;
	private List<IComponentLoader> gameComponents;
	private List<ISystem> systems;
	private List<IEntitySystem> entitySystems;
	private Lookup.Result<IComponentLoader> componentLoaderLookupResult;
	private Lookup.Result<ISystem> systemLookupResult;
	private Lookup.Result<IEntitySystem> entitySystemLookupResult;
	private World world;
	private GameData gameData;
	private GdxKeyboard keyboard;
	private GdxMouse mouse;

	public Game() {
		lookup = Lookup.getDefault();
		gameComponents = new CopyOnWriteArrayList<>();
		systems = new CopyOnWriteArrayList<>();
		entitySystems = new CopyOnWriteArrayList<>();
	}

	@Override
	public void create() {
		Camera camera = new Camera();
		camera.setX(Gdx.graphics.getWidth() / 2);
		camera.setY(Gdx.graphics.getHeight() / 2);

		IMapGenerator mapGenerator = Lookup.getDefault().lookup(IMapGenerator.class);
		world = new World(mapGenerator.generateMap(12,36));

		gameData = new GameData(new Keyboard(), new Mouse(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		keyboard = new GdxKeyboard(gameData.getKeyboard());
		mouse = new GdxMouse(gameData.getMouse());
		Gdx.input.setInputProcessor(new GdxInputProcessor(keyboard, mouse));
		
		componentLoaderLookupResult = lookup.lookupResult(IComponentLoader.class);
		componentLoaderLookupResult.addLookupListener(componentLoaderLookupListener);

		systemLookupResult = lookup.lookupResult(ISystem.class);
		systemLookupResult.addLookupListener(systemLookupListener);

		entitySystemLookupResult = lookup.lookupResult(IEntitySystem.class);
		entitySystemLookupResult.addLookupListener(entitySystemLookupListener);

		for (IComponentLoader componentLoader : lookup.lookupAll(IComponentLoader.class)) {
			componentLoader.load(world);
			gameComponents.add(componentLoader);
		}

		systems.addAll(lookup.lookupAll(ISystem.class));
		systems.sort(systemComparator);
		entitySystems.addAll(lookup.lookupAll(IEntitySystem.class));
		entitySystems.sort(systemComparator);
	}

	@Override
	public void resize(int i, int i1) {

	}

	//Render also acts as our update
	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

		OrthographicCamera camera = QueuedRenderer.getInstance().getDynamicCamera();
		camera.position.x = (int)gameData.getCamera().getX();
		camera.position.y = (int)gameData.getCamera().getY();
		camera.update();
		
		for (ISystem system : systems) {
			system.update(Gdx.graphics.getDeltaTime(), world, gameData);
		}

		for (IEntitySystem entitySystem : entitySystems) {
			BitSet familyBits = entitySystem.getFamily().getBits();

			for (Entity entity : world.getAllEntities()) {

				//Only update entity in the system if the system's family matches the entity
				//We test if the family bits are a subset of the entity's part bits
				BitSet subsetBits = new BitSet();
				subsetBits.or(familyBits);
				subsetBits.and(entity.getBits());

				if (subsetBits.equals(familyBits)) {
					entitySystem.update(Gdx.graphics.getDeltaTime(), entity, world, gameData);
				}
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
	}

	private <T extends IPrioritizable> boolean refreshSystems(Collection<? extends T> actualComponents, Collection<T> localComponents) {
		boolean added = false;

		for (T component : actualComponents) {
			// Newly installed modules
			if (!localComponents.contains(component)) {
				localComponents.add(component);
				added = true;
			}
		}

		for (T component : localComponents) {
			//Removed modules
			if (!actualComponents.contains(component)) {
				localComponents.remove(component);
			}
		}

		return added;
	}

	private final LookupListener componentLoaderLookupListener = new LookupListener() {
		@Override
		public void resultChanged(LookupEvent ev) {
			Collection<? extends IComponentLoader> actualComponents = componentLoaderLookupResult.allInstances();

			for (IComponentLoader component : actualComponents) {
				// Newly installed modules
				if (!gameComponents.contains(component)) {
					component.load(world);
					gameComponents.add(component);
				}
			}
			// Stop and remove module
			for (IComponentLoader component : gameComponents) {
				if (!actualComponents.contains(component)) {
					component.dispose(world);
					gameComponents.remove(component);
				}
			}
		}
	};

	private final LookupListener systemLookupListener = new LookupListener() {
		@Override
		public void resultChanged(LookupEvent ev) {
			if (refreshSystems(systemLookupResult.allInstances(), systems)) {
				systems.sort(systemComparator);
			}
		}
	};

	private final LookupListener entitySystemLookupListener = new LookupListener() {
		@Override
		public void resultChanged(LookupEvent ev) {
			if (refreshSystems(entitySystemLookupResult.allInstances(), entitySystems)) {
				entitySystems.sort(systemComparator);
			}
		}
	};

	private final Comparator<IPrioritizable> systemComparator
			= (IPrioritizable o1, IPrioritizable o2) -> Integer.compare(o1.getPriority(), o2.getPriority());
}
