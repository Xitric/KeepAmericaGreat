package com.kag.shootingtowers;

import com.kag.common.data.GameData;
import com.kag.common.data.IAsset;
import com.kag.common.data.World;
import com.kag.common.data.ZIndex;
import com.kag.common.data.math.Vector2f;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.*;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.common.spinterfaces.IProjectile;
import com.kag.towerparts.CostPart;
import com.kag.towerparts.RotationSpeedPart;
import com.kag.towerparts.WeaponPart;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

import java.util.Collection;

@ServiceProviders(value = {
		@ServiceProvider(service = IEntitySystem.class),
		@ServiceProvider(service = IComponentLoader.class)
})
public class ShootingTowerControlSystem implements IEntitySystem, IComponentLoader {

	private static final Family FAMILY = Family.forAll(NamePart.class, PositionPart.class, BlockingPart.class, RotationSpeedPart.class, CostPart.class, WeaponPart.class);
	private static final Family ENEMY_FAMILY = Family.forAll(LifePart.class, PositionPart.class, BoundingBoxPart.class, BlockingPart.class);
	private IProjectile projectileImplementation;
	private IAsset projectileAsset;

	@Override
	public void load(World world) {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		projectileAsset = assetManager.loadAsset(getClass().getResourceAsStream("/Missile.png"));
	}

	@Override
	public void dispose(World world) {
		for (Entity entity : ShootingTowerBuilder.getAllTowers()) {
			world.removeEntity(entity);
		}
		projectileAsset.dispose();
	}

	@Override
	public Family getFamily() {
		return FAMILY;
	}

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		WeaponPart weaponPart = entity.getPart(WeaponPart.class);
		weaponPart.addDelta(delta);

		float timeBetweenShot = 1 / weaponPart.getAttackSpeed();
		if (weaponPart.getTimeSinceLast() > timeBetweenShot) {
			Entity enemy = getNearestEnemy(world, entity);

			if (enemy != null) {
				shootAt(world, enemy, entity);
				weaponPart.addDelta(-timeBetweenShot);
			} else {
				weaponPart.addDelta(timeBetweenShot - weaponPart.getTimeSinceLast());
			}
		}
	}

	/**
	 * Get the nearest enemy to the specified tower. This method considers only enemies that are within the range of the
	 * tower. If no enemy is close enough, this method will return null.
	 *
	 * @param world the game world
	 * @param tower the tower from which to find the nearest enemy
	 * @return the enemy nearest to the tower, or null if no enemies are in range
	 */
	private Entity getNearestEnemy(World world, Entity tower) {
		PositionPart towerPositionPart = tower.getPart(PositionPart.class);
		WeaponPart towerWeaponPart = tower.getPart(WeaponPart.class);

		Entity nearest = null;
		float shortestDist = towerWeaponPart.getRange();

		for (Entity enemy : world.getEntitiesByFamily(ENEMY_FAMILY)) {
			PositionPart enemyPositionPart = enemy.getPart(PositionPart.class);
			float distance = (float) Math.hypot(enemyPositionPart.getX() - towerPositionPart.getX(), enemyPositionPart.getY() - towerPositionPart.getY());

			if (distance <= shortestDist) {
				nearest = enemy;
				shortestDist = distance;
			}
		}

		return nearest;
	}

	private void shootAt(World world, Entity enemy, Entity tower) {
		projectileImplementation = Lookup.getDefault().lookup(IProjectile.class);

		WeaponPart weaponPart = tower.getPart(WeaponPart.class);
		PositionPart enemyPositionPart = enemy.getPart(PositionPart.class);
		PositionPart towerPositionPart = tower.getPart(PositionPart.class);

		Collection<AssetPart> assetParts = tower.getParts(AssetPart.class);
		AssetPart turretAsset = null;


		for(AssetPart assetPart : assetParts){
			if(assetPart.getzIndex() == ZIndex.TOWER_TURRET.value){
				turretAsset = assetPart;
			}
		}

		//Calculate rotation
		Vector2f move = new Vector2f(enemyPositionPart.getX() - towerPositionPart.getX(), enemyPositionPart.getY() - towerPositionPart.getY());
		Vector2f lookDir = move.normalize();
		float rotationPi = (float) Math.atan2(lookDir.det(Vector2f.AXIS_X), lookDir.dot(Vector2f.AXIS_X));
		float rotationResult = -(float) (rotationPi / (2 * Math.PI) * 360);

		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		AssetPart pAsset = assetManager.createTexture(projectileAsset, 0, 0, projectileAsset.getWidth(), projectileAsset.getHeight());
		if(turretAsset != null) {
			turretAsset.setRotation(rotationResult);
		}
		projectileImplementation.createProjectile(towerPositionPart.getX(), towerPositionPart.getY(), weaponPart.getDamage(), weaponPart.getProjectileSpeed(), rotationResult, world, pAsset);
	}

	@Override
	public int getPriority() {
		return 0;
	}
}
