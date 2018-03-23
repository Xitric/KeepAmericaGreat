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
import com.kag.common.spinterfaces.ISystem;
import com.kag.interfaces.ITower;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

import java.util.Collection;

@ServiceProviders(value = {
    @ServiceProvider(service = ISystem.class),
    @ServiceProvider(service = IComponentLoader.class)
})
public class TowerMasterSystem implements ISystem, IComponentLoader {

    private Entity towerMenuBackground;
    private Entity upgradeMenuBackground;
    private Entity buymenu;

    @Override
    public void update(float dt, World world) {
        Collection<? extends ITower> listOfPowers = Lookup.getDefault().lookupAll(ITower.class);
        if (!listOfPowers.isEmpty()) {
            if(listOfPowers.iterator().hasNext()){
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

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void load(World world) {
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

}
