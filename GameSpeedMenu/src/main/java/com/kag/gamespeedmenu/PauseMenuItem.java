package com.kag.gamespeedmenu;

import com.kag.common.data.GameData;
import com.kag.common.data.IAsset;
import com.kag.common.data.Keyboard;
import com.kag.common.data.World;
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
public class PauseMenuItem extends MultiIconMenuItem implements IMenuItem, IComponentLoader {

	private IAsset runIcon;
	private IAsset pauseIcon;
	private boolean paused;
	private Entity menuItemEntity;

	@Override
	public void load(World world) {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		runIcon = assetManager.loadAsset(getClass().getResourceAsStream("/Run.png"));
		pauseIcon = assetManager.loadAsset(getClass().getResourceAsStream("/Pause.png"));
	}

	@Override
	public void dispose(World world) {
		runIcon.dispose();
		pauseIcon.dispose();
	}

	@Override
	public Entity getMenuItemEntity() {
		AssetPart iconAsset = getAssetPart(pauseIcon);
		AbsolutePositionPart position = new AbsolutePositionPart(0, 0);
		BoundingBoxPart boundingBox = new BoundingBoxPart(pauseIcon.getWidth(), pauseIcon.getHeight());

		menuItemEntity = new Entity();
		menuItemEntity.addPart(iconAsset);
		menuItemEntity.addPart(position);
		menuItemEntity.addPart(boundingBox);

		return menuItemEntity;
	}

	@Override
	public int getHotkey() {
		return Keyboard.KEY_P;
	}

	@Override
	public void onAction(World world, GameData gameData) {
		paused = !paused;

		if (paused) {
			gameData.setSpeedMultiplier(0);

			menuItemEntity.removePart(menuItemEntity.getPartsDeep(AssetPart.class).iterator().next());
			menuItemEntity.addPart(getAssetPart(runIcon));
		} else {
			SpeedMenuItem speedMenuItem = Lookup.getDefault().lookup(SpeedMenuItem.class);
			if (speedMenuItem != null) {
				speedMenuItem.setSpeed(gameData, speedMenuItem.getSpeed());
			} else {
				gameData.setSpeedMultiplier(1);
			}

			menuItemEntity.removePart(menuItemEntity.getPartsDeep(AssetPart.class).iterator().next());
			menuItemEntity.addPart(getAssetPart(pauseIcon));
		}
	}

	public void unpause() {
		if (paused) {
			paused = false;

			menuItemEntity.removePart(menuItemEntity.getPartsDeep(AssetPart.class).iterator().next());
			menuItemEntity.addPart(getAssetPart(pauseIcon));
		}
	}
}
