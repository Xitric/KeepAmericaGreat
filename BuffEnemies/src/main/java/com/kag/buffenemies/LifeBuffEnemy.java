/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.buffenemies;

import com.kag.common.entities.Entity;
import com.kag.commonenemy.spinterfaces.IEnemy;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Vedsted
 */
@ServiceProvider(service = IEnemy.class)
public class LifeBuffEnemy implements IEnemy{

    @Override
    public int getDifficulty() {
        return 10;
    }

    @Override
    public Entity create() {
        return BuffEnemyFactory.create(7, 40, 45, 1, 1, 20, this.getClass());
    }
    
}
