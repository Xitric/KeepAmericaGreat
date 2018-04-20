package com.kag.upgradecontroller;

import com.kag.common.entities.Entity;
import com.kag.commonupgrade.spinterfaces.IUpgrade;

public class UpgradeModel {
    private Entity upgradeEntity;
    private IUpgrade iUpgrade;

    public UpgradeModel(Entity upgradeEntity, IUpgrade iUpgrade) {
        this.upgradeEntity = upgradeEntity;
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
