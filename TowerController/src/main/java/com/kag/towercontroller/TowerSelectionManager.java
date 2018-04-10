package com.kag.towercontroller;

import com.kag.common.data.*;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.*;
import com.kag.common.entities.parts.gui.LabelPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IPathFinder;
import com.kag.towerparts.CostPart;
import com.kag.towerparts.TowerPart;
import org.openide.util.Lookup;

public class TowerSelectionManager {
	private static final Family PLAYER_FAMILY = Family.forAll(CurrencyPart.class, LifePart.class, PositionPart.class, BoundingBoxPart.class).excluding(BlockingPart.class);
	private static final Family towerFamily = Family.forAll(TowerPart.class);

	private TowerModel selectedTower;
	private IAssetManager assetManager;
	private AssetPart towerPreviewOverlayAssetPart = null;
	private Entity previewTower;
	private AssetPart redOverlay;
	private AssetPart blueOverlay;
	private Entity tempTower;
	private float xTilePositionOnMap;
	private float yTilePositionOnMap;
	private Entity sellTowerButton;
	private Entity sellTowerLabel;
	private int sellingPrice;

	public TowerSelectionManager() {
		assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		redOverlay = assetManager.createTexture(getClass().getResourceAsStream("/RedOverlay.png"));
		redOverlay.setzIndex(ZIndex.TOWER_OVERLAY);
		blueOverlay = assetManager.createTexture(getClass().getResourceAsStream("/WhiteOverlay.png"));
		blueOverlay.setzIndex(ZIndex.TOWER_OVERLAY);

		LabelPart btnDescription = new LabelPart("",14);
		btnDescription.setzIndex(ZIndex.GUI_CURRENCY_ICON);
		sellTowerLabel = new Entity();
		sellTowerLabel.addPart(btnDescription);
		sellTowerLabel.addPart(new AbsolutePositionPart(653, 590));

		AssetPart sellBtnImg = assetManager.createTexture(getClass().getResourceAsStream("/WoodSign.png"));
		sellBtnImg.setzIndex(ZIndex.GUI_SELLLABEL);
		sellTowerButton = new Entity();
		sellTowerButton.addPart(sellBtnImg);
		sellTowerButton.addPart(new AbsolutePositionPart(630, 552));
		sellTowerButton.addPart(new BoundingBoxPart(130,80));
	}

	public Entity createTowerPreview(GameData gameData, TowerModel selectedTower) {
		previewTower = new Entity();

		PositionPart positionPart = new AbsolutePositionPart(0, 0);

		IAsset iAsset = selectedTower.getITower().getAsset();
		AssetPart assetPart = assetManager.createTexture(iAsset, 0, 0, iAsset.getWidth(), iAsset.getHeight());

		assetPart.setxOffset((64 - assetPart.getWidth()) / 2);
		assetPart.setyOffset((64 - assetPart.getHeight()) / 2);

		assetPart.setzIndex(ZIndex.TOWER_PREVIEW);

		previewTower.addPart(positionPart);
		previewTower.addPart(assetPart);

		return previewTower;
	}

	public void createTowerPreviewOverlay() {
		if (towerPreviewOverlayAssetPart == null) {
			towerPreviewOverlayAssetPart = redOverlay;
			getPreviewTower().addPart(towerPreviewOverlayAssetPart);
		}
	}

	public void updateTowerPreviewOverlayOnMap(World world, GameData gameData) {
		float xTilePositionOnMap = (gameData.getCamera().getX() - gameData.getWidth() / 2 + gameData.getMouse().getX()) / 64;
		float yTilePositionOnMap = (gameData.getCamera().getY() - gameData.getHeight() / 2 + gameData.getMouse().getY()) / 64;

		Tile hoverTile = world.getGameMap().getTile((int) xTilePositionOnMap, (int) yTilePositionOnMap);
		getPreviewTower().getPart(AbsolutePositionPart.class).setPos(hoverTile.getX() * 64 - gameData.getCamera().getX() + (gameData.getWidth() / 2), hoverTile.getY() * 64 - gameData.getCamera().getY() + (gameData.getHeight() / 2));

		if (!world.isOccupied(hoverTile.getX(), hoverTile.getY())) {
			getPreviewTower().removePart(towerPreviewOverlayAssetPart);
			towerPreviewOverlayAssetPart = blueOverlay;
			getPreviewTower().addPart(towerPreviewOverlayAssetPart);

		} else {
			getPreviewTower().removePart(towerPreviewOverlayAssetPart);
			towerPreviewOverlayAssetPart = redOverlay;
			getPreviewTower().addPart(towerPreviewOverlayAssetPart);
		}
	}

