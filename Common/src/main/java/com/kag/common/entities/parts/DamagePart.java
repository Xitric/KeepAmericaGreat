package com.kag.common.entities.parts;

import com.kag.common.entities.IPart;

public class DamagePart implements IPart {
    private int damage;
    private int range;
    private int attackSpeed;
    private int projectileSpeed;

    public DamagePart(int damage, int range, int attackSpeed, int projectileSpeed) {
	this.damage = damage;
	this.range = range;
	this.attackSpeed = attackSpeed;
	this.projectileSpeed = projectileSpeed;
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

    public int getAttackSpeed() {
	return attackSpeed;
    }

    public void setAttackSpeed(int attackSpeed) {
	this.attackSpeed = attackSpeed;
    }

    public int getProjectileSpeed() {
	return projectileSpeed;
    }

    public void setProjectileSpeed(int projectileSpeed) {
	this.projectileSpeed = projectileSpeed;
    }
}
