package com.kag.commonupgrade.spinterfaces;

import com.kag.common.data.IAsset;
import com.kag.common.entities.Entity;

public interface IUpgrade {
    IAsset getAsset();
    boolean isTowerCompatible(Entity entity);
    void upgrade(Entity entity);
    int getCost(Entity entity);
}