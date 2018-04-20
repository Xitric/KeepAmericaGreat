package com.kag.aimingtowersbasic;

import com.kag.common.entities.Entity;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commontower.spinterfaces.ITower;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ITower.class)
public class BasicTower implements ITower {

    private IAsset missileAsset;
    private IAsset yellowTowerAsset;
    private IAsset yellowBaseAsset;
    private IAsset yellowTurretAsset;
    private int cost = 20;

    @Override
    public IAsset getAsset() {

        if (yellowTowerAsset == null) {
            IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
            yellowTowerAsset = assetManager.loadAsset(getClass().getResourceAsStream("/YellowTower.png"));
        }
        return yellowTowerAsset;
    }

    @Override
    public Entity create() {

        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

        if (yellowBaseAsset == null && yellowTurretAsset == null) {
            yellowBaseAsset = assetManager.loadAsset(getClass().getResourceAsStream("/YellowBase.png"));
            yellowTurretAsset = assetManager.loadAsset(getClass().getResourceAsStream("/YellowTurret.png"));
        }

        AssetPart baseAsset = assetManager.createTexture(yellowBaseAsset, 0, 0, 58, 58);
        AssetPart turretAsset = assetManager.createTexture(yellowTurretAsset, 0, 0, 56, 26);

        return new AimingTowerBuilder()
                .setiTower(this)
                .setName("Basic Tower")
                .setDamage(1)
                .setRange(100)
                .setAttackSpeed(0.25f)
                .setProjectileSpeed(200)
                .setCost(cost)
                .setRotationSpeed(90)
                .setBaseAsset(baseAsset)
                .setTurretAsset(turretAsset)
                .setTurretAxisX(turretAsset.getHeight() / 2)
                .setTurretAxisY(turretAsset.getHeight() / 2)
                .getResult();
    }

    @Override
    public IAsset getProjectileAsset() {

        if (missileAsset == null) {
            IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
            missileAsset = assetManager.loadAsset(getClass().getResourceAsStream("/Missile.png"));
        }
        return missileAsset;
    }

    @Override
    public int getCost() {
        return cost;
    }

}
