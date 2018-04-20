package com.kag.passivetowers;

import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.BlockingPart;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.map.Tile;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commontd.entities.parts.MoneyPart;
import com.kag.commontower.entities.parts.TowerPart;
import com.kag.commontower.spinterfaces.ITower;
import org.openide.util.lookup.ServiceProvider;
import java.util.ArrayList;
import java.util.List;

@ServiceProvider(service = IComponentLoader.class)
public class PassiveTowerFactory implements IComponentLoader {

    private final List<Entity> listOfTowers = new ArrayList<>();

    public Entity createPassiveTower(int cost, AssetPart assetPart, ITower iTower) {
        PositionPart positionPart = new PositionPart(0, 0);
        MoneyPart costPart = new MoneyPart(cost);
        BlockingPart blockingPart = new BlockingPart();
        TowerPart towerPart = new TowerPart(iTower);
        BoundingBoxPart boundingBoxPart = new BoundingBoxPart(assetPart.getWidth(), assetPart.getHeight());

        assetPart.setxOffset(- assetPart.getWidth() / 2);
        assetPart.setyOffset(- assetPart.getHeight() / 2);

        //Creating new entity and adding parts
        Entity newTowerEntity = new Entity();
        newTowerEntity.addPart(positionPart);
        newTowerEntity.addPart(costPart);
        newTowerEntity.addPart(blockingPart);
        newTowerEntity.addPart(assetPart);
        newTowerEntity.addPart(boundingBoxPart);
        newTowerEntity.addPart(towerPart);
        listOfTowers.add(newTowerEntity);
        return newTowerEntity;
    }

    @Override
    public void load(World world) {

    }

    @Override
    public void dispose(World world) {
        for(Entity entity : listOfTowers){
            float xTilePositionOnMap = (entity.getPart(PositionPart.class).getX()) / 64;
            float yTilePositionOnMap = (entity.getPart(PositionPart.class).getY()) / 64;
            Tile tile = world.getGameMap().getTile((int) xTilePositionOnMap, (int) yTilePositionOnMap);
            tile.setWalkable(true);
            world.removeEntity(entity);
        }
    }
}

