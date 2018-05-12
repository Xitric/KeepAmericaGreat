package com.kag.aimingtowerscontroller;

import com.kag.common.data.GameData;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.commonenemy.entities.parts.EnemyPart;
import com.kag.commontower.entities.parts.WeaponPart;
import com.kag.commontoweraiming.entities.parts.AimingTowerPart;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IEntitySystem.class)
public class EnemyLocator implements IEntitySystem {

	private static final Family AIMING_TOWER_FAMILY = Family.forAll(AimingTowerPart.class, PositionPart.class);
	private static final Family ENEMY_FAMILY = Family.forAll(EnemyPart.class, PositionPart.class);

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		AimingTowerPart aimingTowerPart = entity.getPart(AimingTowerPart.class);
		aimingTowerPart.setNearestEnemy(getNearestEnemy(world, entity));
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

	@Override
	public Family getFamily() {
		return AIMING_TOWER_FAMILY;
	}

	@Override
	public int getPriority() {
		return UPDATE_PASS_1;
	}
}
