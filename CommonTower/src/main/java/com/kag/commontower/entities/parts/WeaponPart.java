package com.kag.commontower.entities.parts;

import com.kag.common.entities.IPart;

public class WeaponPart implements IPart {

	private int damage;
	private int range;
	private float attackSpeed;
	private float timeSinceLast;

	public WeaponPart(int damage, int range, float attackSpeed) {
		this.damage = damage;
		this.range = range;
		this.attackSpeed = attackSpeed;
	}

	public float getTimeSinceLast() {
		return timeSinceLast;
	}

	public void addDelta(float delta) {
		this.timeSinceLast += delta;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public float getAttackSpeed() {
		return attackSpeed;
	}

	public void setAttackSpeed(float attackSpeed) {
		this.attackSpeed = attackSpeed;
	}
}
