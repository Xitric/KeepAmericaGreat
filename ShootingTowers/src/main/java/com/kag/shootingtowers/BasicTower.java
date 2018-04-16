package com.kag.shootingtowers;

import com.kag.common.data.IAsset;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.tdcommon.spinterfaces.ITower;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ITower.class)
public class BasicTower implements ITower {

    private IAsset missileAsset;
    private IAsset yellowTowerAsset;
    private IAsset yellowBaseAsset;
    private IAsset yellowTurretAsset;

    @Override
    public IAsset getAsset() {

        if (yellowTowerAsset == null) {
            IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
            yellowTowerAsset = assetManager.loadAsset(getClass().getResourceAsStream("/towerTest.png"));
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

        return new ShootingTowerBuilder()
                .setiTower(this)
                .setName("Basic Tower")
                .setDamage(1)
                .setRange(100)
                .setAttackSpeed(0.25f)
                .setProjectileSpeed(200)
                .setCost(20)
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

}
