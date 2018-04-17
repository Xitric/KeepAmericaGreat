package com.kag.basicupgrade;

import com.kag.common.data.IAsset;
import com.kag.common.entities.Entity;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.tdcommon.entities.parts.WeaponPart;
import com.kag.tdcommon.spinterfaces.IUpgrade;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IUpgrade.class)
public class DamageUpgrade implements IUpgrade {
	private int cost = 50;
	private double damageIncrement = 1.33;

	@Override
	public IAsset getAsset() {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		return assetManager.loadAsset(getClass().getResourceAsStream("/sword.png"));
	}

	@Override
	public boolean isTowerCompatible(Entity entity) {
		return entity.hasPart(WeaponPart.class);
	}

	@Override
	public void upgrade(Entity entity) {
		WeaponPart weaponPart = entity.getPart(WeaponPart.class);
		weaponPart.setDamage((int)Math.ceil(weaponPart.getDamage() * damageIncrement));
	}

	@Override
	public int getCost() {
		return cost;
	}

}
