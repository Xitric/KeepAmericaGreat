package com.kag.basicupgrade;

import com.kag.common.entities.Entity;
import com.kag.common.spinterfaces.IUpgrade;

public class UpgradeModel {
    private Entity upgradeEntity;
    private IUpgrade iUpgrade;

    public UpgradeModel(Entity towerEntity, IUpgrade iUpgrade) {
        this.upgradeEntity = towerEntity;
        this.iUpgrade = iUpgrade;
    }

    public Entity getUpgradeEntity() {
        return upgradeEntity;
    }

    public void setUpgradeEntity(Entity upgradeEntity) {
        this.upgradeEntity = upgradeEntity;
    }

    public IUpgrade getiUpgrade() {
        return iUpgrade;
    }

    public void setiUpgrade(IUpgrade iUpgrade) {
        this.iUpgrade = iUpgrade;
    }

}
