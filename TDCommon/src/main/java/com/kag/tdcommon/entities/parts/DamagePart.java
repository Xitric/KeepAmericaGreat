package com.kag.tdcommon.entities.parts;

import com.kag.common.entities.IPart;

public class DamagePart implements IPart {

	private int damage;

	public DamagePart(int damage) {
		this.damage = damage;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

}
