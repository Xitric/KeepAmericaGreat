package com.kag.towercontroller;

import com.kag.common.data.GameData;
import com.kag.common.data.IAsset;
import com.kag.common.data.Tile;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.spinterfaces.IAssetManager;
import org.openide.util.Lookup;

public class TowerSelectionManager {
    private TowerModel selectedTower;
    private IAssetManager assetManager;
    private AssetPart overlayAssetPart = null;
    private Entity mouseTower;

    public TowerSelectionManager() {
        assetManager = Lookup.getDefault().lookup(IAssetManager.class);
    }

    public Entity getMouseTower() {
        return mouseTower;
    }

    public void setMouseTower(Entity mouseTower) {
        this.mouseTower = mouseTower;
    }

    public TowerModel getSelectedTower() {
        return selectedTower;
    }

    public void setSelectedTower(TowerModel selectedTower) {
        this.selectedTower = selectedTower;
    }

    public Entity createMouseTower(GameData gameData, TowerModel selectedTower) {
        mouseTower = new Entity();

        PositionPart positionPart = new AbsolutePositionPart(gameData.getMouse().getX() - 32, gameData.getMouse().getY() - 32);

        IAsset iAsset = selectedTower.getITower().getAsset();
        AssetPart assetPart = assetManager.createTexture(iAsset, 0, 0, 40, 46);

        assetPart.setxOffset((64 - assetPart.getWidth()) / 2);
        assetPart.setyOffset((64 - assetPart.getHeight()) / 2);

        assetPart.setzIndex(29);

        mouseTower.addPart(positionPart);
        mouseTower.addPart(assetPart);

        return mouseTower;
    }

    public void createOverlay() {
        if (overlayAssetPart == null) {
            overlayAssetPart = assetManager.createTexture(getClass().getResourceAsStream("/RedOverlay.png"));
            overlayAssetPart.setzIndex(30);
            getMouseTower().addPart(overlayAssetPart);
        }
    }

    public void updateOverlay(World world, GameData gameData){
        float xTilePositionOnMap = (gameData.getCamera().getX() - gameData.getWidth() / 2 + gameData.getMouse().getX()) / 64;
        float yTilePositionOnMap = (gameData.getCamera().getY() - gameData.getHeight() / 2 + gameData.getMouse().getY()) / 64;

        Tile hoverTile = world.getGameMap().getTile((int) xTilePositionOnMap, (int) yTilePositionOnMap);
        getMouseTower().getPart(AbsolutePositionPart.class).setPos(hoverTile.getX() * 64 - gameData.getCamera().getX() + (gameData.getWidth()/2), hoverTile.getY() * 64 - gameData.getCamera().getY() + (gameData.getHeight()/2));

        if (!world.isOccupied(hoverTile.getX(), hoverTile.getY())) {
            getMouseTower().removePart(overlayAssetPart);
            overlayAssetPart = assetManager.createTexture(getClass().getResourceAsStream("/BlueOverlay.png"));
            overlayAssetPart.setzIndex(30);
            getMouseTower().addPart(overlayAssetPart);

        } else {
            getMouseTower().removePart(overlayAssetPart);
            overlayAssetPart = assetManager.createTexture(getClass().getResourceAsStream("/RedOverlay.png"));
            overlayAssetPart.setzIndex(30);
            getMouseTower().addPart(overlayAssetPart);
        }
    }

    public void placeTowerOnField(World world, GameData gameData) {
        float xTilePositionOnMap = (gameData.getCamera().getX() - gameData.getWidth() / 2 + gameData.getMouse().getX()) / 64;
        float yTilePositionOnMap = (gameData.getCamera().getY() - gameData.getHeight() / 2 + gameData.getMouse().getY()) / 64;

        Tile hoverTile = world.getGameMap().getTile((int) xTilePositionOnMap, (int) yTilePositionOnMap);

        //If the tile is occupied the tile should turn red to indicate that the player is not allowed to place a tower
        if (!world.isOccupied(hoverTile.getX(), hoverTile.getY())) {
            Entity newTower = getSelectedTower().getITower().create();
            newTower.getPart(PositionPart.class).setPos(hoverTile.getX() * 64, hoverTile.getY() * 64);

            world.removeEntity(getMouseTower());
            setMouseTower(null);
            setSelectedTower(null);
            world.addEntity(newTower);
            hoverTile.setWalkable(false);
        }
    }
}
