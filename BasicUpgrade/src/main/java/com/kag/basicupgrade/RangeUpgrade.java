package com.kag.basicupgrade;


import com.kag.common.entities.Entity;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commontower.entities.parts.WeaponPart;
import com.kag.commonupgrade.spinterfaces.IUpgrade;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IUpgrade.class)
public class RangeUpgrade implements IUpgrade {

	@Override
	public IAsset getAsset() {
		IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
		return assetManager.loadAsset(getClass().getResourceAsStream("/bow21.png"));
	}

	@Override
	public boolean isTowerCompatible(Entity entity) {
		return entity.hasPart(WeaponPart.class);
	}

	@Override
	public void upgrade(Entity entity) {
		RangeUpgradePart upgradePart = entity.getPart(RangeUpgradePart.class);
		if (!entity.hasPart(RangeUpgradePart.class)) {
			upgradePart = new RangeUpgradePart();
			entity.addPart(upgradePart);
		}

		if (upgradePart.getLevel() <= 4) {
			upgradePart.setMultiplier(upgradePart.getMultiplier() * 1.33f);
			upgradePart.setCost(upgradePart.getCost() * 4);
			upgradePart.setLevel(upgradePart.getLevel() + 1);
		}

		WeaponPart weaponPart = entity.getPart(WeaponPart.class);
		weaponPart.setRange((int) Math.ceil(weaponPart.getRange() * upgradePart.getMultiplier()));
	}

	@Override
	public int getCost(Entity entity) {
		RangeUpgradePart upgradePart = entity.getPart(RangeUpgradePart.class);
		return entity.hasPart(RangeUpgradePart.class) ? upgradePart.getCost() : new RangeUpgradePart().getCost();
	}
}
