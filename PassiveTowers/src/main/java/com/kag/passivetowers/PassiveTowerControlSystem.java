package com.kag.passivetowers;

import com.kag.common.data.Tile;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.spinterfaces.IComponentLoader;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IComponentLoader.class)
public class PassiveTowerControlSystem implements IComponentLoader {

    @Override
    public void load(World world) {

    }

    @Override
    public void dispose(World world) {
        for(Entity entity : PassiveTowerFactory.getTowersCreated()){
            float xTilePositionOnMap = (entity.getPart(PositionPart.class).getX()) / 64;
            float yTilePositionOnMap = (entity.getPart(PositionPart.class).getY()) / 64;
            Tile tile = world.getGameMap().getTile((int) xTilePositionOnMap, (int) yTilePositionOnMap);
            tile.setWalkable(true);
            world.removeEntity(entity);
        }
    }

}
