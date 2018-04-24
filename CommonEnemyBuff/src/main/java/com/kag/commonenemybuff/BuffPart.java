/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.commonenemybuff;

import com.kag.common.entities.Entity;
import com.kag.common.entities.IPart;

/**
 *
 * @author danie
 */
public abstract class BuffPart implements IPart{
    
    private int buffValue;
    private int buffRadius;

    public BuffPart(int buffValue, int buffRadius) {
        this.buffValue = buffValue;
        this.buffRadius = buffRadius;
    }

    public int getBuffValue() {
        return buffValue;
    }

    public int getBuffRadius() {
        return buffRadius;
    }   

    public abstract void buff(Entity entityToBuff, float delta);
    
}
