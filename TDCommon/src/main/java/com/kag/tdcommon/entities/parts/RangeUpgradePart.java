package com.kag.tdcommon.entities.parts;

import com.kag.common.entities.IPart;

public class RangeUpgradePart implements IPart {
    private int level = 0;
    private int cost = 50;
    private double rangeMultiplier = 1.1;

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

    public double getRangeMultiplier() {
        return rangeMultiplier;
    }

    public void setRangeMultiplier(double rangeMultiplier) {
        this.rangeMultiplier = rangeMultiplier;
    }
}
