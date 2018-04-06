package com.kag.shootingtowers;

import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.data.math.Vector2f;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.*;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.common.spinterfaces.IProjectile;
import com.kag.common.spinterfaces.ISystem;
import com.kag.towerparts.CostPart;
import com.kag.towerparts.WeaponPart;
import com.kag.towerparts.RotationSpeedPart;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
	@ServiceProvider(service = IEntitySystem.class)
	,
        @ServiceProvider(service = IComponentLoader.class)
})
public class ShootingTowerControlSystem implements IEntitySystem, IComponentLoader {

	private static final Family FAMILY = Family.forAll(NamePart.class, PositionPart.class, BlockingPart.class, RotationSpeedPart.class, CostPart.class, WeaponPart.class);
	private IAssetManager assetManager;
	IProjectile projectileImplementation;
	private Lookup lookup;
	private static final Family ENEMY_FAMILY = Family.forAll(LifePart.class, PositionPart.class, BoundingBoxPart.class, BlockingPart.class);

	public ShootingTowerControlSystem() {
		assetManager = Lookup.getDefault().lookup(IAssetManager.class);
	}

	@Override
	public void load(World world) {
		lookup = Lookup.getDefault();
		System.out.println("Shooting Tower Module loaded");
	}

	@Override
	public void dispose(World world) {
		for (Entity entity : ShootingTowerFactory.getInstance().getTowersCreated()) {
			world.removeEntity(entity);
		}
	}

	@Override
	public Family getFamily() {
		return FAMILY;
	}

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		WeaponPart weaponPart = entity.getPart(WeaponPart.class);
		weaponPart.addDelta(delta);
		for (Entity enemy : world.getAllEntities()) {
			if (ENEMY_FAMILY.matches(enemy.getBits())) {
				float timeBetweenShot = 1 / weaponPart.getAttackSpeed();
				if (timeBetweenShot < weaponPart.getTimeSinceLast()) {
					if (enemyIsInRange(entity, enemy)) {
						System.out.println("SHOOT! PEW PEW PEW PEW!");
						shootAt(world, enemy, entity);
						weaponPart.addDelta(-timeBetweenShot);
					} else {
						weaponPart.addDelta(timeBetweenShot - weaponPart.getTimeSinceLast());
					}

				}
			}
		}
	}

	private boolean enemyIsInRange(Entity tower, Entity enemy) {
		PositionPart towerPositionPart = tower.getPart(PositionPart.class);
		WeaponPart towerWeaponPart = tower.getPart(WeaponPart.class);
		PositionPart enemyPositionPart = enemy.getPart(PositionPart.class);
		
		double distance = Math.hypot(enemyPositionPart.getX() - towerPositionPart.getX(), enemyPositionPart.getY() - towerPositionPart.getY());
		System.out.println("Distance: " + distance);
		return distance <= towerWeaponPart.getRange();
	}

	private void shootAt(World world, Entity enemy, Entity tower) {
		projectileImplementation = Lookup.getDefault().lookup(IProjectile.class);

		WeaponPart weaponPart = tower.getPart(WeaponPart.class);
		PositionPart enemyPositionPart = enemy.getPart(PositionPart.class);
		PositionPart towerPositionPart = tower.getPart(PositionPart.class);
		//Calculate rotation
		Vector2f move = new Vector2f(enemyPositionPart.getX() - towerPositionPart.getX(), enemyPositionPart.getY() - towerPositionPart.getY());
		Vector2f lookDir = move.normalize();
		float rotationPi = (float) Math.atan2(lookDir.det(Vector2f.AXIS_X), lookDir.dot(Vector2f.AXIS_X));
		float rotationResult = -(float) (rotationPi / (2 * Math.PI) * 360);

		projectileImplementation.createProjectile(world, tower, weaponPart.getDamage(), weaponPart.getProjectileSpeed(), rotationResult);

	}

	@Override
	public int getPriority() {
		return 0;
	}
}
