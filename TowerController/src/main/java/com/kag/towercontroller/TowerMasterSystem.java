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
    private boolean towerSelected = false;
    private Entity tempHoverTower = null;
    private TowerModel towerAtPosition = null;
    private IAssetManager assetManager = null;
    private AssetPart overlayAssetPart = null;


    public TowerMasterSystem() {
        towerConsumer = new ArrayList<>();
        towerModels = new ArrayList<>();
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

        if (gameData.getMouse().isButtonPressed(Mouse.BUTTON_LEFT) && !towerSelected) {
            towerAtPosition = getTowerAtMousePosition(gameData.getMouse().getX(), gameData.getMouse().getY());

            if (towerAtPosition != null) {
                towerSelected = true;
            }


        } else if (gameData.getMouse().isButtonDown(Mouse.BUTTON_RIGHT) || gameData.getKeyboard().isKeyDown(Keyboard.KEY_ESCAPE)) {
            world.removeEntity(tempHoverTower);
            towerSelected = false;
        }

        if (towerSelected && towerAtPosition != null) {

            //The mouse starts in the left upper corner whereas the camera starts in the middle. We therefore move the camera
            // to start in the same position as the mouse, thereby ensuring that the correct tile is registered.


            if (tempHoverTower == null) {
                tempHoverTower = new Entity();

                PositionPart positionPart = new AbsolutePositionPart(gameData.getMouse().getX() - 32, gameData.getMouse().getY() - 32);


                IAsset iAsset = towerAtPosition.getiTower().getAsset();
                AssetPart assetPart = assetManager.createTexture(iAsset, 0, 0, 40, 46);

                assetPart.setxOffset((64 - assetPart.getWidth()) / 2);
                assetPart.setyOffset((64 - assetPart.getHeight()) / 2);

                assetPart.setzIndex(29);

//                //Add red or blue to the entity
//                AssetPart red = assetManager.createTexture(getClass().getResourceAsStream("/RedOverlay.png"));
//                red.setzIndex(30);
//
//                tempHoverTower.addPart(red);
                tempHoverTower.addPart(positionPart);
                tempHoverTower.addPart(assetPart);

                world.addEntity(tempHoverTower);
            }

            if (gameData.getMouse().getX() < 768) {
                if (overlayAssetPart == null) {
                    overlayAssetPart = assetManager.createTexture(getClass().getResourceAsStream("/RedOverlay.png"));
                    overlayAssetPart.setzIndex(30);
                    tempHoverTower.addPart(overlayAssetPart);
                }

                float xTilePositionOnMap = (gameData.getCamera().getX() - gameData.getWidth() / 2 + gameData.getMouse().getX()) / 64;
                float yTolePositionOnMap = (gameData.getCamera().getY() - gameData.getHeight() / 2 + gameData.getMouse().getY()) / 64;
                Tile hoverTile = world.getGameMap().getTile((int) xTilePositionOnMap, (int) yTolePositionOnMap);

                tempHoverTower.getPart(AbsolutePositionPart.class).setPos(hoverTile.getX()*64, hoverTile.getY()*64);


                if (!world.isOccupied(hoverTile.getX(), hoverTile.getY())) {
                    tempHoverTower.removePart(overlayAssetPart);
                    overlayAssetPart = assetManager.createTexture(getClass().getResourceAsStream("/BlueOverlay.png"));
                    overlayAssetPart.setzIndex(30);
                    tempHoverTower.addPart(overlayAssetPart);
                    System.out.println("Ledig!");

                } else {
                    tempHoverTower.removePart(overlayAssetPart);
                    overlayAssetPart = assetManager.createTexture(getClass().getResourceAsStream("/RedOverlay.png"));
                    overlayAssetPart.setzIndex(30);
                    tempHoverTower.addPart(overlayAssetPart);
                    System.out.println("Optaget!");
                }


            }

            if (gameData.getMouse().getX() < 768 && gameData.getMouse().isButtonPressed(Mouse.BUTTON_LEFT)) {
                float xPositionMap = (gameData.getCamera().getX() - gameData.getWidth() / 2 + gameData.getMouse().getX()) / 64;
                float yPositionMap = (gameData.getCamera().getY() - gameData.getHeight() / 2 + gameData.getMouse().getY()) / 64;
                Tile hoverTile = world.getGameMap().getTile((int) xPositionMap, (int) yPositionMap);
                System.out.println("Nu er vi på mappet");

                //If the tile is occupied the tile should turn red to indicate that the player is not allowed to place a tower
                if (!world.isOccupied(hoverTile.getX(), hoverTile.getY())) {
                    System.out.println("Nu sætter vi et tårn");
                    Entity newTower = towerAtPosition.getiTower().create();
                    newTower.getPart(PositionPart.class).setPos(hoverTile.getX() * 64, hoverTile.getY() * 64);
                    world.removeEntity(tempHoverTower);
                    tempHoverTower = null;
                    world.addEntity(newTower);
                    towerSelected = false;

                }
            }

        }
    }

    private TowerModel getTowerAtMousePosition(int mouseX, int mouseY) {

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
                        return model;
                    }
                }
            }
        }
        return null;
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
