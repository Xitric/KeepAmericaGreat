/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.moduleupdatemanager;

import org.netbeans.api.autoupdate.InstallSupport;
import org.netbeans.api.autoupdate.OperationContainer;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;

public class Installer extends ModuleInstall {

    private ModuleUpdateManager moduleUpdateManager;

    @Override
    public void restored() {
        moduleUpdateManager = new ModuleUpdateManager();
        moduleUpdateManager.searchNewAndUpdateModules();
        try {
            OperationContainer<InstallSupport> installContainer
                    = moduleUpdateManager.addToContainer(OperationContainer.createForInstall(), moduleUpdateManager.getInstall());

            OperationContainer<InstallSupport> updateContainer
                    = moduleUpdateManager.addToContainer(OperationContainer.createForUpdate(), moduleUpdateManager.getUpdate());

            moduleUpdateManager.installModules(installContainer);
            moduleUpdateManager.installModules(updateContainer);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

}
