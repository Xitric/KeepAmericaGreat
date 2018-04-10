package com.kag.passivetowers;

import com.kag.common.data.IAsset;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.*;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.interfaces.ITower;
import com.kag.towerparts.CostPart;
import com.kag.towerparts.TowerPart;
import org.openide.util.Lookup;

import java.util.ArrayList;
import java.util.List;

public class PassiveTowerFactory  {

    private static PassiveTowerFactory INSTANCE = null;
    private static List<Entity> listOfTowers;

    private PassiveTowerFactory() {
        listOfTowers = new ArrayList<>();
    }

    public static PassiveTowerFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PassiveTowerFactory();
        }
        return INSTANCE;
    }

    public Entity createPassiveTower(int cost, AssetPart assetPart, ITower iTower) {
        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
        PositionPart positionPart = new PositionPart(0, 0);
        CostPart costPart = new CostPart(cost);
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

        return newTowerEntity;
    }

    public static List<Entity> getTowersCreated() {
        return listOfTowers;
    }
}

