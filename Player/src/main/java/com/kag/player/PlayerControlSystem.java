/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.player;

import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.CurrencyPart;
import com.kag.common.entities.parts.LifePart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.spinterfaces.IEntitySystem;

/**
 *
 * @author niels
 */
public class PlayerControlSystem implements IEntitySystem {

    private static final Family FAMILY = Family.forAll(LifePart.class, CurrencyPart.class, PositionPart.class);
     
    @Override
    public Family getFamily() {
        return FAMILY;
    }

    @Override
    public void update(float delta, Entity entity, World world) {
        
    }

    @Override
    public int getPriority() {
        return 0;
    }

}
