package com.kag.towerparts;

import com.kag.common.entities.IPart;
import com.kag.interfaces.ITower;

public class TowerPart implements IPart {

    private ITower iTower;

        public TowerPart(ITower itower) {
            this.iTower = itower;
        }

        public ITower getiTower() {
            return iTower;
        }
}
