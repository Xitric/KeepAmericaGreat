package com.kag.common.entities.parts;

import com.kag.common.entities.IPart;

public class MovingPart implements IPart {

	private float movementSpeed;

	public MovingPart(float movementSpeed) {
		this.movementSpeed = movementSpeed;
	}

	public float getMovementSpeed() {
		return movementSpeed;
	}

	public void setMovementSpeed(float movementSpeed) {
		this.movementSpeed = movementSpeed;
	}
}
