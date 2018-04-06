package com.kag.enemycontroller;

import com.kag.common.data.GameData;
import com.kag.common.data.Mouse;
import com.kag.common.data.ServiceManager;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.*;
import com.kag.common.entities.parts.gui.LabelPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.ISystem;
import com.kag.enemycontroller.interfaces.IEnemy;
import com.kag.enemycontroller.parts.EnemyPart;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.kag.common.data.Mouse.BUTTON_LEFT;

/**
 * @author Kasper
 */
@ServiceProviders(value = {
		@ServiceProvider(service = ISystem.class),
		@ServiceProvider(service = IComponentLoader.class)
})
public class EnemyWaveSystem implements ISystem, IComponentLoader {

	private static final Family PLAYER_FAMILY = Family.forAll(CurrencyPart.class, LifePart.class, PositionPart.class, BoundingBoxPart.class).excluding(EnemyPart.class);

	private static final float spawnDelay = 1f; //The delay between spawning two enemies
	private static final float waveDelay = 30f; //The delay between waves

	private ServiceManager<IEnemy> enemyServiceManager;
	private List<IEnemy> enemyTypes;
	private List<Entity> wave;
	private WaveGenerator waveGenerator;
	private int waveNumber;
	private float nextWaveCountdown;
	private float nextSpawnCountdown;
	private Entity countDownLabel;
	private Entity nextWaveButton;

	@Override
	public void load(World world) {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		nextWaveButton = new Entity();
		nextWaveButton.addPart(new AbsolutePositionPart(15, 550));
		nextWaveButton.addPart(new BoundingBoxPart(45,32));
		AssetPart waveImage = assetManager.createTexture(getClass().getResourceAsStream("/next.png"));
		waveImage.setzIndex(10);
		nextWaveButton.addPart(waveImage);

		countDownLabel = new Entity();
		LabelPart labelPart = new LabelPart("Wave " + waveNumber + " in "+ String.valueOf(Math.round(nextWaveCountdown)),13);
		countDownLabel.addPart(new AbsolutePositionPart(15,620));
		countDownLabel.addPart(labelPart);
		labelPart.setzIndex(10);

		enemyTypes = new CopyOnWriteArrayList<>();
		enemyServiceManager = new ServiceManager<>(IEnemy.class, enemyTypes::add, enemyTypes::remove);

		waveGenerator = new WaveGenerator();
		world.addEntity(countDownLabel);
		//world.addEntity(nextWaveButton);
	}

	@Override
	public void dispose(World world) {
		world.removeEntity(countDownLabel);
		world.removeEntity(nextWaveButton);
	}

	@Override
	public void update(float dt, World world, GameData gameData) {
		if (isNextWavePressed(gameData)){
			nextWaveCountdown = 0;
		}

		if (nextWaveCountdown > 0) {
			nextWaveCountdown -= dt;
			countDownLabel.getPart(LabelPart.class).setLabel("Wave " + waveNumber + " in " + String.valueOf(Math.round(nextWaveCountdown)));

		} else {
			if (wave == null || wave.size() == 0) {
				if (wave != null){
					rewardPlayer(world);
				}
				System.out.println("Generated wave: " + getWaveStrength(waveNumber));
				wave = waveGenerator.generateWave(getWaveStrength(waveNumber), enemyTypes);
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

	private boolean isNextWavePressed(GameData gameData){
		Mouse mouse = gameData.getMouse();
		int mouseX = mouse.getX();
		int mouseY = mouse.getY();

		float btnX = nextWaveButton.getPart(AbsolutePositionPart.class).getX();
		float btnY = nextWaveButton.getPart(AbsolutePositionPart.class).getY();

		float btnW = nextWaveButton.getPart(BoundingBoxPart.class).getWidth();
		float btnH = nextWaveButton.getPart(BoundingBoxPart.class).getHeight();

		return mouse.isButtonPressed(BUTTON_LEFT) && mouseX > btnX && mouseX < btnX + btnW && mouseY > btnY && mouseY < btnY + btnH;
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

	private void rewardPlayer(World world) {
		Entity trump = getPlayer(world);
		if (trump != null) {
			CurrencyPart money = trump.getPart(CurrencyPart.class);
			money.setCurrencyAmount(money.getCurrencyAmount() + 250);
		}
	}

	private Entity getPlayer(World world) {
		for (Entity entity : world.getAllEntities()) {
			if (PLAYER_FAMILY.matches(entity.getBits())) {
				entity.getPart(CurrencyPart.class);
				return entity;
			}
		}
		return null;
		}
	}