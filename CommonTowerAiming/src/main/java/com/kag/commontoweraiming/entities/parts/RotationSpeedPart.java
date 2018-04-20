package com.kag.commontoweraiming.entities.parts;

import com.kag.common.entities.IPart;

public class RotationSpeedPart implements IPart {
	private int rotationSpeed;

	public RotationSpeedPart(int rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	public int getRotationSpeed() {
		return rotationSpeed;
	}

	public void setRotationSpeed(int rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}
}
