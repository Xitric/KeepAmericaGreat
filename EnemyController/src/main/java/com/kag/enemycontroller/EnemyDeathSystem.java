package com.kag.enemycontroller;

import com.kag.common.data.GameData;
import com.kag.common.data.ISound;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.CurrencyPart;
import com.kag.common.entities.parts.LifePart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.spinterfaces.IAudioManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.enemycontroller.parts.EnemyPart;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 * @author Kasper
 */
@ServiceProviders(value = {
		@ServiceProvider(service = IEntitySystem.class),
		@ServiceProvider(service = IComponentLoader.class)
})
public class EnemyDeathSystem implements IEntitySystem, IComponentLoader {

	private static final Family FAMILY = Family.forAll(EnemyPart.class, LifePart.class);
	private static final Family PLAYER_FAMILY = Family.forAll(CurrencyPart.class, LifePart.class, PositionPart.class, BoundingBoxPart.class).excluding(EnemyPart.class);

	private ISound[] deathSounds;

	@Override
	public void load(World world) {
		IAudioManager audioManager = Lookup.getDefault().lookup(IAudioManager.class);

		deathSounds = new ISound[3];
		deathSounds[0] = audioManager.loadSound(getClass().getResourceAsStream("/Mexican1.mp3"), "mp3");
		deathSounds[1] = audioManager.loadSound(getClass().getResourceAsStream("/Mexican2.mp3"), "mp3");
		deathSounds[2] = audioManager.loadSound(getClass().getResourceAsStream("/Mexican3.mp3"), "mp3");
	}

	@Override
	public void dispose(World world) {
		for (ISound sound : deathSounds) {
			sound.dispose();
		}
		deathSounds = null;
	}

	@Override
	public Family getFamily() {
		return FAMILY;
	}

	@Override
	public void update(float delta, Entity entity, World world, GameData gameData) {
		if (entity.getPart(LifePart.class).getHealth() <= 0) {

			//Give money to player
			Entity trump = getPlayer(world);
			if (trump != null && entity.hasPart(CurrencyPart.class)) {
				CurrencyPart money = trump.getPart(CurrencyPart.class);
				money.setCurrencyAmount(money.getCurrencyAmount() + entity.getPart(CurrencyPart.class).getCurrencyAmount());
			}

			deathSounds[(int) (Math.random() * deathSounds.length)].play();
			world.removeEntity(entity);
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

	@Override
	public int getPriority() {
		return UPDATE_PASS_3;
	}
}
