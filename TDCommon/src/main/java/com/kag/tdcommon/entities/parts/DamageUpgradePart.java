package com.kag.tdcommon.entities.parts;

import com.kag.common.entities.IPart;

public class DamageUpgradePart implements IPart {
    private int level = 0;
    private int cost = 50;
    private double damageMultiplier = 1.33;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public void setDamageMultiplier(double damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }



}
