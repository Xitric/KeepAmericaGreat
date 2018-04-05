package com.kag.enemycontroller;

import com.kag.common.entities.Entity;
import com.kag.enemycontroller.interfaces.IEnemy;

import java.util.*;

/**
 * @author Kasper
 */
public class WaveGenerator {

	public List<Entity> generateWave(int strength, List<IEnemy> enemyTypes) {
		List<Entity> wave = new ArrayList<>();

		List<IEnemy> possibleEnemyTypes = new ArrayList<>(enemyTypes);
		possibleEnemyTypes.sort(Comparator.comparingInt(IEnemy::getDifficulty));
		int possibleEnemyCount = possibleEnemyTypes.size();

		List<Double> spawnChances = new ArrayList<>();
		double range = 100;
		for (IEnemy enemyType: possibleEnemyTypes) {
			double spawnChance = range / (Math.random() * 0.4 + 1.7);
			spawnChances.add(spawnChance);
			range -= spawnChance;
		}

		range = 100 - range;

		while (strength > 0 && possibleEnemyCount > 0) {
			for (int i = 0; i < possibleEnemyTypes.size(); i++) {
				if (possibleEnemyTypes.get(i).getDifficulty() > strength) {
					range -= spawnChances.get(i);
					spawnChances.set(i, 0.0);
					possibleEnemyCount--;
				}
			}

			IEnemy enemy = chooseEnemyType(possibleEnemyTypes, spawnChances, range);
			strength -= enemy.getDifficulty();

			wave.add(enemy.create());
		}

		return wave;
	}

	private IEnemy chooseEnemyType(List<IEnemy> enemyTypes, List<Double> spawnChances, double range) {
		double choice = Math.random() * range;
		double accumulation = 0;

		for (int i = 0; i < spawnChances.size(); i++) {
			accumulation += spawnChances.get(i);

			if (accumulation >= choice) {
				return enemyTypes.get(i);
			}
		}

		return null;
	}
}
