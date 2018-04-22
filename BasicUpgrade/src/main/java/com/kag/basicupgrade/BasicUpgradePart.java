package com.kag.basicupgrade;

import com.kag.common.entities.IPart;

/**
 * @author Kasper
 */
public abstract class BasicUpgradePart implements IPart {

	private int level;
	private int cost;
	private float multiplier;

	public BasicUpgradePart(int baseCost, float baseMultiplier) {
		cost = baseCost;
		multiplier = baseMultiplier;
	}

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

	public float getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(float multiplier) {
		this.multiplier = multiplier;
	}
}
