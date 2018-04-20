package com.kag.aimingtowersbasic;

import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.map.Tile;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IComponentLoader;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Kasper
 */
@ServiceProvider(service = IComponentLoader.class)
public class Unloader implements IComponentLoader {

	private static final Family FAMILY = Family.forAll(AimingTowersBasicPart.class);

	@Override
	public void load(World world) {

	}

	@Override
	public void dispose(World world) {
		for(Entity entity : world.getEntitiesByFamily(FAMILY)){
			PositionPart towerPositionPart = entity.getPart(PositionPart.class);
			Tile hoverTile = world.getGameMap().getTile((int) towerPositionPart.getX() / world.getGameMap().getTileWidth() , (int) towerPositionPart.getY() / world.getGameMap().getTileWidth());
			hoverTile.setWalkable(true);
			world.removeEntity(entity);
		}
	}
}
