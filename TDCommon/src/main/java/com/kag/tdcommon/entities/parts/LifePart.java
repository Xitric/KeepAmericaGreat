package com.kag.tdcommon.entities.parts;

import com.kag.common.entities.IPart;

public class LifePart implements IPart {

    private int health;

    public LifePart(int health) {
        this.health = health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }
}
