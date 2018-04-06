package com.kag.common.entities.parts;

import com.kag.common.entities.IPart;

public class DamagePart implements IPart {
    private int damage;
    private float movingSpeed;

    public DamagePart(int damage, float movingSpeed) {
        this.damage = damage;
        this.movingSpeed = movingSpeed;

    }
    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getMovingSpeed() {
        return movingSpeed;
    }

    public void setMovingSpeed(float movingSpeed) {
        this.movingSpeed = movingSpeed;
    }


}
