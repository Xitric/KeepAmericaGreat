package com.kag.passivetowers;

import com.kag.common.entities.Entity;
import com.kag.commonasset.ZIndex;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commontower.spinterfaces;

import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ITower.class)
public class BasicCrateTower implements ITower {

    private IAsset crateAsset;
    private int cost = 5;

    @Override
    public IAsset getAsset() {

        if (crateAsset == null) {
            IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
            crateAsset = assetManager.loadAsset(getClass().getResourceAsStream("/Crate.png"));
        }
        return crateAsset;
    }

    @Override
    public Entity create() {

        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
        AssetPart assetPart = assetManager.createTexture(getAsset(), 0, 0, 58, 58);
        assetPart.setzIndex(ZIndex.TOWER_BASE);
        return Lookup.getDefault().lookup(PassiveTowerFactory.class).createPassiveTower(cost, assetPart, this);
    }

    @Override
    public IAsset getProjectileAsset() {
        return null;
    }

    @Override
    public int getCost() {
        return cost;
    }
}
