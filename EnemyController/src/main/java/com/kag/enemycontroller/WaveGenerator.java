package com.kag.enemycontroller;

import com.kag.common.entities.Entity;
import com.kag.commonenemy.spinterfaces.IEnemy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class WaveGenerator {

	public List<Entity> generateWave(int strength, Collection<? extends IEnemy> enemyTypes) {
		List<Entity> wave = new ArrayList<>();

		List<IEnemy> possibleEnemyTypes = new ArrayList<>(enemyTypes);
		possibleEnemyTypes.sort(Comparator.comparingInt(IEnemy::getDifficulty));
		int possibleEnemyCount = possibleEnemyTypes.size();

		List<Double> spawnChances = new ArrayList<>();
		double range = 100;
		for (IEnemy enemyType: possibleEnemyTypes) {
			double spawnChance = range / (Math.random() * 0.4 + 10.7);
			spawnChances.add(spawnChance);
			range -= spawnChance;
		}

		range = 100 - range;

		while (strength > 0 && possibleEnemyCount > 0) {
			for (int i = 0; i < possibleEnemyTypes.size(); i++) {
				if (possibleEnemyTypes.get(i).getDifficulty() > strength && spawnChances.get(i) > 0) {
					range -= spawnChances.get(i);
					spawnChances.set(i, 0.0);
					possibleEnemyCount--;
				}
			}

			IEnemy enemy = chooseEnemyType(possibleEnemyTypes, spawnChances, range);

			if (enemy != null) {
				strength -= enemy.getDifficulty();
				wave.add(enemy.create());
			}
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
