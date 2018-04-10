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
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.ISystem;
import com.kag.interfaces.ITower;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

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
    private IAssetManager assetManager;
    private TowerSelectionManager towerSelectionManager;


    public TowerMasterSystem() {
        towerConsumer = new ArrayList<>();
        towerModels = new ArrayList<>();
        assetManager = Lookup.getDefault().lookup(IAssetManager.class);
    }

    @Override
    public void update(float dt, World world, GameData gameData) {

        if (!towerConsumer.isEmpty()) {
            for (Consumer<World> consumer : towerConsumer) {
                consumer.accept(world);
            }
            towerConsumer.clear();
        }

        //Handle tower selection on buy menu.
        handleMouseInput(world, gameData);

        //If a tower has been selected.
        if (towerSelectionManager.getSelectedTower() != null) {

            if (isMouseOnGameMap(gameData)) {
                towerSelectionManager.createTowerPreviewOverlay();
                towerSelectionManager.updateTowerPreviewOverlayOnMap(world, gameData);
            } else {
                towerSelectionManager.updateTowerPreviewOverlayOnMenu(gameData);
            }

            if (isMouseOnGameMap(gameData) && gameData.getMouse().isButtonPressed(Mouse.BUTTON_LEFT)) {
                towerSelectionManager.placeTowerOnGameMap(world, gameData);
            }
        }

        if (isMouseOnGameMap(gameData)) {
            towerSelectionManager.handleSellTower(world, gameData);
        }
    }

    private boolean isMouseOnGameMap(GameData gameData) {
        return gameData.getMouse().getX() < 768;
    }


    private void handleMouseInput(World world, GameData gameData) {
        if (gameData.getMouse().isButtonPressed(Mouse.BUTTON_LEFT)) {
            handleBuyMenu(gameData, world);

        } else if (gameData.getMouse().isButtonDown(Mouse.BUTTON_RIGHT) || gameData.getKeyboard().isKeyDown(Keyboard.KEY_ESCAPE)) {
            towerSelectionManager.resetTowerSelection(world);
        }
    }

    private void handleBuyMenu(GameData gameData, World world) {
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
                        world.addEntity(towerSelectionManager.createTowerPreview(gameData, towerSelectionManager.getSelectedTower()));
                    }
                }
            }
        }
    }

    @Override
    public int getPriority() {
        return UPDATE_PASS_1;
    }

    @Override
    public void load(World world) {
	    towerSelectionManager = new TowerSelectionManager();
        lookup = Lookup.getDefault();

        towersToBeDrawn = new ArrayList<>();

        towerImple = new CopyOnWriteArrayList<>();
        towerImpleLookupResult = lookup.lookupResult(ITower.class);
        towerImpleLookupResult.addLookupListener(iTowerLookupListener);

        lookup.lookupAll(ITower.class).forEach((e) -> {
            Entity entity = addNewTowerToMenu(e);
            world.addEntity(entity);
            addTowerToList(new TowerModel(entity, e));
        });

        LabelPart btnDescription = new LabelPart("",14);
        btnDescription.setzIndex(ZIndex.GUI_SELLLABEL);
        sellTowerLabel = new Entity();
        sellTowerLabel.addPart(btnDescription);
        sellTowerLabel.addPart(new AbsolutePositionPart(653, 590));

        AssetPart sellBtnImg = assetManager.createTexture(getClass().getResourceAsStream("/WoodSign.png"));
        sellBtnImg.setzIndex(ZIndex.GUI_BUTTON);
        sellTowerButton = new Entity();
        sellTowerButton.addPart(sellBtnImg);
        sellTowerButton.addPart(new AbsolutePositionPart(630, 552));
        sellTowerButton.addPart(new BoundingBoxPart(130,80));

        AssetPart towerPanel = assetManager.createTexture(getClass().getResourceAsStream("/TowerPanel.png"));
        towerPanel.setzIndex(ZIndex.GUI_PANELS);
        towerMenuBackground = new Entity();
        towerMenuBackground.addPart(towerPanel);
        towerMenuBackground.addPart(new AbsolutePositionPart(768, 128));

        AssetPart upgradePanel = assetManager.createTexture(getClass().getResourceAsStream("/TowerPanel.png"));
        upgradePanel.setzIndex(ZIndex.GUI_PANELS);
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
        for(TowerModel model : towerModels){
            world.removeEntity(model.getTowerEntity());
        }
        towerSelectionManager.dispose(world);
    }


    private Entity addNewTowerToMenu(ITower tower) {
        //Create Entity from tower and return
        Entity towerEntity = new Entity();
        towersToBeDrawn.add(towerEntity);

        int index = towersToBeDrawn.indexOf(towerEntity);

        int menuX = index % 3;
        int menuY = index / 3;

        int menuStartX = 788 + menuX * 52;
        int menuStartY = 154 + menuY * 52;

        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

        IAsset iAsset = tower.getAsset();
	    AssetPart assetPart = assetManager.createTexture(iAsset, 0, 0, iAsset.getWidth(), iAsset.getHeight());
        float aspectRatio = (float)iAsset.getWidth() / iAsset.getHeight();
        if(iAsset.getWidth() > iAsset.getHeight()) {
        	assetPart.setWidth(48);
        	assetPart.setHeight((int)(48 / aspectRatio));
        } else {
	        assetPart.setHeight(48);
	        assetPart.setWidth((int)(48 * aspectRatio));
        }
        int dx = (48 - assetPart.getWidth()) / 2;
        int dy = (48 - assetPart.getHeight()) / 2;
        menuStartX += dx;
        menuStartY += dy;
        assetPart.setzIndex(ZIndex.TOWER_BASE);
        AbsolutePositionPart positionPart = new AbsolutePositionPart(menuStartX, menuStartY);

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
