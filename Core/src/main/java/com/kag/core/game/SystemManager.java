package com.kag.core.game;

import com.kag.common.data.ServiceManager;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.common.spinterfaces.IPrioritizable;
import com.kag.common.spinterfaces.ISystem;

import java.util.*;

public class SystemManager {

	private final List<ISystem> systems;
	private final Collection<ISystem> pendingSystems;

	private final List<IEntitySystem> entitySystems;
	private final Collection<IEntitySystem> pendingEntitySystems;

	private final Collection<IComponentLoader> componentLoaders;
	private final Collection<IComponentLoader> toLoad;
	private final Collection<IComponentLoader> toDispose;

	private final ServiceManager<IComponentLoader> componentManager;
	private final ServiceManager<ISystem> systemManager;
	private final ServiceManager<IEntitySystem> entitySystemManager;

	public SystemManager() {
		systems = new ArrayList<>();
		pendingSystems = new ArrayList<>();
		entitySystems = new ArrayList<>();
		pendingEntitySystems = new ArrayList<>();
		componentLoaders = new ArrayList<>();
		toLoad = new ArrayList<>();
		toDispose = new ArrayList<>();

		componentManager = new ServiceManager<>(IComponentLoader.class, this::addComponent, this::removeComponent);
		systemManager = new ServiceManager<>(ISystem.class, this::addSystem, this::removeSystem);
		entitySystemManager = new ServiceManager<>(IEntitySystem.class, this::addEntitySystem, this::removeEntitySystem);
	}

	private synchronized void addComponent(IComponentLoader component) {
		toLoad.add(component);
	}

	private synchronized void removeComponent(IComponentLoader component) {
		toDispose.add(component);
	}

	private synchronized void addSystem(ISystem system) {
		pendingSystems.add(system);
	}

	private synchronized void removeSystem(ISystem system) {
		systems.remove(system);
		pendingSystems.remove(system);
	}

	private synchronized void addEntitySystem(IEntitySystem system) {
		pendingEntitySystems.add(system);
	}

	private synchronized void removeEntitySystem(IEntitySystem system) {
		entitySystems.remove(system);
		pendingEntitySystems.remove(system);
	}

	//TODO: Very important to synchronize on the system manager, to ensure system does not get unloaded during iteration
	public synchronized Collection<ISystem> getActiveSystems() {
		return systems;
	}

	//TODO: Very important to synchronize on the system manager, to ensure system does not get unloaded during iteration
	public synchronized Collection<IEntitySystem> getActiveEntitySystems() {
		return entitySystems;
	}

	public synchronized void dispose(World world) {
		for (IComponentLoader componentLoader : componentLoaders) {
			componentLoader.dispose(world);
		}

		componentLoaders.clear();
	}

	public synchronized void update(World world) {
		if (!toLoad.isEmpty()) {
			loadComponents(world);
		}

		if (!toDispose.isEmpty()) {
			disposeComponents(world);
		}

		if (!pendingSystems.isEmpty()) {
			considerPendingSystems();
		}

		if (!pendingEntitySystems.isEmpty()) {
			considerPendingEntitySystems();
		}
	}

	private void loadComponents(World world) {
		for (IComponentLoader componentLoader : toLoad) {
			componentLoader.load(world);
			componentLoaders.add(componentLoader);
		}

		toLoad.clear();
	}

	private void disposeComponents(World world) {
		for (Iterator<IComponentLoader> iter = toDispose.iterator(); iter.hasNext(); ) {
			IComponentLoader componentLoader = iter.next();

			if (componentLoader instanceof ISystem) {
				if (systems.contains(componentLoader)) {
					//Cannot dispose yet if the system is still active
					continue;
				} else if (pendingSystems.contains(componentLoader)) {
					//If the system is pending when disposed, just discard it
					pendingSystems.remove(componentLoader);
				}
			}

			if (componentLoader instanceof IEntitySystem) {
				if (entitySystems.contains(componentLoader)) {
					//Cannot dispose yet if the entity system is still active
					continue;
				} else if (pendingEntitySystems.contains(componentLoader)) {
					//If the entity system is pending when disposed, just discard it
					pendingEntitySystems.remove(componentLoader);
				}
			}

			//If we got this far, it is safe to dispose
			componentLoader.dispose(world);
			iter.remove();
			componentLoaders.remove(componentLoader);
		}
	}

	private void considerPendingSystems() {
		boolean changed = false;

		for (Iterator<ISystem> iter = pendingSystems.iterator(); iter.hasNext(); ) {
			ISystem pendingSystem = iter.next();

			if (pendingSystem instanceof IComponentLoader) {
				if (!componentLoaders.contains(pendingSystem)) {
					//The system is still pending if it has not been loaded yet
					continue;
				}
			}

			//If we got this far, we upgrade the system to an active state
			systems.add(pendingSystem);
			iter.remove();
			changed = true;
		}

		if (changed) {
			systems.sort(Comparator.comparingInt(IPrioritizable::getPriority));
		}
	}

	private void considerPendingEntitySystems() {
		boolean changed = false;

		for (Iterator<IEntitySystem> iter = pendingEntitySystems.iterator(); iter.hasNext(); ) {
			IEntitySystem pendingEntitySystem = iter.next();

			if (pendingEntitySystem instanceof IComponentLoader) {
				if (toLoad.contains(pendingEntitySystem)) {
					//The entity system is still pending if it awaits loading
					continue;
				}
			}

			//If we got this far, we upgrade the entity system to an active state
			entitySystems.add(pendingEntitySystem);
			iter.remove();
			changed = true;
		}

		if (changed) {
			entitySystems.sort(Comparator.comparingInt(IPrioritizable::getPriority));
		}
	}
}
