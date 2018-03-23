/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.towercontroller;

import com.kag.common.data.IAsset;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.entities.parts.gui.IconPart;
import com.kag.common.entities.parts.gui.MenuBackgroundPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.common.spinterfaces.IPrioritizable;
import com.kag.common.spinterfaces.ISystem;
import com.kag.interfaces.ITower;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

@ServiceProviders(value = {
    @ServiceProvider(service = ISystem.class),
    @ServiceProvider(service = IComponentLoader.class)
})
public class TowerMasterSystem implements ISystem, IComponentLoader {

    private Lookup lookup;
    private Entity towerMenuBackground;
    private Entity upgradeMenuBackground;
    private Entity buymenu;
    private List<ITower> towerImple;
    private Lookup.Result<ITower> towerImpleLookupResult;

    @Override
    public void update(float dt, World world) {
        

    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void load(World world) {
        lookup = Lookup.getDefault();
        
        towerImple = new CopyOnWriteArrayList<>();
        towerImpleLookupResult = lookup.lookupResult(ITower.class);
	towerImpleLookupResult.addLookupListener(iTowerLookupListener);
        
        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

        System.out.println("TOWER SYSTEM IS CALLED!!!");
        towerMenuBackground = new Entity();
        towerMenuBackground.addPart(new MenuBackgroundPart(assetManager.createAsset(getClass().getResourceAsStream("/todo.png"))));
        towerMenuBackground.addPart(new PositionPart(768, 260));

        upgradeMenuBackground = new Entity();
        upgradeMenuBackground.addPart(new MenuBackgroundPart(assetManager.createAsset(getClass().getResourceAsStream("/todo2.png"))));
        upgradeMenuBackground.addPart(new PositionPart(768, 0));

        world.addEntity(towerMenuBackground);
        world.addEntity(upgradeMenuBackground);
       
    }

    @Override
    public void dispose(World world) {
        world.removeEntity(towerMenuBackground);
        world.removeEntity(upgradeMenuBackground);
    }
    
    private void drawNewTowerMenu(){
        Collection<? extends ITower> listOfPowers = Lookup.getDefault().lookupAll(ITower.class);
        if (!listOfPowers.isEmpty()) {
            if (listOfPowers.iterator().hasNext()) {

                IAsset iasset = listOfPowers.iterator().next().getAsset();
                buymenu = new Entity();
                buymenu.addPart(new PositionPart(100, 100));
//            System.out.println(entity.getPart(IconPart.class).getAsset());
                buymenu.addPart(new IconPart(iasset));
                world.addEntity(buymenu);
            }
        } else {
            System.out.println("List of towers is empty!");
        }
    }
    
    private final LookupListener iTowerLookupListener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent ev
        ) {
            Collection<? extends ITower> actualComponents = towerImpleLookupResult.allInstances();

            for (ITower component : actualComponents) {
                // Newly installed modules
                if (!towerImple.contains(component)) {
                    towerImple.add(component);
                    drawNewTowerMenu();
                }
            }
            // Stop and remove module
            for (ITower component : towerImple) {
                if (!actualComponents.contains(component)) {
                    towerImple.remove(component);
                    drawNewTowerMenu();
                }
            }
        }

    };

}
