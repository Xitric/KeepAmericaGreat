package com.kag.projectile;

import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.MovingPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IGameStateListener;
import com.kag.commonprojectile.entities.parts.DamagePart;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Kasper
 */
@ServiceProvider(service = IGameStateListener.class)
public class ProjectileGameReset implements IGameStateListener {

	private static final Family PROJECTILE_FAMILY = Family.forAll(PositionPart.class, BoundingBoxPart.class, DamagePart.class, MovingPart.class);

	@Override
	public void newGame(World world) {
		for (Entity projectile : world.getEntitiesByFamily(PROJECTILE_FAMILY)) {
			world.removeEntity(projectile);
		}
	}
}
