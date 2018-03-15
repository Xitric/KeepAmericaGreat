/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.moduleupdatemanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.autoupdate.InstallSupport;
import org.netbeans.api.autoupdate.InstallSupport.Installer;
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
import org.openide.util.Exceptions;

public class ModuleUpdateManager {

    private List<UpdateElement> install = new ArrayList<>();
    private List<UpdateElement> update = new ArrayList<>();
    private List<UpdateElement> localInstalled = new ArrayList<>();
    private List<UpdateElement> uninstalls = new ArrayList<>();

    public void findNewAndUpdateModules() {
	localInstalled = getLocalInstalled();

	for (UpdateUnitProvider provider
		: UpdateUnitProviderFactory.getDefault()
			.getUpdateUnitProviders(false)) {
	    try {
		provider.refresh(null, true);
	    } catch (IOException ex) {
		ex.printStackTrace();
	    }
	}

	for (UpdateUnit unit : UpdateManager.getDefault().getUpdateUnits()) {
	    if (!unit.getAvailableUpdates().isEmpty()) {
		if (unit.getInstalled() == null) {
		    System.out.println("Found a new module " + unit.getCodeName());
		    install.add(unit.getAvailableUpdates().get(0));
		} else {
		    System.out.println("Found an update " + unit.getCodeName());
		    update.add(unit.getAvailableUpdates().get(0));

		}
	    }
	}
    }

    private List<UpdateElement> getLocalInstalled() {
	List<UpdateElement> locals = new ArrayList<>();
	for (UpdateUnit unit : UpdateManager.getDefault().getUpdateUnits()) {
	    if (unit.getInstalled() != null) {
		locals.add(unit.getInstalled());
	    }
	}
	return locals;
    }

    public OperationContainer<InstallSupport> addToContainer(
	    OperationContainer<InstallSupport> container,
	    List<UpdateElement> modules) throws Exception {
	for (UpdateElement e : modules) {
	    if (container.canBeAdded(e.getUpdateUnit(), e)) {
		OperationInfo<InstallSupport> operationInfo = container.add(e);
		if (operationInfo != null) {
		    container.add(operationInfo.getRequiredElements());
		    if (!operationInfo.getBrokenDependencies().isEmpty()) {
			throw new Exception("YOU FUCKED UP!");
		    }
		}
	    }
	}
	return container;
    }

    public void installModules(OperationContainer<InstallSupport> container) {
	try {
	    InstallSupport support = container.getSupport();
	    if (support != null) {
		Validator vali = support.doDownload(null, true, true);
		Installer inst = support.doValidate(vali, null);
		Restarter restarter = support.doInstall(inst, null);
		if (restarter != null) {
		    support.doRestartLater(restarter);
		}
	    }
	} catch (OperationException ex) {
	    ex.printStackTrace();
	}
    }

    public List<UpdateElement> getInstall() {
	return install;
    }

    public List<UpdateElement> getUpdate() {
	return update;
    }

    public void findModulesToUninstall() {
	//Get installed modules from UC, and local installed
	List<UpdateElement> modulesInSystemAndUC = getLocalInstalled();

	//Get modules that is already installed 
	List<UpdateElement> installedModules = new ArrayList<>(localInstalled);

	//Remove all modules, that is local, but not in updatecenter.
	installedModules.removeAll(modulesInSystemAndUC);
	uninstalls = installedModules;
    }

    public OperationContainer<OperationSupport> uninstallModules() {
	OperationContainer<OperationSupport> container = OperationContainer.createForDirectUninstall();

	for (UpdateElement module : uninstalls) {
	    if (container.canBeAdded(module.getUpdateUnit(), module)) {
		OperationInfo<OperationSupport> info = container.add(module);
		if (info == null) {
		    continue;
		}
		container.add(info.getRequiredElements());
	    }
	}

	if (!container.listAll().isEmpty()) {
	    try {
		//Uninstalls the module
		Restarter rs = container.getSupport().doOperation(null);
		if (rs != null) {
		    //Restart module if necessary
		    container.getSupport().doRestart(rs, null);
		}
	    } catch (OperationException ex) {
		Exceptions.printStackTrace(ex);
	    }
	}

	return container;
    }

}
