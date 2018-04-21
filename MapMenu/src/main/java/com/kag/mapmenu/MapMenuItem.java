package com.kag.mapmenu;

import com.kag.common.data.GameData;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.input.Keyboard;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.commonasset.ZIndex;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commonmenu.spinterfaces.IMenuItem;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 * @author Kasper
 */
@ServiceProviders(value = {
		@ServiceProvider(service = IMenuItem.class),
		@ServiceProvider(service = IComponentLoader.class)
})
public class MapMenuItem implements IMenuItem, IComponentLoader {

	private IAsset icon;
	private AssetPart mapMenuBackground;
	private Entity mapMenu;

	@Override
	public void load(World world) {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		icon = assetManager.loadAsset(getClass().getResourceAsStream("/MapIcon.png"));
		mapMenuBackground = assetManager.createTexture(getClass().getResourceAsStream("/MapMenu.png"));
		mapMenuBackground.setzIndex(ZIndex.MENU_BACKGROUND);

		mapMenu = new Entity();
		mapMenu.addPart(mapMenuBackground);
		mapMenu.addPart(new AbsolutePositionPart(59, 640));
		mapMenu.addPart(new MapMenuPart());
	}

	@Override
	public void dispose(World world) {
		icon.dispose();
		mapMenuBackground.dispose();

		world.removeEntity(mapMenu);
	}

	@Override
	public Entity getMenuItemEntity() {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

		AssetPart iconAsset = assetManager.createTexture(icon, 0, 0, icon.getWidth(), icon.getHeight());
		iconAsset.setzIndex(ZIndex.MENU_ITEM_ICON);
		AbsolutePositionPart position = new AbsolutePositionPart(0, 0);
		BoundingBoxPart boundingBox = new BoundingBoxPart(icon.getWidth(), icon.getHeight());

		Entity menuItemEntity = new Entity();
		menuItemEntity.addPart(iconAsset);
		menuItemEntity.addPart(position);
		menuItemEntity.addPart(boundingBox);

		return menuItemEntity;
	}

	@Override
	public int getHotkey() {
		return Keyboard.KEY_M;
	}

	@Override
	public void onAction(World world, GameData gameData) {
		MapMenuPart mapMenuPart = mapMenu.getPart(MapMenuPart.class);
		mapMenuPart.setVisible(! mapMenuPart.isVisible());

		if (mapMenuPart.isVisible() && ! world.getAllEntities().contains(mapMenu)) {
			world.addEntity(mapMenu);
		}
		//Lookup.getDefault().lookup(IGame.class).startNewGame();
	}
}