	public void updateTowerPreviewOverlayOnMenu(GameData gameData) {
		getPreviewTower().getPart(AbsolutePositionPart.class).setPos(gameData.getMouse().getX() - 32, gameData.getMouse().getY() - 32);

		if (towerPreviewOverlayAssetPart != null) {
			getPreviewTower().removePart(towerPreviewOverlayAssetPart);
		}
	}

	private Node[][] getPaths(World world) {
		IPathFinder pathFinder = Lookup.getDefault().lookup(IPathFinder.class);

		return pathFinder.getPath(world.getGameMap().getPlayerX(), world.getGameMap().getPlayerY(), world);
	}

	private int countValidPaths(Node[][] paths) {
		int count = 0;

		for (int i = 0; i < paths.length; i++) {
			for (int j = 0; j < paths[i].length; j++) {
				if (paths[i][j] != null) {
					count++;
				}
			}
		}

		return count;
	}

	private boolean canAffordTower(Entity player, Entity tower) {
		int playerMoney = player.getPart(CurrencyPart.class).getCurrencyAmount();
		int cost = tower.getPart(CostPart.class).getCost();

		return cost <= playerMoney;
	}

	private void buyTower(Entity player, Entity tower) {
		CurrencyPart playerCurrencyPart = player.getPart(CurrencyPart.class);

		int playerMoney = playerCurrencyPart.getCurrencyAmount();
		int cost = tower.getPart(CostPart.class).getCost();

		playerCurrencyPart.setCurrencyAmount(playerMoney - cost);
	}

	public void placeTowerOnGameMap(World world, GameData gameData) {
		float xTilePositionOnMap = (gameData.getCamera().getX() - gameData.getWidth() / 2 + gameData.getMouse().getX()) / 64;
		float yTilePositionOnMap = (gameData.getCamera().getY() - gameData.getHeight() / 2 + gameData.getMouse().getY()) / 64;

		Tile hoverTile = world.getGameMap().getTile((int) xTilePositionOnMap, (int) yTilePositionOnMap);

		//If the tile is occupied the tile should turn red to indicate that the player is not allowed to place a tower
		if (!world.isOccupied(hoverTile.getX(), hoverTile.getY())) {
			Entity newTower = getSelectedTower().getITower().create();

			if(!gameData.getKeyboard().isKeyDown(Keyboard.KEY_LSHIFT)){
				resetTowerSelection(world);
			}

			Entity trumpTower = world.getEntitiesByFamily(PLAYER_FAMILY).stream().findFirst().orElse(null);

			// Check cost and can afford
			if (!canAffordTower(trumpTower, newTower)) {
				System.out.println("Cant afford");
				return;
			}

			buyTower(trumpTower, newTower);

			newTower.getPart(PositionPart.class).setPos(hoverTile.getX() * world.getGameMap().getTileWidth() + world.getGameMap().getTileWidth() / 2,
					hoverTile.getY() * world.getGameMap().getTileHeight() + world.getGameMap().getTileHeight() / 2);

			// Running Dijkstra pf to get a count of nodes available for start pos, to know how many start positions are removed after tower placement
			Node[][] validPaths = getPaths(world);
			int validPathsCount = countValidPaths(validPaths);

			world.addEntity(newTower);
			hoverTile.setWalkable(false);

			if (!isMapValid(world, validPathsCount, validPaths)) {
				world.removeEntity(newTower);
				hoverTile.setWalkable(true);
			}
		}
	}

