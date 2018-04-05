package com.kag.towerparts;

import com.kag.common.entities.IPart;

public class RotationSpeedPart implements IPart {

    private float rotationSpeed;

    public RotationSpeedPart(float rotationSpeed){
        this.rotationSpeed = rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed){
        this.rotationSpeed = rotationSpeed;
    }

    public float getRotationSpeed(){
        return rotationSpeed;
    }
}
