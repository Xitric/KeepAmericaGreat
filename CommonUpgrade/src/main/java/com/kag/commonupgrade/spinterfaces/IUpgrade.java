package com.kag.commonupgrade.spinterfaces;


import com.kag.common.entities.Entity;
import com.kag.commonasset.spinterfaces.IAsset;

public interface IUpgrade {
    IAsset getAsset();
    boolean isTowerCompatible(Entity entity);
    void upgrade(Entity entity);
    int getCost(Entity entity);
}