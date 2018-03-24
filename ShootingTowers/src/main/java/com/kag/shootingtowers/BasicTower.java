package com.kag.shootingtowers;

import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.interfaces.ITower;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ITower.class)
public class BasicTower implements ITower {

	private AssetPart assetPart;

	@Override
	public AssetPart getAsset() {
		if (assetPart == null) {
			IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
			assetPart = assetManager.createTexture(getClass().getResourceAsStream("/towerTest.png"));
			assetPart.setzIndex(10);
		}

		return assetPart;
	}

	@Override
	public Entity create() {
		Entity newTower = ShootingTowerFactory.getInstance().createTower("Basic Tower", 2, 96, 1000, 0.03f, 10, (float) (Math.PI / 4), assetPart);

		return newTower;
	}
}
