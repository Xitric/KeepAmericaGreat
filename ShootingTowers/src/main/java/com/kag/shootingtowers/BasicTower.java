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

	@Override
	public IAsset getAsset() {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		return assetManager.loadAsset(getClass().getResourceAsStream("/towerTest.png"));
	}

	@Override
	public Entity create() {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		AssetPart baseAsset = assetManager.createTexture(getClass().getResourceAsStream("/YellowBase.png"));
		AssetPart turretAsset = assetManager.createTexture(getClass().getResourceAsStream("/YellowTurret.png"));

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
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		return assetManager.loadAsset(getClass().getResourceAsStream("/Missile.png"));

	}
}
