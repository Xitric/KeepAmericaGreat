package com.kag.shootingtowers;

import com.kag.common.data.IAsset;
import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.interfaces.ITower;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ITower.class)
public class BasicTower implements ITower {

	@Override
	public IAsset getAsset() {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		return assetManager.loadAsset(getClass().getResourceAsStream("/towerTest.png"));
	}

	@Override
	public Entity create() {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		AssetPart assetPart = assetManager.createTexture(getClass().getResourceAsStream("/towerTest.png"));
		assetPart.setzIndex(30);

		return ShootingTowerFactory.getInstance().createTower("Basic Tower", 2, 100, 0.25f, 200, 10, (float) (Math.PI / 4), assetPart);
	}
}
