package com.kag.enemycontroller;

import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.CurrencyPart;
import com.kag.common.entities.parts.LifePart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.enemycontroller.parts.EnemyPart;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Kasper
 */
@ServiceProvider(service = IEntitySystem.class)
public class EnemyDeathSystem implements IEntitySystem {

	private static final Family FAMILY = Family.forAll(EnemyPart.class, LifePart.class);
	private static final Family PLAYER_FAMILY = Family.forAll(CurrencyPart.class, LifePart.class, PositionPart.class, BoundingBoxPart.class).excluding(EnemyPart.class);

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
