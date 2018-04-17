package com.kag.shootingtowers;

import com.kag.common.data.ZIndex;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.*;
import com.kag.tdcommon.entities.parts.MoneyPart;
import com.kag.tdcommon.entities.parts.TowerPart;
import com.kag.tdcommon.entities.parts.WeaponPart;
import com.kag.tdcommon.spinterfaces.ITower;
import parts.RotationSpeedPart;
import parts.ShootingTowerPart;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kasper
 */
public class ShootingTowerBuilder {

	private String name;
	private int damage;
	private int range;
	private int cost;
	private ITower iTower;
	private float attackSpeed;
	private float projectileSpeed;
	private float rotationSpeed;
	private AssetPart baseAsset;
	private AssetPart turretAsset;
	private int turretAxisX;
	private int turretAxisY;

	public ShootingTowerBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public ShootingTowerBuilder setDamage(int damage) {
		this.damage = damage;
		return this;
	}

	public ShootingTowerBuilder setRange(int range) {
		this.range = range;
		return this;
	}

	public ShootingTowerBuilder setCost(int cost) {
		this.cost = cost;
		return this;
	}

	public ShootingTowerBuilder setAttackSpeed(float attackSpeed) {
		this.attackSpeed = attackSpeed;
		return this;
	}

	public ShootingTowerBuilder setProjectileSpeed(float projectileSpeed) {
		this.projectileSpeed = projectileSpeed;
		return this;
	}

	public ShootingTowerBuilder setRotationSpeed(float rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
		return this;
	}

	public ShootingTowerBuilder setBaseAsset(AssetPart baseAsset) {
		this.baseAsset = baseAsset;
		return this;
	}

	public ShootingTowerBuilder setTurretAsset(AssetPart turretAsset) {
		this.turretAsset = turretAsset;
		return this;
	}

	public ShootingTowerBuilder setTurretAxisX(int turretAxisX) {
		this.turretAxisX = turretAxisX;
		return this;
	}

	public ShootingTowerBuilder setTurretAxisY(int turretAxisY) {
		this.turretAxisY = turretAxisY;
		return this;
	}

	public ShootingTowerBuilder setiTower(ITower iTower) {
		this.iTower = iTower;
		return this;
	}

	public Entity getResult() {
		TowerPart towerPart = new TowerPart(iTower);
		NamePart namePart = new NamePart(name);
		MoneyPart costPart = new MoneyPart(cost);
		PositionPart positionPart = new PositionPart(0, 0);
		WeaponPart weaponPart = new WeaponPart(damage, range, attackSpeed, projectileSpeed);
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
		tower.addPart(new ShootingTowerPart());
		tower.addPart(towerPart);
		tower.addPart(namePart);
		tower.addPart(costPart);
		tower.addPart(positionPart);
		tower.addPart(weaponPart);
		tower.addPart(rotationSpeedPart);
		tower.addPart(boundingBoxPart);
		tower.addPart(blockingPart);
		tower.addPart(baseAsset);
		tower.addPart(turretAsset);

		return tower;
	}
}
