/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.commonenemybuff;

import com.kag.common.entities.Entity;
import com.kag.common.entities.parts.MovingPart;

/**
 *
 * @author danie
 */
public class SpeedBuffPart extends BuffPart {

    public SpeedBuffPart(int buffValue, int buffRadius) {
        super(buffValue, buffRadius);
    }

    @Override
    public void buff(Entity entityToBuff, float delta) {
        MovingPart movingPart = entityToBuff.getPart(MovingPart.class);
        if (movingPart == null) {
            return;
        }
        if (movingPart.getMovementSpeed() < this.getBuffValue()) {
            movingPart.setMovementSpeed(this.getBuffValue());
        }
    }

}
