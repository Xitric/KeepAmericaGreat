package com.kag.shootingtowers;

import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.entities.parts.BlockingPart;
import com.kag.common.entities.parts.NamePart;
import com.kag.towerparts.CostPart;
import com.kag.towerparts.DamagePart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.towerparts.RotationSpeedPart;

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
		
		//Creating new entity and adding parts
		Entity newTowerentity = new Entity();

		newTowerentity.addPart(namePart);
		newTowerentity.addPart(positionPart);
		newTowerentity.addPart(damagePart);
		newTowerentity.addPart(costPart);
		newTowerentity.addPart(blockingPart);
		newTowerentity.addPart(rotationSpeedPart);
		newTowerentity.addPart(assetPart);

		listOfTowers.add(newTowerentity);

		return newTowerentity;
	}

	public List<Entity> getTowersCreated() {
		return listOfTowers;
	}
}
