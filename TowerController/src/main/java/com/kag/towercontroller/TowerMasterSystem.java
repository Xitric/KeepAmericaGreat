/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.towercontroller;

import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.entities.parts.gui.IconPart;
import com.kag.common.entities.parts.gui.MenuBackgroundPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.ISystem;
import com.kag.interfaces.ITower;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

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
    private List<Entity> towersToBeDrawn;

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

        towersToBeDrawn = new ArrayList<>();

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

    
    private Entity addNewTowerToMenu(ITower tower){
        //Create Entity from tower and return
        Entity towerEntity = new Entity();
        towersToBeDrawn.add(towerEntity);

        for (Entity entity : towersToBeDrawn) {
            if (entity == towerEntity) {
                int index = towersToBeDrawn.indexOf(entity);

            }
        }

        IconPart iconPart = new IconPart(tower.getAsset());
        PositionPart positionPart = new PositionPart(0, 0);

        towerEntity.addPart(iconPart);
        towerEntity.addPart(positionPart);


        return towerEntity;
    }
    
    private final LookupListener iTowerLookupListener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent ev) {
            Collection<? extends ITower> actualTowers = towerImpleLookupResult.allInstances();
            Consumer<World> worldConsumer = (world) -> {

            };

            for (ITower tower : actualTowers) {
                // Newly installed modules
                if (!towerImple.contains(tower)) {
                    towerImple.add(tower);
                    addNewTowerToMenu(tower);
                }
            }
            // Stop and remove module
            for (ITower tower : towerImple) {
                if (!actualTowers.contains(tower)) {
                    towerImple.remove(tower);
                    addNewTowerToMenu(tower);
                }
            }
        }

    };

}
