package com.kag.passivetowers;

import com.kag.common.data.IAsset;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.interfaces.ITower;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ITower.class)
public class BasicCrateTower implements ITower {

    @Override
    public IAsset getAsset() {
        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
        return assetManager.loadAsset(getClass().getResourceAsStream("/Crate.png"));
    }

    @Override
    public Entity create() {
        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
        AssetPart assetPart = assetManager.createTexture(getClass().getResourceAsStream("/Crate.png"));
        assetPart.setzIndex(30);
        return PassiveTowerFactory.getInstance().createPassiveTower(5, assetPart);
    }
}
