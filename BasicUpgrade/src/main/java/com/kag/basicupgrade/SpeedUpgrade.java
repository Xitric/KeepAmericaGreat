package com.kag.basicupgrade;


import com.kag.common.entities.Entity;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commontower.entities.parts.WeaponPart;
import com.kag.commonupgrade.spinterfaces.IUpgrade;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IUpgrade.class)
public class SpeedUpgrade implements IUpgrade {

    @Override
    public IAsset getAsset() {
        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
        return assetManager.loadAsset(getClass().getResourceAsStream("/speed.png"));
    }

    @Override
    public boolean isTowerCompatible(Entity entity) {
        return entity.hasPart(WeaponPart.class);
    }

    @Override
    public void upgrade(Entity entity) {
        SpeedUpgradePart upgradePart = entity.getPart(SpeedUpgradePart.class);
        if (!entity.hasPart(SpeedUpgradePart.class)) {
            upgradePart = new SpeedUpgradePart();
            entity.addPart(upgradePart);
        }

        if (upgradePart.getLevel() <= 4) {
            upgradePart.setMultiplier(upgradePart.getMultiplier() * 1.5f);
            upgradePart.setCost(upgradePart.getCost() * 4);
            upgradePart.setLevel(upgradePart.getLevel() + 1);
        }

        WeaponPart weaponPart = entity.getPart(WeaponPart.class);
        weaponPart.setAttackSpeed((int) Math.ceil(weaponPart.getAttackSpeed() * upgradePart.getMultiplier()));
    }

    @Override
    public int getCost(Entity entity) {
	    SpeedUpgradePart upgradePart = entity.getPart(SpeedUpgradePart.class);
        return entity.hasPart(SpeedUpgradePart.class) ? upgradePart.getCost() : new SpeedUpgradePart().getCost();
    }
}
