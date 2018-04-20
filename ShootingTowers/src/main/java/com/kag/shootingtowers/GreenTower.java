package com.kag.shootingtowers;

import com.kag.common.entities.Entity;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commontower.spinterfaces.ITower;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ITower.class)
public class GreenTower implements ITower {

    private IAsset projectileAsset;
    private IAsset greenTowerAsset;
    private IAsset genericBaseBAsset;
    private IAsset greenTurretAsset;
    private int cost = 75;

    @Override
    public IAsset getAsset() {

        if (greenTowerAsset == null) {
            IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
            greenTowerAsset = assetManager.loadAsset(getClass().getResourceAsStream("/GreenTower.png"));
        }
        return greenTowerAsset;
    }

    @Override
    public Entity create() {

        if (genericBaseBAsset == null && greenTurretAsset == null) {
            IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
            genericBaseBAsset = assetManager.loadAsset(getClass().getResourceAsStream("/GenericBaseB.png"));
            greenTurretAsset = assetManager.loadAsset(getClass().getResourceAsStream("/GreenTurret.png"));
        }

        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
        AssetPart basePart = assetManager.createTexture(genericBaseBAsset,0,0,58,58);
        AssetPart turretPart = assetManager.createTexture(greenTurretAsset,0,0,63,48);

        return new ShootingTowerBuilder()
                .setiTower(this)
                .setName("Green Tower")
                .setDamage(1)
                .setRange(300)
                .setAttackSpeed(2f)
                .setProjectileSpeed(400)
                .setCost(cost)
                .setRotationSpeed(90)
                .setBaseAsset(basePart)
                .setTurretAsset(turretPart)
                .setTurretAxisX(turretPart.getHeight() / 2)
                .setTurretAxisY(turretPart.getHeight() / 2)
                .getResult();
    }

    @Override
    public IAsset getProjectileAsset() {

        if (projectileAsset == null) {
            IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
            projectileAsset = assetManager.loadAsset(getClass().getResourceAsStream("/Missile.png"));
        }
        return projectileAsset;
    }

    @Override
    public int getCost() {
        return 75;
    }
}
