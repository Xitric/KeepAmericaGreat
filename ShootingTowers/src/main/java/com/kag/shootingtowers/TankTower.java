package com.kag.shootingtowers;

import com.kag.common.data.IAsset;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.interfaces.ITower;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ITower.class)
public class TankTower implements ITower {
    @Override
    public IAsset getAsset() {
        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
        return assetManager.loadAsset(getClass().getResourceAsStream("/TankTower.png"));
    }

    @Override
    public Entity create() {

        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
        AssetPart basePart = assetManager.createTexture(getClass().getResourceAsStream("/GenericBaseA.png"));
        AssetPart turretPart = assetManager.createTexture(getClass().getResourceAsStream("/TankB.png"));

        return new ShootingTowerBuilder()
                .setiTower(this)
                .setName("Tank Tower")
                .setDamage(2)
                .setRange(200)
                .setAttackSpeed(0.10f)
                .setProjectileSpeed(200)
                .setCost(30)
                .setRotationSpeed(45)
                .setBaseAsset(basePart)
                .setTurretAsset(turretPart)
                .setTurretAxisX(turretPart.getHeight() / 2)
                .setTurretAxisY(turretPart.getHeight() / 2)
                .getResult();

    }

    @Override
    public IAsset getProjectileAsset() {
        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
        return assetManager.loadAsset(getClass().getResourceAsStream("/cannonball.png"));
    }
}
