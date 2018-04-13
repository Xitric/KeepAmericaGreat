package com.kag.shootingtowers;

import com.kag.common.data.IAsset;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.interfaces.ITower;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ITower.class)
public class GreenTower implements ITower {
    @Override
    public IAsset getAsset() {
        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
        return assetManager.loadAsset(getClass().getResourceAsStream("/GreenTower.png"));
    }

    @Override
    public Entity create() {

        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
        AssetPart basePart = assetManager.createTexture(getClass().getResourceAsStream("/GenericBaseB.png"));
        AssetPart turretPart = assetManager.createTexture(getClass().getResourceAsStream("/GreenTurret.png"));

        return new ShootingTowerBuilder()
                .setiTower(this)
                .setName("Green Tower")
                .setDamage(1)
                .setRange(300)
                .setAttackSpeed(2f)
                .setProjectileSpeed(400)
                .setCost(75)
                .setRotationSpeed(90)
                .setBaseAsset(basePart)
                .setTurretAsset(turretPart)
                .setTurretAxisX(turretPart.getHeight() / 2)
                .setTurretAxisY(turretPart.getHeight() / 2)
                .getResult();
    }

    @Override
    public IAsset getProjectileAsset() {
        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
        return assetManager.loadAsset(getClass().getResourceAsStream("/Missile.png"));
    }
}
