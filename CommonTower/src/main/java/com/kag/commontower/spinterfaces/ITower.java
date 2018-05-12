package com.kag.commontower.spinterfaces;

import com.kag.common.entities.Entity;
import com.kag.commonasset.spinterfaces.IAsset;

public interface ITower {
    IAsset getAsset();
    Entity create();
    IAsset getProjectileAsset();
    int getCost();
}
