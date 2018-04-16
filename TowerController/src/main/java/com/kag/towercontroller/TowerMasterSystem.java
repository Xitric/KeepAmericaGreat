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
import com.kag.common.entities.parts.gui.LabelPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.ISystem;
import com.kag.tdcommon.entities.parts.MoneyPart;
import com.kag.tdcommon.spinterfaces.ITower;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@ServiceProviders(value = {
		@ServiceProvider(service = ISystem.class),
		@ServiceProvider(service = IComponentLoader.class)
})
public class TowerMasterSystem implements ISystem, IComponentLoader {

	private Entity towerMenuBackground;
	private Entity upgradeMenuBackground;
	private List<Consumer<World>> towerConsumer;
	private List<TowerModel> towerModels;
	private ServiceManager<ITower> towerServiceManager;
	private IAssetManager assetManager;
	private TowerSelectionManager towerSelectionManager;
	private boolean updateBuyMenu;

	public TowerMasterSystem() {
		towerConsumer = new ArrayList<>();
		towerModels = new ArrayList<>();
		assetManager = Lookup.getDefault().lookup(IAssetManager.class);
	}

	@Override
	public void load(World world) {
		towerSelectionManager = new TowerSelectionManager();

		towerServiceManager = new ServiceManager<>(ITower.class, this::onTowersChanged, this::onTowersChanged);

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
		for (TowerModel model : towerModels) {
			world.removeEntity(model.getTowerEntity());
		}
		towerSelectionManager.dispose(world);
	}

	@Override
	public void update(float dt, World world, GameData gameData) {

		if (!towerConsumer.isEmpty()) {
			for (Consumer<World> consumer : towerConsumer) {
				consumer.accept(world);
			}
			towerConsumer.clear();
		}

		if (updateBuyMenu) {
			Collection<? extends ITower> tower = towerServiceManager.getServiceProviders();
			for (ITower iTower : tower) {
				Entity entity = addNewTowerToMenu(iTower);
				LabelPart priceLabel = new LabelPart(String.valueOf(iTower.create().getPart(MoneyPart.class).getMoney()));
				priceLabel.setzIndex(ZIndex.TOWER_TURRET);
				entity.addPart(priceLabel);
				world.addEntity(entity);
			}
			towerSelectionManager.resetTowerSelection(world);
			updateBuyMenu = false;
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
		for (TowerModel towerModel : towerModels) {
			Entity tower = towerModel.getTowerEntity();
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

	private Entity addNewTowerToMenu(ITower tower) {
		//Create Entity from tower and return
		Entity towerEntity = new Entity();
		towerModels.add(new TowerModel(towerEntity, tower));

		int index = towerModels.size() - 1;
		System.out.println("Index of new tower added to menu: " + index);

		int menuX = index % 3;
		int menuY = index / 3;

		int menuStartX = 788 + menuX * 52;
		int menuStartY = 154 + menuY * 52;

		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

		IAsset iAsset = tower.getAsset();
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
		assetPart.setzIndex(ZIndex.TOWER_BASE);
		AbsolutePositionPart positionPart = new AbsolutePositionPart(menuStartX, menuStartY);

		towerEntity.addPart(assetPart);
		towerEntity.addPart(positionPart);

		return towerEntity;
	}

	private void removeTowerPreviews(World world) {
		for (TowerModel towerModel : towerModels) {
			world.removeEntity(towerModel.getTowerEntity());
		}
		towerModels.clear();
		updateBuyMenu = true;
	}

	private void onTowersChanged(ITower tower) {
		towerConsumer.add(this::removeTowerPreviews);
	}
}
