/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.entities;

import com.kag.common.data.World;
import com.kag.common.interfaces.IEntitySystem;

/**
 *
 * @author Sofie Jørgensen
 */
public class EntitySystem implements IEntitySystem {

    @Override
    public void update(float delta, Entity entity, World world) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getPriority() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 
    
}
