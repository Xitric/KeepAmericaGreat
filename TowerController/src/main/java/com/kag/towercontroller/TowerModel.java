package com.kag.towercontroller;

import com.kag.common.entities.Entity;
import com.kag.tdcommon.spinterfaces.ITower;

public class TowerModel {

    private Entity towerEntity;
    private ITower iTower;

    public TowerModel(Entity towerEntity, ITower iTower) {
        this.towerEntity = towerEntity;
        this.iTower = iTower;
    }

    public Entity getTowerEntity() {
        return towerEntity;
    }

    public void setTowerEntity(Entity towerEntity) {
        this.towerEntity = towerEntity;
    }

    public ITower getITower() {
        return iTower;
    }

    public void setiTower(ITower iTower) {
        this.iTower = iTower;
    }

}
