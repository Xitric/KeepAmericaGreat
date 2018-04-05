/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.towercontroller;

import com.kag.common.data.*;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.ISystem;
import com.kag.interfaces.ITower;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

@ServiceProviders(value = {
        @ServiceProvider(service = ISystem.class),
        @ServiceProvider(service = IComponentLoader.class)
})
public class TowerMasterSystem implements ISystem, IComponentLoader {

    private Lookup lookup;
    private Entity towerMenuBackground;
    private Entity upgradeMenuBackground;
    private List<ITower> towerImple;
    private Lookup.Result<ITower> towerImpleLookupResult;
    private List<Entity> towersToBeDrawn;
    private List<Consumer<World>> towerConsumer;
    private List<TowerModel> towerModels;
    private IAssetManager assetManager = null;
    private TowerSelectionManager towerSelectionManager;


    public TowerMasterSystem() {
        towerConsumer = new ArrayList<>();
        towerModels = new ArrayList<>();
        towerSelectionManager = new TowerSelectionManager();
    }

    @Override
    public void update(float dt, World world, GameData gameData) {
        if (assetManager == null) {
            assetManager = Lookup.getDefault().lookup(IAssetManager.class);
        }

        if (!towerConsumer.isEmpty()) {
            for (Consumer<World> consumer : towerConsumer) {
                consumer.accept(world);
            }
            towerConsumer.clear();
        }

        handleTowerSelection(world, gameData);

        if (towerSelectionManager.getSelectedTower() != null) {

            if (isMouseOnPlayField(gameData)) {
                towerSelectionManager.createOverlay();
                towerSelectionManager.updateOverlay(world, gameData);
            }

            if (isMouseOnPlayField(gameData) && gameData.getMouse().isButtonPressed(Mouse.BUTTON_LEFT)) {
                towerSelectionManager.placeTowerOnField(world, gameData);
            }
        }
    }

    private boolean isMouseOnPlayField(GameData gameData) {
        return gameData.getMouse().getX() < 768;
    }


    private void handleTowerSelection(World world, GameData gameData) {
        if (gameData.getMouse().isButtonPressed(Mouse.BUTTON_LEFT)) {
            setTowerFromBuyMenu(gameData, world);

        } else if (gameData.getMouse().isButtonDown(Mouse.BUTTON_RIGHT) || gameData.getKeyboard().isKeyDown(Keyboard.KEY_ESCAPE)) {
            towerSelectionManager.resetTowerSelection(world);
        }
    }

    private void setTowerFromBuyMenu(GameData gameData, World world) {
        int mouseX = gameData.getMouse().getX();
        int mouseY = gameData.getMouse().getY();
        for (Entity tower : towersToBeDrawn) {

            AbsolutePositionPart absolutePositionPart = tower.getPart(AbsolutePositionPart.class);
            float towerXStart = absolutePositionPart.getX();
            float towerXEnd = absolutePositionPart.getX() + 48;
            float towerYStart = absolutePositionPart.getY();
            float towerYEnd = absolutePositionPart.getY() + 48;

            //If the mouse is hovering over an entity in the buy menu
            //Find a tower at the position
            if (mouseX >= towerXStart && mouseX <= towerXEnd && mouseY >= towerYStart && mouseY <= towerYEnd) {
                for (TowerModel model : towerModels) {
                    if (model.getTowerEntity() == tower) {
                        towerSelectionManager.resetTowerSelection(world);
                        towerSelectionManager.setSelectedTower(model);
                        world.addEntity(towerSelectionManager.createMouseTower(gameData, towerSelectionManager.getSelectedTower()));
                    }
                }
            }
        }
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void load(World world) {
        lookup = Lookup.getDefault();

        towersToBeDrawn = new ArrayList<>();

        towerImple = new CopyOnWriteArrayList<>();
        towerImpleLookupResult = lookup.lookupResult(ITower.class);
        towerImpleLookupResult.addLookupListener(iTowerLookupListener);

        lookup.lookupAll(ITower.class).stream().forEach((e) -> {
            Entity entity = addNewTowerToMenu(e);
            world.addEntity(entity);
            addTowerToList(new TowerModel(entity, e));
        });

        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

        AssetPart towerPanel = assetManager.createTexture(getClass().getResourceAsStream("/TowerPanel.png"));
        towerPanel.setzIndex(5);

        towerMenuBackground = new Entity();
        towerMenuBackground.addPart(towerPanel);
        towerMenuBackground.addPart(new AbsolutePositionPart(768, 128));

        AssetPart upgradePanel = assetManager.createTexture(getClass().getResourceAsStream("/TowerPanel.png"));
        upgradePanel.setzIndex(5);

        upgradeMenuBackground = new Entity();
        upgradeMenuBackground.addPart(upgradePanel);
        upgradeMenuBackground.addPart(new AbsolutePositionPart(768, 384));

        world.addEntity(towerMenuBackground);
        world.addEntity(upgradeMenuBackground);
    }

    @Override
    public void dispose(World world) {
        world.removeEntity(towerMenuBackground);
        world.removeEntity(upgradeMenuBackground);
    }


    private Entity addNewTowerToMenu(ITower tower) {
        //Create Entity from tower and return
        Entity towerEntity = new Entity();
        towersToBeDrawn.add(towerEntity);

        int index = towersToBeDrawn.indexOf(towerEntity);

        int x = index % 3;
        int y = index / 3;

        int pixelx = 788 + 4 + x * 52;
        int pixely = 154 + 1 + y * 52;

        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

        IAsset iAsset = tower.getAsset();
        AssetPart assetPart = assetManager.createTexture(iAsset, 0, 0, 40, 46);
        assetPart.setzIndex(30);
        AbsolutePositionPart positionPart = new AbsolutePositionPart(pixelx, pixely);

        towerEntity.addPart(assetPart);
        towerEntity.addPart(positionPart);

        return towerEntity;
    }

    private void addTowerToList(TowerModel towerModel) {

        towerModels.add(towerModel);

    }

    private final LookupListener iTowerLookupListener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent ev) {

            Collection<? extends ITower> actualTowers = towerImpleLookupResult.allInstances();

            for (ITower tower : actualTowers) {
                // Newly installed modules
                if (!towerImple.contains(tower)) {
                    towerImple.add(tower);
                    Entity entity = addNewTowerToMenu(tower);
                    addTowerToList(new TowerModel(entity, tower));
                    towerConsumer.add(world -> {
                        world.addEntity(entity);
                    });
                }
            }
            // Stop and remove module
            for (ITower tower : towerImple) {
                if (!actualTowers.contains(tower)) {
                    towerImple.remove(tower);
                }
            }
        }

    };

}
