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
import org.netbeans.api.autoupdate.OperationSupport.Restarter;
import org.netbeans.api.autoupdate.UpdateElement;
import org.netbeans.api.autoupdate.UpdateManager;
import org.netbeans.api.autoupdate.UpdateUnit;
import org.netbeans.api.autoupdate.UpdateUnitProvider;
import org.netbeans.api.autoupdate.UpdateUnitProviderFactory;
import org.openide.awt.NotificationDisplayer;
import org.openide.util.ImageUtilities;

public class ModuleUpdateManager {

    private List<UpdateElement> install = new ArrayList<>();
    private List<UpdateElement> update = new ArrayList<>();

    public void searchNewAndUpdateModules() {
        for (UpdateUnitProvider provider : UpdateUnitProviderFactory.getDefault().getUpdateUnitProviders(true)) {
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

    public OperationContainer<InstallSupport> addToContainer(
            OperationContainer<InstallSupport> container,
            List<UpdateElement> modules) throws Exception {
        for (UpdateElement e : modules) {
            if (container.canBeAdded(e.getUpdateUnit(), e)) {
                OperationInfo<InstallSupport> operationInfo = container.add(e);
                if (operationInfo != null) {
                    container.add(operationInfo.getRequiredElements());
                    if(!operationInfo.getBrokenDependencies().isEmpty()){
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
    
    

}
