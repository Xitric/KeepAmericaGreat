package com.kag.basicupgrade;

import com.kag.common.data.*;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.IPart;
import com.kag.common.entities.parts.*;
import com.kag.common.entities.parts.gui.LabelPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.ISystem;
import com.kag.common.spinterfaces.IUpgrade;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Emil
 */
@ServiceProviders(value = {
        @ServiceProvider(service = ISystem.class),
        @ServiceProvider(service = IComponentLoader.class)
})
public class BasicUpgradeController implements ISystem, IComponentLoader {

    private static final Family towerFamily = Family.forAll(CostPart.class, PositionPart.class, WeaponPart.class);
    private static final Family upgradeFamily = Family.forAll(UpgradePart.class);

    private ServiceManager<IUpgrade> towerServiceManager;
    private List<UpgradeModel> upgradeModels;
    private List<Consumer<World>> upgradeConsumer;
    private boolean updateUpgradeMenu;
    private IAssetManager assetManager;
    private Entity towerToUpgrade;

    public BasicUpgradeController() {
        assetManager = Lookup.getDefault().lookup(IAssetManager.class);
        upgradeModels = new ArrayList<>();
    }

    @Override
    public void load(World world) {

    }

    @Override
    public void dispose(World world) {

    }

    @Override
    public void update(float dt, World world, GameData gameData) {

        if (gameData.getMouse().isButtonPressed(Mouse.BUTTON_LEFT) && gameData.getMouse().getX() < 768) {

            if (getMouseSelectedTower(gameData, world) != null) {
                removeUpgradePreviews(world);

                towerToUpgrade = getMouseSelectedTower(gameData, world);
                Collection<? extends IUpgrade> upgrades = Lookup.getDefault().lookupAll(IUpgrade.class);
                for (IUpgrade upgrade : upgrades) {
                    if (upgrade.isTowerCompatible(towerToUpgrade)) {
                        world.addEntity(addNewUpgradeToMenu(upgrade));

                    }
                }
            } else {
                removeUpgradePreviews(world);
            }
        }

        if (gameData.getMouse().isButtonPressed(Mouse.BUTTON_LEFT)) {
            if (getUpgradeEntity(gameData, world) != null) {
                System.out.println("Kommer du herind?");

                for (UpgradeModel upgradeModel : upgradeModels) {
                    if (upgradeModel.getUpgradeEntity() == getUpgradeEntity(gameData, world)) {
                        upgradeModel.getiUpgrade().upgrade(towerToUpgrade);
                        System.out.println("Tower has been upgraded");
                    }
                }

            }
        }

    }

    public Entity addNewUpgradeToMenu(IUpgrade upgrade) {
        //Create Entity from tower and return
        Entity upgradeEntity = new Entity();
        upgradeModels.add(new UpgradeModel(upgradeEntity, upgrade));

        int index = upgradeModels.size() - 1;
        System.out.println("Index of new tower added to menu: " + index);

        int menuX = index % 3;
        int menuY = index / 3;

        int menuStartX = 788 + menuX * 52;
        int menuStartY = 410 + menuY * 52;

        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

        IAsset iAsset = upgrade.getAsset();
        AssetPart assetPart = assetManager.createTexture(iAsset, 0, 0, iAsset.getWidth(), iAsset.getHeight());
        float aspectRatio = (float) iAsset.getWidth() / iAsset.getHeight();
        if (iAsset.getWidth() > iAsset.getHeight()) {
            assetPart.setWidth(48);
            assetPart.setHeight((int) (48 / aspectRatio));
        } else {
            assetPart.setHeight(48);
            assetPart.setWidth((int) (48 * aspectRatio));
        }
        int dx = (48 - assetPart.getWidth()) / 2;
        int dy = (48 - assetPart.getHeight()) / 2;
        menuStartX += dx;
        menuStartY += dy;
        assetPart.setzIndex(ZIndex.GUI_CURRENCY_ICON);
        AbsolutePositionPart positionPart = new AbsolutePositionPart(menuStartX, menuStartY);

        upgradeEntity.addPart(assetPart);
        upgradeEntity.addPart(positionPart);
        upgradeEntity.addPart(new UpgradePart());

        return upgradeEntity;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    private Entity getMouseSelectedTower(GameData gameData, World world) {
        float xTilePositionOnMap = (gameData.getCamera().getX() - gameData.getWidth() / 2 + gameData.getMouse().getX());
        float yTilePositionOnMap = (gameData.getCamera().getY() - gameData.getHeight() / 2 + gameData.getMouse().getY());
        Entity entity = world.getEntityAt(xTilePositionOnMap, yTilePositionOnMap);
        if (entity == null) {
            return null;
        }
        if (towerFamily.matches(entity.getBits())) {
            System.out.println("upgrade: found a tower");
            return entity;
        }
        return null;
    }

    private Entity getUpgradeEntity(GameData gameData, World world) {
        float xTilePositionOnMap = (gameData.getCamera().getX() - gameData.getWidth() / 2 + gameData.getMouse().getX());
        float yTilePositionOnMap = (gameData.getCamera().getY() - gameData.getHeight() / 2 + gameData.getMouse().getY());

        System.out.println(xTilePositionOnMap);
        System.out.println(yTilePositionOnMap);

        Entity entity = world.getEntityAt(xTilePositionOnMap, yTilePositionOnMap);
        System.out.println(entity);
        System.out.println("Listen af " + upgradeModels.size());

        if (entity == null) {
            return null;
        }
        if (upgradeFamily.matches(entity.getBits())) {
            System.out.println("upgrade: found a upgrade");
            return entity;
        }
        return null;
    }

    private void removeUpgradePreviews(World world) {
        for (UpgradeModel upgradeModel : upgradeModels) {
            world.removeEntity(upgradeModel.getUpgradeEntity());
            System.out.println("Upgrademodel is removed from the world! " + upgradeModel);
        }
        upgradeModels.clear();
    }

}
