package com.kag.moduleupdatemanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.autoupdate.InstallSupport;
import org.netbeans.api.autoupdate.InstallSupport.Validator;
import org.netbeans.api.autoupdate.OperationContainer;
import org.netbeans.api.autoupdate.OperationContainer.OperationInfo;
import org.netbeans.api.autoupdate.OperationException;
import org.netbeans.api.autoupdate.OperationSupport;
import org.netbeans.api.autoupdate.OperationSupport.Restarter;
import org.netbeans.api.autoupdate.UpdateElement;
import org.netbeans.api.autoupdate.UpdateManager;
import org.netbeans.api.autoupdate.UpdateUnit;
import org.netbeans.api.autoupdate.UpdateUnitProvider;
import org.netbeans.api.autoupdate.UpdateUnitProviderFactory;

public class ModuleUpdateManager {

	private static final Logger LOGGER = Logger.getLogger(ModuleUpdateManager.class.getName());
	private static final String KAG_UC = "com_kag_moduleupdatemanager_update_center";

	private List<UpdateElement> locallyInstalled = new ArrayList<>();

	/**
	 * Query update centers for new modules, updates, and uninstalled modules,
	 * and apply the changes.
	 */
	public void syncWithUpdateCenter() {
		locallyInstalled = getLocallyInstalled();

		try {
			getUpdateProvider(KAG_UC).refresh(null, true);
		} catch (IOException ex) {
			LOGGER.log(Level.WARNING, ex.getLocalizedMessage(), ex.getCause());
			return;
		}

		List<UpdateElement> install = getModulesToInstall();
		List<UpdateElement> update = getModulesToUpdate();
		List<UpdateElement> uninstall = getModulesToUninstall();

		installModules(install, false);
		installModules(update, true);
		uninstallModules(uninstall);
	}

	/**
	 * Finds those modules from the KAG update center that are installed
	 * locally. This means that if a module is installed locally but it is not
	 * available in the update center, it will not be found by this method.
	 */
	private List<UpdateElement> getLocallyInstalled() {
		List<UpdateElement> locals = new ArrayList<>();

		for (UpdateUnit unit : getUpdateProvider(KAG_UC).getUpdateUnits()) {
			if (unit.getInstalled() != null) {
				locals.add(unit.getInstalled());
			}
		}

		return locals;
	}

	/**
	 * Find those modules from all available update centers that are not
	 * installed locally.
	 */
	private List<UpdateElement> getModulesToInstall() {
		List<UpdateElement> install = new ArrayList<>();

		for (UpdateUnit unit : UpdateManager.getDefault().getUpdateUnits()) {
			if (!unit.getAvailableUpdates().isEmpty()) {
				if (unit.getInstalled() == null) {
					LOGGER.log(Level.INFO, "Found new module: {0}", unit.getCodeName());
					install.add(unit.getAvailableUpdates().get(0));
				}
			}
		}

		return install;
	}

	/**
	 * Find those modules from the KAG update center that are installed locally,
	 * but for which a newer version is avialable.
	 */
	private List<UpdateElement> getModulesToUpdate() {
		List<UpdateElement> update = new ArrayList<>();

		for (UpdateUnit unit : getUpdateProvider(KAG_UC).getUpdateUnits()) {
			if (!unit.getAvailableUpdates().isEmpty()) {
				if (unit.getInstalled() != null) {
					LOGGER.log(Level.INFO, "Found new version of module: {0} v{1}", new Object[]{unit.getCodeName(), unit.getAvailableUpdates().get(0).getSpecificationVersion()});
					update.add(unit.getAvailableUpdates().get(0));
				}
			}
		}

		return update;
	}

	/**
	 * Find those locally installed modules from the KAG update center that have
	 * since been removed from the KAG update center.
	 */
	private List<UpdateElement> getModulesToUninstall() {
		//Get modules installed locally and in the UC
		List<UpdateElement> modulesInSystemAndUC = getLocallyInstalled();

		//Get modules that were previously installed from the UC
		List<UpdateElement> installedModules = new ArrayList<>(locallyInstalled);

		//Remove all modules that are in the UC, leaving those modules that are
		//only local
		installedModules.removeAll(modulesInSystemAndUC);

		for (UpdateElement element : installedModules) {
			LOGGER.log(Level.INFO, "Module removed: {0}", element.getCodeName());
		}

		return installedModules;
	}

	private void installModules(List<UpdateElement> install, boolean update) {
		OperationContainer<InstallSupport> container;

		if (update) {
			container = OperationContainer.createForUpdate();
		} else {
			container = OperationContainer.createForInstall();
		}

		//Add elements to install to the container for later processing
		for (UpdateElement element : install) {
			if (container.canBeAdded(element.getUpdateUnit(), element)) {
				OperationInfo<InstallSupport> info = container.add(element);
				if (info == null) {
					continue;
				}
				container.add(info.getRequiredElements());

				if (!info.getBrokenDependencies().isEmpty()) {
					LOGGER.log(Level.WARNING, "Install failed. Broken dependencies when installing modules: {0}", info.getBrokenDependencies());
					return;
				}
			}
		}

		//Install the elements in the container
		if (!container.listAll().isEmpty()) {
			try {
				InstallSupport support = container.getSupport();
				Validator vali = support.doDownload(null, true, true);
				InstallSupport.Installer inst = support.doValidate(vali, null);
				Restarter restarter = support.doInstall(inst, null);
				if (restarter != null) {
					support.doRestartLater(restarter);
				}
			} catch (OperationException ex) {
				LOGGER.log(Level.WARNING, ex.getLocalizedMessage(), ex.getCause());
			}
		}
	}

	private void uninstallModules(List<UpdateElement> uninstall) {
		OperationContainer<OperationSupport> container = OperationContainer.createForDirectUninstall();

		//Add elements to uninstall to the container for later processing
		for (UpdateElement element : uninstall) {
			if (container.canBeAdded(element.getUpdateUnit(), element)) {
				OperationInfo<OperationSupport> info = container.add(element);
				if (info == null) {
					continue;
				}
				container.add(info.getRequiredElements());

				if (!info.getBrokenDependencies().isEmpty()) {
					LOGGER.log(Level.WARNING, "Uninstall failed. Broken dependencies when uninstalling modules: {0}", info.getBrokenDependencies());
					return;
				}
			}
		}

		//Uninstall the elements in the container
		if (!container.listAll().isEmpty()) {
			try {
				Restarter restarter = container.getSupport().doOperation(null);
				if (restarter != null) {
					//Restart if necessary
					container.getSupport().doRestart(restarter, null);
				}
			} catch (OperationException ex) {
				LOGGER.log(Level.WARNING, ex.getLocalizedMessage(), ex.getCause());
			}
		}
	}

	private UpdateUnitProvider getUpdateProvider(String name) {
		for (UpdateUnitProvider provider : UpdateUnitProviderFactory.getDefault().getUpdateUnitProviders(true)) {
			if (provider.getName().equals(name)) {
				return provider;
			}
		}

		LOGGER.log(Level.WARNING, "No available update center with name: {0}", name);
		return null;
	}
}
