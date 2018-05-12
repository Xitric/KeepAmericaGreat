/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.commonenemybuff;

import com.kag.common.entities.Entity;
import com.kag.commontd.entities.parts.LifePart;

/**
 *
 * @author Vedsted
 */
public class LifeBuffPart extends BuffPart{
    
    private int buffTimer = 0;
    
    public LifeBuffPart(int buffValue, int buffRadius) {
        super(buffValue, buffRadius);
    }

    @Override
    public void buff(Entity entityToBuff, float delta) {
        LifePart lp = entityToBuff.getPart(LifePart.class);
        
        if (lp == null){
            return;
        }
        buffTimer++;
        
        if (buffTimer > 10){
        lp.setHealth(lp.getHealth() + super.getBuffValue());
        buffTimer = 0;
        }
    }
    
}
