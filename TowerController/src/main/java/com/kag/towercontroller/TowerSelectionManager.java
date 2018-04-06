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
    private AssetPart towerPreviewOverlayAssetPart = null;
    private Entity previewTower;

    public TowerSelectionManager() {
        assetManager = Lookup.getDefault().lookup(IAssetManager.class);
    }

    public Entity createTowerPreview(GameData gameData, TowerModel selectedTower) {
        previewTower = new Entity();

        PositionPart positionPart = new AbsolutePositionPart(gameData.getMouse().getX() - 32, gameData.getMouse().getY() - 32);

        IAsset iAsset = selectedTower.getITower().getAsset();
        AssetPart assetPart = assetManager.createTexture(iAsset, 0, 0, 40, 46);

        assetPart.setxOffset((64 - assetPart.getWidth()) / 2);
        assetPart.setyOffset((64 - assetPart.getHeight()) / 2);

        assetPart.setzIndex(29);

        previewTower.addPart(positionPart);
        previewTower.addPart(assetPart);

        return previewTower;
    }

    public void createTowerPreviewOverlay() {
        if (towerPreviewOverlayAssetPart == null) {
            towerPreviewOverlayAssetPart = assetManager.createTexture(getClass().getResourceAsStream("/RedOverlay.png"));
            towerPreviewOverlayAssetPart.setzIndex(30);
            getPreviewTower().addPart(towerPreviewOverlayAssetPart);
        }
    }

    public void updateTowerPreviewOverlay(World world, GameData gameData){
        float xTilePositionOnMap = (gameData.getCamera().getX() - gameData.getWidth() / 2 + gameData.getMouse().getX()) / 64;
        float yTilePositionOnMap = (gameData.getCamera().getY() - gameData.getHeight() / 2 + gameData.getMouse().getY()) / 64;

        Tile hoverTile = world.getGameMap().getTile((int) xTilePositionOnMap, (int) yTilePositionOnMap);
        getPreviewTower().getPart(AbsolutePositionPart.class).setPos(hoverTile.getX() * 64 - gameData.getCamera().getX() + (gameData.getWidth()/2), hoverTile.getY() * 64 - gameData.getCamera().getY() + (gameData.getHeight()/2));

        if (!world.isOccupied(hoverTile.getX(), hoverTile.getY())) {
            getPreviewTower().removePart(towerPreviewOverlayAssetPart);
            towerPreviewOverlayAssetPart = assetManager.createTexture(getClass().getResourceAsStream("/BlueOverlay.png"));
            towerPreviewOverlayAssetPart.setzIndex(30);
            getPreviewTower().addPart(towerPreviewOverlayAssetPart);

        } else {
            getPreviewTower().removePart(towerPreviewOverlayAssetPart);
            towerPreviewOverlayAssetPart = assetManager.createTexture(getClass().getResourceAsStream("/RedOverlay.png"));
            towerPreviewOverlayAssetPart.setzIndex(30);
            getPreviewTower().addPart(towerPreviewOverlayAssetPart);
        }
    }

    public void placeTowerOnGameMap(World world, GameData gameData) {
        float xTilePositionOnMap = (gameData.getCamera().getX() - gameData.getWidth() / 2 + gameData.getMouse().getX()) / 64;
        float yTilePositionOnMap = (gameData.getCamera().getY() - gameData.getHeight() / 2 + gameData.getMouse().getY()) / 64;

        Tile hoverTile = world.getGameMap().getTile((int) xTilePositionOnMap, (int) yTilePositionOnMap);

        //If the tile is occupied the tile should turn red to indicate that the player is not allowed to place a tower
        if (!world.isOccupied(hoverTile.getX(), hoverTile.getY())) {
            Entity newTower = getSelectedTower().getITower().create();
            newTower.getPart(PositionPart.class).setPos(hoverTile.getX() * 64, hoverTile.getY() * 64);

            resetTowerSelection(world);
            world.addEntity(newTower);
            hoverTile.setWalkable(false);
        }
    }

    public void resetTowerSelection(World world) {
        world.removeEntity(getPreviewTower());
        setPreviewTower(null);
        setSelectedTower(null);
    }

    public Entity getPreviewTower() {
        return previewTower;
    }

    public void setPreviewTower(Entity mouseTower) {
        this.previewTower = mouseTower;
    }

    public TowerModel getSelectedTower() {
        return selectedTower;
    }

    public void setSelectedTower(TowerModel selectedTower) {
        this.selectedTower = selectedTower;
    }
}
