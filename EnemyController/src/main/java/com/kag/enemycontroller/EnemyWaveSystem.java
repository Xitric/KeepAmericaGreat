package com.kag.enemycontroller;

import com.kag.common.data.GameData;
import com.kag.common.data.ServiceManager;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.input.Keyboard;
import com.kag.common.input.Mouse;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IGameStateListener;
import com.kag.common.spinterfaces.ISystem;
import com.kag.commonasset.ZIndex;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.entities.parts.LabelPart;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commonenemy.entities.parts.EnemyPart;
import com.kag.commonenemy.spinterfaces.IEnemy;
import com.kag.commonplayer.entities.parts.PlayerPart;
import com.kag.commontd.entities.parts.MoneyPart;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

import java.util.List;

@ServiceProviders(value = {
		@ServiceProvider(service = ISystem.class),
		@ServiceProvider(service = IComponentLoader.class),
		@ServiceProvider(service = IGameStateListener.class)
})
public class EnemyWaveSystem implements ISystem, IComponentLoader, IGameStateListener {

	private static final Family PLAYER_FAMILY = Family.forAll(PlayerPart.class);

	private static final float spawnDelay = 0.3f; //The delay between spawning two enemies
	private static final float waveDelay = 30f; //The delay between waves

	private ServiceManager<IEnemy> enemyServiceManager;
	private List<Entity> wave;
	private WaveGenerator waveGenerator;
	private boolean waveInvalidated;
	private int waveNumber;
	private float nextWaveCountdown;
	private float nextSpawnCountdown;
	private Entity countDownLabel;
	private Entity nextWaveButton;

	@Override
	public void load(World world) {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		nextWaveButton = new Entity();
		nextWaveButton.addPart(new AbsolutePositionPart(38, 586));
		AssetPart waveImage = assetManager.createTexture(getClass().getResourceAsStream("/next.png"));
		waveImage.setxOffset(-waveImage.getWidth() / 2);
		waveImage.setyOffset(-waveImage.getHeight() / 2);
		waveImage.setzIndex(ZIndex.WAVE_IMAGE);
		nextWaveButton.addPart(waveImage);
		nextWaveButton.addPart(new BoundingBoxPart(waveImage.getWidth(), waveImage.getHeight()));

		countDownLabel = new Entity();
		LabelPart labelPart = new LabelPart("Wave " + waveNumber + " in " + String.valueOf(Math.round(nextWaveCountdown)), 13);
		countDownLabel.addPart(new AbsolutePositionPart(15, 620));
		countDownLabel.addPart(labelPart);
		labelPart.setzIndex(ZIndex.WAVE_COUNTDOWN);

		enemyServiceManager = new ServiceManager<>(IEnemy.class, null, this::removeEnemyType);
		waveGenerator = new WaveGenerator();
		world.addEntity(countDownLabel);
	}

	private synchronized void removeEnemyType(IEnemy enemyType) {
		waveInvalidated = true;
	}

	@Override
	public void dispose(World world) {
		world.removeEntity(countDownLabel);
		world.removeEntity(nextWaveButton);
	}

	@Override
	public synchronized void update(float dt, World world, GameData gameData) {
		if (isNextWavePressed(world, gameData, nextWaveButton) || gameData.getKeyboard().isKeyPressed(Keyboard.KEY_SPACE)) {
			nextWaveCountdown = 0;
		}

		if (nextWaveCountdown > 0) {
			nextWaveCountdown -= dt;
			countDownLabel.getPart(LabelPart.class).setLabel("Wave " + waveNumber + " in " + String.valueOf(Math.round(nextWaveCountdown)));

			//If we are counting down to a wave when enemies were unloaded, just regenerate the next wave
			if (waveInvalidated) {
				wave = waveGenerator.generateWave(getWaveStrength(waveNumber), enemyServiceManager.getServiceProviders());
				waveInvalidated = false;
			}
		} else {
			//If we are currently running a wave when enemies were unloaded, just clear the wave and move to the next
			if (waveInvalidated) {
				wave.clear();
				waveInvalidated = false;
			}

			if (wave == null || wave.size() == 0) {
				if (wave != null) {
					rewardPlayer(world);
				}

				wave = waveGenerator.generateWave(getWaveStrength(waveNumber), enemyServiceManager.getServiceProviders());
				nextWaveCountdown = waveDelay;
				waveNumber++;

				world.addEntity(nextWaveButton);
			} else {

				nextSpawnCountdown += dt;
				countDownLabel.getPart(LabelPart.class).setLabel("Enemies spawning");
				world.removeEntity(nextWaveButton);

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

	private boolean isNextWavePressed(World world, GameData gameData, Entity nextWaveButton) {
		return gameData.getMouse().isButtonPressed(Mouse.BUTTON_LEFT) &&
				world.isEntityAt(nextWaveButton, gameData.getMouse().getX(), gameData.getMouse().getY());
	}

	private int getWaveStrength(int waveNumber) {
		float linear = 2 * waveNumber + 10;
		float exp = (float) Math.exp(0.12 * waveNumber);
		return (int) (linear + exp);
	}

	@Override
	public int getPriority() {
		return UPDATE_PASS_1;
	}

	private void rewardPlayer(World world) {
		Entity trump = world.getEntitiesByFamily(PLAYER_FAMILY).stream().findFirst().orElse(null);
		if (trump != null) {
			MoneyPart money = trump.getPart(MoneyPart.class);
			money.setMoney(money.getMoney() + 30);
		}
	}

	@Override
	public void newGame(World world) {
		//Reset instance variables that result in the wave generator restarting
		wave = null;
		waveNumber = 0;
		nextWaveCountdown = 0;

		for (Entity enemy : world.getEntitiesByFamily(Family.forAll(EnemyPart.class))) {
			world.removeEntity(enemy);
		}
	}
}
