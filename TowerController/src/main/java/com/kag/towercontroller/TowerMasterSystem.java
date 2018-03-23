/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.towercontroller;

import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.entities.parts.gui.MenuBackgroundPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.ISystem;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = ISystem.class)
    ,
	@ServiceProvider(service = IComponentLoader.class)
})
public class TowerMasterSystem implements ISystem, IComponentLoader {

    private Entity towerMenuBackground;
    private Entity upgradeMenuBackground;

    @Override
    public void update(float dt, World world, GameData gameData) {
        
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void load(World world) {
        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

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

}
