package com.kag.passivetowers;

import com.kag.common.data.IAsset;
import com.kag.common.data.ZIndex;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.tdcommon.spinterfaces.ITower;
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
        return PassiveTowerFactory.getInstance().createPassiveTower(cost, assetPart, iTower);
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
