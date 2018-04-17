package com.kag.basicupgrade;

import com.kag.common.data.IAsset;
import com.kag.common.entities.Entity;
import com.kag.common.spinterfaces.IAssetManager;
import com.kag.tdcommon.entities.parts.RangeUpgradePart;
import com.kag.tdcommon.entities.parts.WeaponPart;
import com.kag.tdcommon.spinterfaces.IUpgrade;
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
            upgradePart.setRangeMultiplier(upgradePart.getRangeMultiplier() * 1.33);
            upgradePart.setCost(upgradePart.getCost() * 4);
            upgradePart.setLevel(upgradePart.getLevel() + 1);
        }

        WeaponPart weaponPart = entity.getPart(WeaponPart.class);
        weaponPart.setDamage((int) Math.ceil(weaponPart.getDamage() * upgradePart.getRangeMultiplier()));
    }

    @Override
    public int getCost(Entity entity) {
        RangeUpgradePart upgradePart = entity.getPart(RangeUpgradePart.class);
        return entity.hasPart(RangeUpgradePart.class) ? upgradePart.getCost() : new RangeUpgradePart().getCost();
    }
}
