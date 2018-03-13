/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.moduleupdatemanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.autoupdate.UpdateElement;
import org.netbeans.api.autoupdate.UpdateManager;
import org.netbeans.api.autoupdate.UpdateUnit;
import org.netbeans.api.autoupdate.UpdateUnitProvider;
import org.netbeans.api.autoupdate.UpdateUnitProviderFactory;

public class ModuleUpdateManager {

    private List<UpdateElement> install = new ArrayList<>();
    private List<UpdateElement> update = new ArrayList<>();
    
    public void searchNewAndUpdateModules(){
        for(UpdateUnitProvider provider : UpdateUnitProviderFactory.getDefault().getUpdateUnitProviders(true)){
            try{
                provider.refresh(null, true);
            } catch(IOException ex){
                ex.printStackTrace();
            }
        }
        
        for(UpdateUnit unit : UpdateManager.getDefault().getUpdateUnits()){
            if(!unit.getAvailableUpdates().isEmpty()){
                if(unit.getInstalled() == null){
                    install.add(unit.getAvailableUpdates().get(0));
                } else {
                    update.add(unit.getAvailableUpdates().get(0));
                }
            }
        }
    }
    
    
}
