package com.kag.mapmenu;

import com.kag.common.data.*;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.input.Keyboard;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IGame;
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

	@Override
	public void load(World world) {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		icon = assetManager.loadAsset(getClass().getResourceAsStream("/MapIcon.png"));
	}

	@Override
	public void dispose(World world) {
		icon.dispose();
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
		//TODO: Show menu
		Lookup.getDefault().lookup(IGame.class).startNewGame();
	}
}
