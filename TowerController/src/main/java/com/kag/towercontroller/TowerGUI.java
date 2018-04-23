package com.kag.towercontroller;

import com.kag.common.data.GameData;
import com.kag.common.data.ServiceManager;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.ISystem;
import com.kag.commonasset.ZIndex;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.entities.parts.LabelPart;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commontower.spinterfaces.ITower;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
		@ServiceProvider(service = IComponentLoader.class),
		@ServiceProvider(service = ISystem.class)
})
public class TowerGUI implements IComponentLoader, ISystem {

	private static final Family BUY_MENU_ELEMENT_FAMILY = Family.forAll(TowerBuyMenuPart.class);
	private static final int MENU_WIDTH = 3;
	private static final int MENU_START_X = 788;
	private static final int MENU_START_Y = 154;
	private static final int MENU_CELL_WIDTH = 48;
	private static final int MENU_CELL_HEIGHT = 48;
	private static final int MENU_CELL_SPACING_H = 4;
	private static final int MENU_CELL_SPACING_V = 4;

	private Entity towerMenuBackground;
	private ServiceManager<ITower> towerServiceManager;
	private boolean buyMenuInvalid;

	@Override
	public void load(World world) {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

		//Create GUI
		AssetPart towerPanel = assetManager.createTexture(getClass().getResourceAsStream("/TowerPanel.png"));
		towerPanel.setzIndex(ZIndex.GUI_PANELS);
		towerMenuBackground = new Entity();
		towerMenuBackground.addPart(towerPanel);
		towerMenuBackground.addPart(new AbsolutePositionPart(768, 128));

		world.addEntity(towerMenuBackground);

		//Listen for new tower implementations
		towerServiceManager = new ServiceManager<>(ITower.class, this::onTowersChanged, this::onTowersChanged);
	}

	@Override
	public void dispose(World world) {
		world.removeEntity(towerMenuBackground);
		clearBuyMenu(world);

		for (AssetPart asset : towerMenuBackground.getPartsDeep(AssetPart.class)) {
			asset.dispose();
		}
	}


	@Override
	public void update(float dt, World world, GameData gameData) {
		if (buyMenuInvalid) {
			clearBuyMenu(world);
			createBuyMenu(world);

			buyMenuInvalid = false;
		}
	}

	private void onTowersChanged(ITower tower) {
		//Invalidate buy menu. This will result in the update method remaking it
		buyMenuInvalid = true;
	}

	private void clearBuyMenu(World world) {
		for (Entity buyMenuElement : world.getEntitiesByFamily(BUY_MENU_ELEMENT_FAMILY)) {
			world.removeEntity(buyMenuElement);
		}
	}

	private void createBuyMenu(World world) {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		int index = 0;

		for (ITower tower : towerServiceManager.getServiceProviders()) {
			//Calculate position of buy element (centered in the cell)
			int cellX = index % MENU_WIDTH;
			int cellY = index / MENU_WIDTH;

			int pixX = MENU_START_X + cellX * (MENU_CELL_WIDTH + MENU_CELL_SPACING_H) + MENU_CELL_WIDTH / 2;
			int pixY = MENU_START_Y + cellY * (MENU_CELL_HEIGHT + MENU_CELL_SPACING_V) + MENU_CELL_HEIGHT / 2;

			//Retrieve asset and resize so that it fits a cell
			IAsset icon = tower.getAsset();
			AssetPart iconPart = assetManager.createTexture(icon, 0, 0, icon.getWidth(), icon.getHeight());

			float aspectRatio = (float) icon.getWidth() / icon.getHeight();
			if (icon.getWidth() > icon.getHeight()) {
				iconPart.setWidth(MENU_CELL_WIDTH);
				iconPart.setHeight((int) (MENU_CELL_WIDTH / aspectRatio));
			} else {
				iconPart.setHeight(MENU_CELL_HEIGHT);
				iconPart.setWidth((int) (MENU_CELL_HEIGHT * aspectRatio));
			}

			//Create entity
			AbsolutePositionPart positionPart = new AbsolutePositionPart(pixX, pixY);
			BoundingBoxPart bboxPart = new BoundingBoxPart(iconPart.getWidth(), iconPart.getHeight());
			TowerBuyMenuPart buyMenuPart = new TowerBuyMenuPart(tower);
			iconPart.setxOffset(-iconPart.getWidth() / 2);
			iconPart.setyOffset(-iconPart.getHeight() / 2);
			iconPart.setzIndex(ZIndex.TOWER_BASE);

			LabelPart priceLabelPart = new LabelPart(String.valueOf(tower.getCost()));
			priceLabelPart.setzIndex(ZIndex.TOWER_TURRET);
			priceLabelPart.setxOffset(-MENU_CELL_WIDTH / 2);
			priceLabelPart.setyOffset(MENU_CELL_HEIGHT / 2 - 8);

			Entity buyElement = new Entity();
			buyElement.addPart(positionPart);
			buyElement.addPart(bboxPart);
			buyElement.addPart(buyMenuPart);
			buyElement.addPart(iconPart);
			buyElement.addPart(priceLabelPart);

			world.addEntity(buyElement);

			index++;
		}
	}

	@Override
	public int getPriority() {
		return UPDATE_PASS_1;
	}
}
