package com.kag.aimingtowerscontroller;

import com.kag.common.data.GameData;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commonaudio.spinterfaces.IAudioManager;
import com.kag.commonaudio.spinterfaces.ISound;
import com.kag.commonprojectile.entities.parts.ProjectileSpeedPart;
import com.kag.commonprojectile.spinterfaces.IProjectile;
import com.kag.commontower.entities.parts.TowerPart;
import com.kag.commontower.entities.parts.WeaponPart;
import com.kag.commontoweraiming.entities.parts.AimingTowerPart;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
		@ServiceProvider(service = IComponentLoader.class),
		@ServiceProvider(service = IEntitySystem.class)
})
public class TowerShootingSystem implements IComponentLoader, IEntitySystem {

	private static final Family FAMILY = Family.forAll(TowerPart.class, AimingTowerPart.class, PositionPart.class, WeaponPart.class, ProjectileSpeedPart.class);

	private ISound shootingSound;

	@Override
	public void load(World world) {
		IAudioManager audioManager = Lookup.getDefault().lookup(IAudioManager.class);
		shootingSound = audioManager.loadSound(getClass().getResourceAsStream("/shooting.wav"), "wav");
	}

	@Override
	public void dispose(World world) {
		shootingSound.dispose();
	}

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		AimingTowerPart aimingTowerPart = entity.getPart(AimingTowerPart.class);
		WeaponPart weaponPart = entity.getPart(WeaponPart.class);
		weaponPart.addDelta(delta);

		float timeBetweenShot = 1 / weaponPart.getAttackSpeed();
		if (weaponPart.getTimeSinceLast() > timeBetweenShot) {
			if (aimingTowerPart.getNearestEnemy() != null && Math.abs(aimingTowerPart.getRotationDifference()) < 25) {
				shootAt(world, entity);
				weaponPart.addDelta(-timeBetweenShot);
			} else {
				weaponPart.addDelta(timeBetweenShot - weaponPart.getTimeSinceLast());
			}
		}
	}

	private void shootAt(World world, Entity tower) {
		IProjectile projectileImplementation = Lookup.getDefault().lookup(IProjectile.class);
		if (projectileImplementation == null) return;

		TowerPart towerPart = tower.getPart(TowerPart.class);
		WeaponPart weaponPart = tower.getPart(WeaponPart.class);
		ProjectileSpeedPart projectileSpeedPart = tower.getPart(ProjectileSpeedPart.class);
		PositionPart towerPositionPart = tower.getPart(PositionPart.class);
		AimingTowerPart aimingTowerPart = tower.getPart(AimingTowerPart.class);

		//Calculate rotation
		AssetPart turretPart = aimingTowerPart.getTurret();
		float rotationResult = turretPart.getRotation();

		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		AssetPart pAsset = assetManager.createTexture(towerPart.getiTower().getProjectileAsset(),0,0,towerPart.getiTower().getProjectileAsset().getWidth(),towerPart.getiTower().getProjectileAsset().getHeight());
		pAsset.setyOffset(- pAsset.getHeight() / 2);

		int turretLength = turretPart.getWidth() + turretPart.getxOffset();
		float turretEndX = (float) (Math.cos(rotationResult / 360 * 2 * Math.PI) * turretLength) + towerPositionPart.getX();
		float turretEndY = (float) (Math.sin(rotationResult / 360 * 2 * Math.PI) * turretLength) + towerPositionPart.getY();

		shootingSound.play();

		projectileImplementation.createProjectile(turretEndX, turretEndY, weaponPart.getDamage(), projectileSpeedPart.getProjectileSpeed(), rotationResult, world, pAsset);
	}

	@Override
	public Family getFamily() {
		return FAMILY;
	}

	@Override
	public int getPriority() {
		return UPDATE_PASS_2;
	}
}
