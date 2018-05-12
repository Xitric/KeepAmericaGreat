package com.kag.commontoweraiming.entities.parts;

import com.kag.common.entities.Entity;
import com.kag.common.entities.IPart;
import com.kag.commonasset.entities.parts.AssetPart;

public class AimingTowerPart implements IPart {

	private Entity nearestEnemy;
	private float rotationDifference;
	private AssetPart turret;

	public AimingTowerPart(AssetPart turret) {
		this.turret = turret;
	}

	public Entity getNearestEnemy() {
		return nearestEnemy;
	}

	public void setNearestEnemy(Entity nearestEnemy) {
		this.nearestEnemy = nearestEnemy;
	}

	public float getRotationDifference() {
		return rotationDifference;
	}

	public void setRotationDifference(float rotationDifference) {
		this.rotationDifference = rotationDifference;
	}

	public AssetPart getTurret() {
		return turret;
	}
}
