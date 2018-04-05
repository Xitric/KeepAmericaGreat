package com.kag.enemycontroller;

import com.kag.common.data.GameData;
import com.kag.common.data.ServiceManager;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.ISystem;
import com.kag.enemycontroller.interfaces.IEnemy;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Kasper
 */
@ServiceProviders(value = {
		@ServiceProvider(service = ISystem.class),
		@ServiceProvider(service = IComponentLoader.class)
})
public class EnemyWaveSystem implements ISystem, IComponentLoader {

	private static final float spawnDelay = 1f; //The delay between spawning two enemies
	private static final float waveDelay = 3f; //The delay between waves

	private ServiceManager<IEnemy> enemyServiceManager;
	private List<IEnemy> enemyTypes;
	private List<Entity> wave;
	private WaveGenerator waveGenerator;
	private int waveNumber;
	private float nextWaveCountdown;
	private float nextSpawnCountdown;

	@Override
	public void load(World world) {
		enemyTypes = new CopyOnWriteArrayList<>();
		enemyServiceManager = new ServiceManager<>(IEnemy.class, enemyTypes::add, enemyTypes::remove);

		waveGenerator = new WaveGenerator();
	}

	@Override
	public void dispose(World world) {

	}

	@Override
	public void update(float dt, World world, GameData gameData) {
		if (nextWaveCountdown > 0) {
			nextWaveCountdown -= dt;
		} else {
			if (wave == null || wave.size() == 0) {
				System.out.println("Generated wave: " + getWaveStrength(waveNumber));
				wave = waveGenerator.generateWave(getWaveStrength(waveNumber), enemyTypes);
				nextWaveCountdown = waveDelay;
				waveNumber++;
			} else {
				nextSpawnCountdown += dt;

				if (nextSpawnCountdown >= spawnDelay) {
					nextSpawnCountdown -= spawnDelay;

					Entity enemy = wave.get(wave.size() - 1);

					//Calculate random spawn position
					int tileX = (int) (Math.random() * world.getGameMap().getWidth());
					PositionPart enemyPosition = enemy.getPart(PositionPart.class);
					enemyPosition.setPos(tileX * world.getGameMap().getTileWidth() + world.getGameMap().getTileWidth() / 2, world.getGameMap().getTileHeight() / 2);

					world.addEntity(enemy);
					wave.remove(wave.size() - 1);
				}
			}
		}
	}

	private int getWaveStrength(int waveNumber) {
		float linear = 2 * waveNumber + 10;
		float exp = (float) Math.exp(0.06 * waveNumber);
		return (int) (linear + exp);
	}

	@Override
	public int getPriority() {
		return UPDATE_PASS_1;
	}
}
