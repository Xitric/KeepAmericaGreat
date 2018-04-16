package com.kag.gamemenu;

import com.kag.common.data.*;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.ISystem;
import com.kag.tdcommon.spinterfaces.IMenuItem;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kasper
 */
@ServiceProviders(value = {
		@ServiceProvider(service = ISystem.class),
		@ServiceProvider(service = IComponentLoader.class)
})
public class GameMenuController implements ISystem, IComponentLoader {

	private static final int menuBorderX = 4;
	private static final int menuItemSpacingX = 32;

	private ServiceManager<IMenuItem> menuItemServiceManager;
	private List<Entity> menuItemEntities;
	private boolean updateMenuItemEntities;

	private AssetPart menuPaddingLeft;
	private IAsset menuItemBackground;
	private AssetPart menuPaddingRight;
	private Entity menuPadding;

	@Override
	public void load(World world) {
		menuItemEntities = new ArrayList<>();
		menuItemServiceManager = new ServiceManager<>(IMenuItem.class, this::menuItemsChanged, this::menuItemsChanged);

		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		menuPaddingLeft = assetManager.createTexture(getClass().getResourceAsStream("/MenuPaddingLeft.png"));
		menuItemBackground = assetManager.loadAsset(getClass().getResourceAsStream("/MenuItemBackground.png"));
		menuPaddingRight = assetManager.createTexture(getClass().getResourceAsStream("/MenuPaddingRight.png"));

		menuPaddingLeft.setzIndex(ZIndex.MENU_BACKGROUND);
		menuPaddingRight.setzIndex(ZIndex.MENU_BACKGROUND);
		menuPaddingRight.setxOffset(menuBorderX);

		menuPadding = new Entity();
		world.addEntity(menuPadding);
	}

	@Override
	public void dispose(World world) {
		world.removeEntity(menuPadding);

		menuPaddingLeft.dispose();
		menuItemBackground.dispose();
		menuPaddingRight.dispose();
	}

	@Override
	public void update(float dt, World world, GameData gameData) {
		if (updateMenuItemEntities) {
			//Remove menu icons before adding new
			for (Entity menuItemEntity : menuItemEntities) {
				world.removeEntity(menuItemEntity);
			}
			menuItemEntities.clear();

			//Reset menu background
			for (AssetPart part : menuPadding.getPartsDeep(AssetPart.class)) {
				menuPadding.removePart(part);
			}
			menuPadding.addPart(menuPaddingLeft);
			menuPadding.addPart(menuPaddingRight);
			menuPadding.addPart(new AbsolutePositionPart(0, 0));

			//Add new menu icons
			for (IMenuItem menuItem : menuItemServiceManager.getServiceProviders()) {
				Entity e = createMenuItemEntity(menuItem);
				world.addEntity(e);
				menuItemEntities.add(e);
			}

			menuPaddingRight.setxOffset(menuBorderX + menuItemEntities.size() * menuItemSpacingX);
			updateMenuItemEntities = false;
		}

		for (IMenuItem menuItem : menuItemServiceManager.getServiceProviders()) {
			if (gameData.getKeyboard().isKeyPressed(menuItem.getHotkey())) {
				menuItem.onAction(world, gameData);
			}
		}

		//TODO: Mouse press
	}

	private void menuItemsChanged(IMenuItem menuItem) {
		updateMenuItemEntities = true;
	}

	private Entity createMenuItemEntity(IMenuItem menuItem) {
		Entity menuItemEntity = menuItem.getMenuItemEntity();

		int itemX = menuBorderX + menuItemEntities.size() * menuItemSpacingX;
		int itemY = 0;
		AbsolutePositionPart position = menuItemEntity.getPart(AbsolutePositionPart.class);
		position.setPos(itemX, itemY);

		AssetPart background = Lookup.getDefault().lookup(IAssetManager.class).createTexture(menuItemBackground, 0, 0, menuItemBackground.getWidth(), menuItemBackground.getHeight());
		background.setzIndex(ZIndex.MENU_BACKGROUND);
		background.setxOffset(itemX);
		menuPadding.addPart(background);

		return menuItemEntity;
	}

	@Override
	public int getPriority() {
		return UPDATE_PASS_1;
	}
}
