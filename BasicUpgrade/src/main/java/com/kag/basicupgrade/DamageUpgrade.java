package com.kag.basicupgrade;


import com.kag.common.entities.Entity;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commontower.entities.parts.WeaponPart;
import com.kag.commonupgrade.spinterfaces.IUpgrade;

import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IUpgrade.class)
public class DamageUpgrade implements IUpgrade {

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
        DamageUpgradePart upgradePart = entity.getPart(DamageUpgradePart.class);
        if (!entity.hasPart(DamageUpgradePart.class)) {
            upgradePart = new DamageUpgradePart();
            entity.addPart(upgradePart);
        }

        if (upgradePart.getLevel() <= 4) {
            upgradePart.setDamageMultiplier(upgradePart.getDamageMultiplier() * 1.33);
            upgradePart.setCost(upgradePart.getCost() * 4);
            upgradePart.setLevel(upgradePart.getLevel() + 1);
        }

        WeaponPart weaponPart = entity.getPart(WeaponPart.class);
        weaponPart.setDamage((int) Math.ceil(weaponPart.getDamage() * upgradePart.getDamageMultiplier()));
    }

    @Override
    public int getCost(Entity entity) {
        DamageUpgradePart upgradePart = entity.getPart(DamageUpgradePart.class);
        return entity.hasPart(DamageUpgradePart.class) ? upgradePart.getCost() : new DamageUpgradePart().getCost();
    }

}
