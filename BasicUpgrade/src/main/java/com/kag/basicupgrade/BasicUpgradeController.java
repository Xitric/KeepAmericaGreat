package com.kag.basicupgrade;

import com.kag.common.data.GameData;
import com.kag.common.data.ServiceManager;
import com.kag.common.data.World;
import com.kag.common.data.ZIndex;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.CostPart;
import com.kag.common.entities.parts.DamagePart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.entities.parts.gui.LabelPart;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.ISystem;
import com.kag.common.spinterfaces.IUpgrade;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

import java.util.Collection;

/**
 *
 * @author Emil
 */
public class BasicUpgradeController implements ISystem, IComponentLoader {

    private static final Family towerFamily = Family.forAll(CostPart.class, PositionPart.class, DamagePart.class);

    private ServiceManager<IUpgrade> towerServiceManager;

    @Override
    public void load(World world) {
        towerServiceManager = new ServiceManager<>(IUpgrade.class, this::onTowersChanged, this::onTowersChanged);

    }

    @Override
    public void dispose(World world) {

    }

    @Override
    public void update(float dt, World world, GameData gameData) {
        if(getMouseSelectedTower(gameData, world)!=null){
            Entity tempTower = getMouseSelectedTower(gameData, world);
        }
    }

    @Override
    public int getPriority() {
        return 0;
    }

    private Entity getMouseSelectedTower (GameData gameData, World world){
        float xTilePositionOnMap = (gameData.getCamera().getX() - gameData.getWidth() / 2 + gameData.getMouse().getX());
        float yTilePositionOnMap = (gameData.getCamera().getY() - gameData.getHeight() / 2 + gameData.getMouse().getY());
        Entity entity = world.getEntityAt(xTilePositionOnMap,yTilePositionOnMap);
        if(entity == null){
            System.out.println("upgrade: no entity");
            return null;
        }
        if(towerFamily.matches(entity.getBits())){
            System.out.println("upgrade: found a tower");
            return entity;
        } else {
            System.out.println("upgrade: found a non-tower");
        }
        return null;
    }

    private void removeUpgradePreviews(World world) {
        for (TowerModel towerModel : towerModels) {
            world.removeEntity(towerModel.getTowerEntity());
        }
        towerModels.clear();
        updateBuyMenu = true;
    }

    private void onTowersChanged(ITower tower) {
        towerConsumer.add(this::removeUpgradePreviews);
    }

}
