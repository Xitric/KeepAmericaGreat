package com.kag.aimingtowersbasic;

import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.BlockingPart;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.NamePart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.commonasset.ZIndex;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonprojectile.entities.parts.ProjectileSpeedPart;
import com.kag.commontd.entities.parts.MoneyPart;
import com.kag.commontower.entities.parts.TowerPart;
import com.kag.commontower.entities.parts.WeaponPart;
import com.kag.commontower.spinterfaces.ITower;
import com.kag.commontoweraiming.entities.parts.AimingTowerPart;
import com.kag.commontoweraiming.entities.parts.RotationSpeedPart;

public class AimingTowerBuilder {

	private String name;
	private int damage;
	private int range;
	private int cost;
	private ITower iTower;
	private float attackSpeed;
	private int projectileSpeed;
	private int rotationSpeed;
	private AssetPart baseAsset;
	private AssetPart turretAsset;
	private int turretAxisX;
	private int turretAxisY;

	public AimingTowerBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public AimingTowerBuilder setDamage(int damage) {
		this.damage = damage;
		return this;
	}

	public AimingTowerBuilder setRange(int range) {
		this.range = range;
		return this;
	}

	public AimingTowerBuilder setCost(int cost) {
		this.cost = cost;
		return this;
	}

	public AimingTowerBuilder setAttackSpeed(float attackSpeed) {
		this.attackSpeed = attackSpeed;
		return this;
	}

	public AimingTowerBuilder setProjectileSpeed(int projectileSpeed) {
		this.projectileSpeed = projectileSpeed;
		return this;
	}

	public AimingTowerBuilder setRotationSpeed(int rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
		return this;
	}

	public AimingTowerBuilder setBaseAsset(AssetPart baseAsset) {
		this.baseAsset = baseAsset;
		return this;
	}

	public AimingTowerBuilder setTurretAsset(AssetPart turretAsset) {
		this.turretAsset = turretAsset;
		return this;
	}

	public AimingTowerBuilder setTurretAxisX(int turretAxisX) {
		this.turretAxisX = turretAxisX;
		return this;
	}

	public AimingTowerBuilder setTurretAxisY(int turretAxisY) {
		this.turretAxisY = turretAxisY;
		return this;
	}

	public AimingTowerBuilder setiTower(ITower iTower) {
		this.iTower = iTower;
		return this;
	}

	public Entity getResult() {
		AimingTowerPart aimingTowerPart = new AimingTowerPart(turretAsset);
		AimingTowersBasicPart aimingTowersBasicPart = new AimingTowersBasicPart();
		TowerPart towerPart = new TowerPart(iTower);
		NamePart namePart = new NamePart(name);
		MoneyPart costPart = new MoneyPart(cost);
		PositionPart positionPart = new PositionPart(0, 0);
		WeaponPart weaponPart = new WeaponPart(damage, range, attackSpeed);
		ProjectileSpeedPart projectileSpeedPart = new ProjectileSpeedPart(projectileSpeed);
		RotationSpeedPart rotationSpeedPart = new RotationSpeedPart(rotationSpeed);
		BoundingBoxPart boundingBoxPart = new BoundingBoxPart(baseAsset.getWidth(), baseAsset.getHeight());
		BlockingPart blockingPart = new BlockingPart();

		baseAsset.setxOffset(-baseAsset.getWidth() / 2);
		baseAsset.setyOffset(-baseAsset.getWidth() / 2);
		baseAsset.setzIndex(ZIndex.TOWER_BASE);

		turretAsset.setxOffset(-turretAxisX);
		turretAsset.setyOffset(-turretAxisY);
		turretAsset.setOriginX(turretAxisX);
		turretAsset.setOriginY(turretAxisY);
		turretAsset.setzIndex(ZIndex.TOWER_TURRET);

		Entity tower = new Entity();
		tower.addPart(aimingTowerPart);
		tower.addPart(aimingTowersBasicPart);
		tower.addPart(towerPart);
		tower.addPart(namePart);
		tower.addPart(costPart);
		tower.addPart(positionPart);
		tower.addPart(weaponPart);
		tower.addPart(projectileSpeedPart);
		tower.addPart(rotationSpeedPart);
		tower.addPart(boundingBoxPart);
		tower.addPart(blockingPart);
		tower.addPart(baseAsset);
		tower.addPart(turretAsset);

		return tower;
	}
}
