package com.kag.towercontroller;

import com.kag.common.data.*;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.*;
import com.kag.common.input.Keyboard;
import com.kag.common.input.Mouse;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.ISystem;
import com.kag.commonasset.Color;
import com.kag.commonasset.ZIndex;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.entities.parts.CirclePart;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commontower.entities.parts.TowerPart;
import com.kag.commontower.entities.parts.WeaponPart;
import com.kag.commontower.spinterfaces.ITower;
import com.kag.commontower.spinterfaces.ITowerService;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
		@ServiceProvider(service = IComponentLoader.class),
		@ServiceProvider(service = ISystem.class),
})
public class TowerSelectionManager implements IComponentLoader, ISystem {

	private static final Family BUY_MENU_ELEMENT_FAMILY = Family.forAll(TowerBuyMenuPart.class, AbsolutePositionPart.class, BoundingBoxPart.class);
	private static final Family TOWER_FAMILY = Family.forAll(TowerPart.class, PositionPart.class, BoundingBoxPart.class);

	private Entity towerPreview;
	private AssetPart redOverlay;
	private AssetPart whiteOverlay;
	private TowerBuilder towerBuilder;

	@Override
	public void load(World world) {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		redOverlay = assetManager.createTexture(getClass().getResourceAsStream("/RedOverlay.png"));
		redOverlay.setzIndex(ZIndex.TOWER_OVERLAY);
		whiteOverlay = assetManager.createTexture(getClass().getResourceAsStream("/WhiteOverlay.png"));
		whiteOverlay.setzIndex(ZIndex.TOWER_OVERLAY);

		towerBuilder = new TowerBuilder();
	}

	@Override
	public void dispose(World world) {
		redOverlay.dispose();
		whiteOverlay.dispose();
	}

	@Override
	public void update(float dt, World world, GameData gameData) {
		doMenuSelect(world, gameData);

		if (isHoldingTower()) {
			updateTowerPreview(world, gameData);

			if (gameData.getMouse().isButtonPressed(Mouse.BUTTON_LEFT)) {
				if (towerBuilder.tryPlaceTower(towerPreview, world, gameData) && !gameData.getKeyboard().isKeyDown(Keyboard.KEY_LSHIFT)) {
					world.removeEntity(towerPreview);
					towerPreview = null;
				}
			}
		} else {
			if (gameData.getMouse().isButtonPressed(Mouse.BUTTON_LEFT)) {
				doTowerSelect(world, gameData);
			}
		}
	}

	private void doMenuSelect(World world, GameData gameData) {
		//Deselect tower
		if (gameData.getMouse().isButtonPressed(Mouse.BUTTON_RIGHT) || gameData.getKeyboard().isKeyPressed(Keyboard.KEY_ESCAPE)) {
			world.removeEntity(towerPreview);
			towerPreview = null;
			return;
		}

		//Check for new selection
		if (!gameData.getMouse().isButtonPressed(Mouse.BUTTON_LEFT)) return;

		for (Entity buyElement : world.getEntitiesByFamily(BUY_MENU_ELEMENT_FAMILY)) {
			if (world.isEntityAt(buyElement, gameData.getMouse().getX(), gameData.getMouse().getY())) {
				ITower factory = buyElement.getPart(TowerBuyMenuPart.class).getTowerFactory();

				if (isHoldingTower()) world.removeEntity(towerPreview);
				towerPreview = createTowerPreview(factory, world.getGameMap().getTileWidth(), world.getGameMap().getTileHeight());
				world.addEntity(towerPreview);
			}
		}
	}

	private Entity createTowerPreview(ITower factory, int width, int height) {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		Entity preview = new Entity();

		AbsolutePositionPart positionPart = new AbsolutePositionPart(0, 0);
		TowerBuyMenuPart buyMenuPart = new TowerBuyMenuPart(factory);

		//Tower icon
		IAsset icon = factory.getAsset();
		AssetPart iconPart = assetManager.createTexture(icon, 0, 0, icon.getWidth(), icon.getHeight());
		iconPart.setxOffset((width - iconPart.getWidth()) / 2);
		iconPart.setyOffset((height - iconPart.getHeight()) / 2);
		iconPart.setzIndex(ZIndex.TOWER_PREVIEW);

		preview.addPart(positionPart);
		preview.addPart(buyMenuPart);
		preview.addPart(iconPart);

		//Tower range
		WeaponPart weaponPart = factory.create().getPart(WeaponPart.class);
		if (weaponPart != null) {
			CirclePart rangeCircle = new CirclePart(weaponPart.getRange(), new Color(0x55ADD8E6));
			rangeCircle.setxOffset(width / 2);
			rangeCircle.setyOffset(height / 2);
			rangeCircle.setzIndex(ZIndex.TOWER_RANGE_PREVIEW);
			preview.addPart(rangeCircle);
		}

		return preview;
	}

	private void updateTowerPreview(World world, GameData gameData) {
		AbsolutePositionPart positionPart = towerPreview.getPart(AbsolutePositionPart.class);
		towerPreview.removePart(whiteOverlay);
		towerPreview.removePart(redOverlay);

		if (isMouseOnGameMap(gameData.getMouse())) {
			//Snap preview position to tiles
			int xTilePositionOnMap = (int) ((gameData.getCamera().getX() - gameData.getWidth() / 2 + gameData.getMouse().getX()) / world.getGameMap().getTileWidth());
			int yTilePositionOnMap = (int) ((gameData.getCamera().getY() - gameData.getHeight() / 2 + gameData.getMouse().getY()) / world.getGameMap().getTileHeight());

			int pixelX = (int) (xTilePositionOnMap * world.getGameMap().getTileWidth() - gameData.getCamera().getX() + (gameData.getWidth() / 2));
			int pixelY = (int) (yTilePositionOnMap * world.getGameMap().getTileHeight() - gameData.getCamera().getY() + (gameData.getHeight() / 2));

			positionPart.setPos(pixelX, pixelY);

			//Update overlay
			if (!world.isOccupied(xTilePositionOnMap, yTilePositionOnMap)) {
				towerPreview.addPart(whiteOverlay);
			} else {
				towerPreview.addPart(redOverlay);
			}
		} else {
			//Center icon on mouse
			positionPart.setPos(gameData.getMouse().getX() - world.getGameMap().getTileWidth() / 2, gameData.getMouse().getY() - world.getGameMap().getTileHeight() / 2);
		}
	}

	private void doTowerSelect(World world, GameData gameData) {
		int worldX = (int) (gameData.getCamera().getX() - gameData.getWidth() / 2 + gameData.getMouse().getX());
		int worldY = (int) (gameData.getCamera().getY() - gameData.getHeight() / 2 + gameData.getMouse().getY());

		for (Entity tower : world.getEntitiesByFamily(TOWER_FAMILY)) {
			if (world.isEntityAt(tower, worldX, worldY)) {
				Lookup.getDefault().lookup(ITowerService.class).towerSelected(tower);
				return;
			}
		}

		Lookup.getDefault().lookup(ITowerService.class).towerSelected(null);
	}

	private boolean isMouseOnGameMap(Mouse mouse) {
		return mouse.getX() < 768;
	}

	private boolean isHoldingTower() {
		return towerPreview != null;
	}

	@Override
	public int getPriority() {
		return UPDATE_PASS_2;
	}
}
