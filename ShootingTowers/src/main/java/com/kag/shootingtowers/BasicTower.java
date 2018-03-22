package com.kag.shootingtowers;

import com.kag.common.data.IAsset;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.entities.parts.gui.IconPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.interfaces.ITower;
import org.openide.util.Lookup;

public class BasicTower implements ITower {


    @Override
    public IAsset getAsset() {
        return null;
    }

    @Override
    public Entity create() {
        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

        Entity newTower = ShootingTowerFactory.getInstance().createTower("Basic Tower",2,96,1000,0.03f,10,(float)(Math.PI/4),assetManager.createAsset(getClass().getResourceAsStream("/trumpTower.png")));

        return newTower;
    }
}
