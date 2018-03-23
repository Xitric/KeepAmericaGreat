package com.kag.shootingtowers;

import com.kag.common.data.IAsset;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.gui.IconPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.interfaces.ITower;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProvider(service = ITower.class)
public class BasicTower implements ITower {

    private IconPart iconPart;

    @Override
    public IAsset getAsset() {
        if (iconPart != null) {
            return iconPart.getAsset();
        } else {
            IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

            iconPart = new IconPart(assetManager.createAsset(getClass().getResourceAsStream("/trumpTower.png")));
            return iconPart.getAsset();
        }
    }

    @Override
    public Entity create() {
        Entity newTower = ShootingTowerFactory.getInstance().createTower("Basic Tower", 2, 96, 1000, 0.03f, 10, (float) (Math.PI / 4), iconPart.getAsset());

        return newTower;
    }
}
