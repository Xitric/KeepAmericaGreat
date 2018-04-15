package com.kag.gamespeedmenu;

import com.kag.common.data.*;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AbsolutePositionPart;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.spinterfaces.IMenuItem;
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
public class SpeedMenuItem extends MultiIconMenuItem implements IMenuItem, IComponentLoader {

	private IAsset[] speedIcons;
	private int speed = 1;
	private Entity menuItemEntity;

	@Override
	public void load(World world) {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		speedIcons = new IAsset[3];
		speedIcons[0] = assetManager.loadAsset(getClass().getResourceAsStream("/Speed1.png"));
		speedIcons[1] = assetManager.loadAsset(getClass().getResourceAsStream("/Speed2.png"));
		speedIcons[2] = assetManager.loadAsset(getClass().getResourceAsStream("/Speed3.png"));
	}

	@Override
	public void dispose(World world) {
		for (IAsset speedIcon : speedIcons) {
			speedIcon.dispose();
		}
	}

	@Override
	public Entity getMenuItemEntity() {
		AssetPart iconAsset = getAssetPart(speedIcons[0]);
		AbsolutePositionPart position = new AbsolutePositionPart(0, 0);
		BoundingBoxPart boundingBox = new BoundingBoxPart(speedIcons[0].getWidth(), speedIcons[0].getHeight());

		menuItemEntity = new Entity();
		menuItemEntity.addPart(iconAsset);
		menuItemEntity.addPart(position);
		menuItemEntity.addPart(boundingBox);

		return menuItemEntity;
	}

	@Override
	public int getHotkey() {
		return Keyboard.KEY_UP;
	}

	@Override
	public void onAction(World world, GameData gameData) {
		speed = speed % speedIcons.length + 1;
		setSpeed(gameData, speed);

		PauseMenuItem pauseMenuItem = Lookup.getDefault().lookup(PauseMenuItem.class);
		pauseMenuItem.unpause();
	}

	public void setSpeed(GameData gameData, int speed) {
		if (speed > 0 && speed <= speedIcons.length) {
			gameData.setSpeedMultiplier(speed * 2);

			menuItemEntity.removePart(menuItemEntity.getPartsDeep(AssetPart.class).iterator().next());
			menuItemEntity.addPart(getAssetPart(speedIcons[speed - 1]));
		}
	}

	public int getSpeed() {
		return speed;
	}
}
