package com.kag.shootingtowers;

import com.kag.common.data.IAsset;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.tdcommon.spinterfaces.ITower;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ITower.class)
public class TankTower implements ITower {

    private IAsset tankTowerAsset;
    private IAsset tankBaseAAsset;
    private IAsset tankBAsset;
    private IAsset cannonBallAsset;


    @Override
    public IAsset getAsset() {

        if (tankTowerAsset == null) {
            IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
            tankTowerAsset = assetManager.loadAsset(getClass().getResourceAsStream("/TankTower.png"));
        }
        return tankTowerAsset;
    }

    @Override
    public Entity create() {

        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

        if (tankBaseAAsset == null && tankBAsset == null) {
            tankBaseAAsset = assetManager.loadAsset(getClass().getResourceAsStream("/GenericBaseA.png"));
            tankBAsset = assetManager.loadAsset(getClass().getResourceAsStream("/TankB.png"));
        }

        AssetPart basePart = assetManager.createTexture(tankBaseAAsset, 0, 0, 58, 58);
        AssetPart turretPart = assetManager.createTexture(tankBAsset, 0, 0, 62, 27);

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

        if (cannonBallAsset == null) {
            IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
            cannonBallAsset = assetManager.loadAsset(getClass().getResourceAsStream("/cannonball.png"));
        }
        return cannonBallAsset;
    }
}