	private boolean isMapValid(World world, int prevValidPathsCount, Node[][] prevValidPaths) {
    	/*
    	Run Dijkstra.
    	If more than one possible path to the goal is removed, an area is blocked off.
    	Validate that there are no enemies by checking the blocked off tiles for whether they are walkable and occupied,
    	if that's the case, there are enemies on the blocked off tiles and the map is invalid.
    	 */
		IPathFinder pathFinder = Lookup.getDefault().lookup(IPathFinder.class);

		// Check whether there's a valid path from spawn to goal (the player)
		if (pathFinder.getPath(0, 0, world.getGameMap().getPlayerX(), world.getGameMap().getPlayerY(), world) == null) {
			return false;
		}

		Node[][] validPaths = getPaths(world);

		// Check whether more than one valid path was removed
		if (prevValidPathsCount - countValidPaths(validPaths) > 1) {
			for (int y = 0; y < validPaths.length; y++) {
				for (int x = 0; x < validPaths[y].length; x++) {
					if (prevValidPaths[y][x] != null && validPaths[y][x] == null && world.isWalkable(x, y) && world.isOccupied(x, y)) {
						return false;
					}
				}
			}
		}

		return true;
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

	public void handleSellTower(World world, GameData gameData) {
		if (gameData.getMouse().isButtonPressed(Mouse.BUTTON_LEFT)){
			if(isSellBtnPressed(world, gameData, sellTowerButton) && world.getAllEntities().contains(sellTowerButton)){
				world.removeEntity(tempTower);
				world.removeEntity(sellTowerLabel);
				world.removeEntity(sellTowerButton);
				//Give player sellingPrice currencies
				world.getEntitiesByFamily(PLAYER_FAMILY).stream().findFirst().ifPresent(trumpTower ->
						trumpTower.getPart(CurrencyPart.class).setCurrencyAmount(trumpTower.getPart(CurrencyPart.class).getCurrencyAmount() + sellingPrice));

				sellingPrice = 0;

				Tile hoverTile = world.getGameMap().getTile((int) xTilePositionOnMap, (int) yTilePositionOnMap);
				hoverTile.setWalkable(true);
			}

			Entity tower = getMouseSelectedTower(gameData, world);
			if (tower != null && towerFamily.matches(tower.getBits())){
				xTilePositionOnMap = (gameData.getCamera().getX() - gameData.getWidth() / 2 + gameData.getMouse().getX()) / 64;
				yTilePositionOnMap = (gameData.getCamera().getY() - gameData.getHeight() / 2 + gameData.getMouse().getY()) / 64;
				//Save a reference to the tower, so the tower can be removed
				tempTower = tower;
				sellingPrice = (int) (tower.getPart(CostPart.class).getCost()*0.75);
				sellTowerLabel.getPart(LabelPart.class).setLabel("Sell for\n" + sellingPrice);
				world.addEntity(sellTowerButton);
				world.addEntity(sellTowerLabel);
			} else {
				world.removeEntity(sellTowerLabel);
				world.removeEntity(sellTowerButton);
			}
		}
	}

	private Entity getMouseSelectedTower (GameData gameData, World world){
		float xTilePositionOnMap = (gameData.getCamera().getX() - gameData.getWidth() / 2 + gameData.getMouse().getX());
		float yTilePositionOnMap = (gameData.getCamera().getY() - gameData.getHeight() / 2 + gameData.getMouse().getY());
		Entity entity = world.getEntityAt(xTilePositionOnMap,yTilePositionOnMap);
		if(entity == null){
			System.out.println("no entity");
			return null;
		}
		if(towerFamily.matches(entity.getBits())){
			System.out.println("found a tower");
			return entity;
		} else {
			System.out.println("found a non-tower");
		}
		return null;
	}

	private boolean isSellBtnPressed(World world, GameData gameData, Entity sellTowerButton) {
		return world.isEntityLeftPressed(gameData, sellTowerButton);
	}

	public void dispose(World world) {
		redOverlay.dispose();
		blueOverlay.dispose();
		world.removeEntity(sellTowerButton);
		world.removeEntity(sellTowerLabel);
		world.removeEntity(tempTower);
	}
}
