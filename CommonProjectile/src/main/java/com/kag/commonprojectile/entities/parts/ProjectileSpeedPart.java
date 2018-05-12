package com.kag.commonprojectile.entities.parts;

import com.kag.common.entities.IPart;

public class ProjectileSpeedPart implements IPart {
	private int projectileSpeed;

	public ProjectileSpeedPart(int projectileSpeed) {
		this.projectileSpeed = projectileSpeed;
	}

	public int getProjectileSpeed() {
		return projectileSpeed;
	}

	public void setProjectileSpeed(int projectileSpeed) {
		this.projectileSpeed = projectileSpeed;
	}

}
