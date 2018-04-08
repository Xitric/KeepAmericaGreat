package com.kag.shootingtowers;

import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.*;
import com.kag.towerparts.CostPart;
import com.kag.towerparts.DamagePart;
import com.kag.towerparts.RotationSpeedPart;
import com.kag.towerparts.TowerPart;

import java.util.ArrayList;
import java.util.List;

public class ShootingTowerFactory {

	private static ShootingTowerFactory INSTANCE = null;

	private static List<Entity> listOfTowers;

	private ShootingTowerFactory() {
		listOfTowers = new ArrayList<>();
	}

	public static ShootingTowerFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ShootingTowerFactory();
		}
		return INSTANCE;
	}

	public Entity createTower(String name, int damage, int range, int attackSpeed, float projectileSpeed, int cost, float rotationSpeed, AssetPart assetPart) {
		//Creating new parts for tower
		NamePart namePart = new NamePart(name);
		PositionPart positionPart = new PositionPart(0, 0);
		DamagePart damagePart = new DamagePart(damage, range, attackSpeed, projectileSpeed);
		CostPart costPart = new CostPart(cost);
		BlockingPart blockingPart = new BlockingPart();
		RotationSpeedPart rotationSpeedPart = new RotationSpeedPart(rotationSpeed);
		TowerPart towerPart = new TowerPart();
		BoundingBoxPart boundingBoxPart = new BoundingBoxPart(assetPart.getWidth(), assetPart.getHeight());

		assetPart.setxOffset(- assetPart.getWidth() / 2);
		assetPart.setyOffset(- assetPart.getHeight() / 2);
		
		//Creating new entity and adding parts
		Entity newTowerEntity = new Entity();

		newTowerEntity.addPart(namePart);
		newTowerEntity.addPart(positionPart);
		newTowerEntity.addPart(damagePart);
		newTowerEntity.addPart(costPart);
		newTowerEntity.addPart(blockingPart);
		newTowerEntity.addPart(rotationSpeedPart);
		newTowerEntity.addPart(assetPart);
		newTowerEntity.addPart(boundingBoxPart);
		newTowerEntity.addPart(towerPart);

		listOfTowers.add(newTowerEntity);

		return newTowerEntity;
	}

	public List<Entity> getTowersCreated() {
		return listOfTowers;
	}
}
