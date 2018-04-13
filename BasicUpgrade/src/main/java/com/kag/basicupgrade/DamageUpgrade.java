package com.kag.basicupgrade;

import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.DamagePart;
import com.kag.common.spinterfaces.IUpgrade;

public class DamageUpgrade implements IUpgrade {
    @Override
    public boolean isTowerCompatible(Entity entity) {
        return entity.hasPart(DamagePart.class);
    }

    @Override
    public void upgrade(Entity entity) {
        DamagePart damagePart = entity.getPart(DamagePart.class);
        damagePart.setDamage(damagePart.getDamage()+10);
    }

}
