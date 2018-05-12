package com.kag.aimingtowerscontroller;

import com.kag.common.data.GameData;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.commonmath.Vector2f;
import com.kag.commontoweraiming.entities.parts.AimingTowerPart;
import com.kag.commontoweraiming.entities.parts.RotationSpeedPart;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IEntitySystem.class)
public class TowerRotationSystem implements IEntitySystem {

	private static final Family ROTATING_TOWER_FAMILY = Family.forAll(AimingTowerPart.class, RotationSpeedPart.class);

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		AimingTowerPart aimingTowerPart = entity.getPart(AimingTowerPart.class);

		if (aimingTowerPart.getNearestEnemy() != null) {
			rotateTower(entity, calculateRotationForTower(aimingTowerPart.getNearestEnemy(), entity, delta));
			aimingTowerPart.setRotationDifference(rotationDifference(entity, aimingTowerPart.getNearestEnemy()));
		}
	}

	private void rotateTower(Entity tower, float rotationResult) {
		tower.getPart(AimingTowerPart.class).getTurret().setRotation(rotationResult);
	}

	private float getTowerRotation(Entity tower) {
		return tower.getPart(AimingTowerPart.class).getTurret().getRotation();
	}

	private float calculateRotation(Entity enemy, Entity tower) {
		PositionPart enemyPositionPart = enemy.getPart(PositionPart.class);
		PositionPart towerPositionPart = tower.getPart(PositionPart.class);

		Vector2f move = new Vector2f(enemyPositionPart.getX() - towerPositionPart.getX(), enemyPositionPart.getY() - towerPositionPart.getY());
		Vector2f lookDir = move.normalize();
		float rotationPi = (float) Math.atan2(lookDir.det(Vector2f.AXIS_X), lookDir.dot(Vector2f.AXIS_X));
		return -(float) (rotationPi / (2 * Math.PI) * 360);
	}

	private float rotationDifference(Entity tower, Entity enemy) {
		float towerRotation = getTowerRotation(tower);
		float rotationToEnemy = calculateRotation(enemy, tower);

		return (rotationToEnemy - towerRotation) % 360;
	}

	private float calculateRotationForTower(Entity enemy, Entity tower, float dt) {
		float rotationSpeed = tower.getPart(RotationSpeedPart.class).getRotationSpeed();
		float towerRotation = getTowerRotation(tower);

		float difference = rotationDifference(tower, enemy);
		if(Math.abs(difference) > 180) {
			difference -= 360;
		}
		float toRotate = Math.max(Math.min(rotationSpeed * dt, difference), -rotationSpeed*dt);

		return towerRotation + toRotate;
	}

	@Override
	public Family getFamily() {
		return ROTATING_TOWER_FAMILY;
	}

	@Override
	public int getPriority() {
		return UPDATE_PASS_2;
	}
}
